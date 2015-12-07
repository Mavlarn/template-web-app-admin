package com.datatalk.xyztemp.weixin.service;

import com.datatalk.xyztemp.domain.User;
import com.datatalk.xyztemp.repository.UserRepository;
import com.datatalk.xyztemp.service.UserService;
import com.datatalk.xyztemp.service.util.ServiceConditionUtils;
import com.datatalk.xyztemp.weixin.domain.WxUser;
import com.datatalk.xyztemp.weixin.repository.WxUserRepository;
import com.datatalk.xyztemp.weixin.web.rest.ActivateDTO;
import com.google.common.base.Preconditions;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/12
 */
@Service
public class WxUserService {

    private static final Logger LOG = LoggerFactory.getLogger(WxUserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;
    @Inject
    private UserRepository userRepository;
    @Inject
    private UserService userService;
    @Inject
    private WxUserRepository wxUserRepository;
    @Inject
    private Environment environment;
    @Inject
    private WxConfiguration wxConfiguration;

    /**
     * Activate the user based on his key.
     *
     * @param activateDTO
     * @param wxMpUser
     * @return
     */
    @Transactional
    public User activateUser(ActivateDTO activateDTO, WxMpUser wxMpUser) {
        User user = userRepository.findOneByActivationKey(activateDTO.getActivationKey()).get();
        ServiceConditionUtils.checkState(user != null, "授权码无效");
        // TODO: for test, we can bind multiple times.
//        ServiceConditionUtils.checkState(!user.getActivated(), "该用户已被绑定");
        ServiceConditionUtils.checkState(!user.isDeleted(), "用户已经被停止使用!");
        ServiceConditionUtils.checkState(activateDTO.getRealName().equals(user.getRealName()), "绑定的用户名称与授权码不符");

        user.getAuthorities().size(); // get roles eagerly, will be used later.
        user.setActivated(true);
        // not remove activation key, user can re-activate with admin side and weixin side.
//        user.setActivationKey(null);
        // using name during creating.
//        user.setName(wxUser.getNickname());
        user.setOpenId(wxMpUser.getOpenId());
        user.setUseServiceId(activateDTO.getServiceId());

        // TODO:
//        user.setSex(wxUser.getSex());
        LOG.debug("wxMpUser.getSex(): {}", wxMpUser.getSex());
        user.setLastModifiedBy(user.getId());
        user.setLastModifiedDate(ZonedDateTime.now());
        // user will be saved in download function.
        downloadAndSaveWeixinHeadPic(user, wxMpUser.getHeadImgUrl());
        LOG.debug("Activated user: {}", user);
        //update the wxUser data
        WxUser wxUser = wxUserRepository.findOneByUseServiceIdAndOpenId(activateDTO.getServiceId(), wxMpUser.getOpenId());
        if (wxUser != null) {
            wxUser.setBindDate(ZonedDateTime.now());
            wxUserRepository.save(wxUser);
        }
        return user;
    }

    /**
     * Save the user into DB for subscription, it is used for subscription statistic.
     * @throws WxErrorException
     */
    @Async
    public void subscribeWxUser(Long serviceId, String userOpenId) throws WxErrorException {
        // save this wx user into DB.
        WxUser wxUser = wxUserRepository.findOneByUseServiceIdAndOpenId(serviceId, userOpenId);
        WxMpUser wxMpUser = getWxMpUser(serviceId, userOpenId);
        // maybe user un-subscribed and subscribed again.
        // FIXME: now the WxUser is deleted for un-subscribe. So this should be always Null.
        if (wxUser == null) {
            wxUser = new WxUser();
            wxUser.setUseServiceId(serviceId);
            wxUser.setOpenId(userOpenId);
            wxUser.setCountry(wxMpUser.getCountry());
            wxUser.setProvince(wxMpUser.getProvince());
            wxUser.setCity(wxMpUser.getCity());
            wxUser.setNickName(wxMpUser.getNickname());
            wxUser.setSubscribeDate(ZonedDateTime.now());
        } else {
            wxUser.setSubscribeDate(ZonedDateTime.now());
        }
        wxUserRepository.save(wxUser);
    }

    @Transactional
    public void unSubscribeUser(Long serviceId, String userOpenId) {
        userRepository.findOneByUseServiceIdAndOpenId(serviceId, userOpenId)
            .ifPresent(user -> {
                user.setActivated(false);
                userService.clearWxInfo(user);
                userRepository.save(user);
                wxUserRepository.deleteByUseServiceIdAndOpenId(serviceId, userOpenId);
            });
    }

    @Transactional(readOnly = true)
    public Optional<User> getWxUser(Long serviceId, String wxUserOpenId) {
        return userRepository.findOneByUseServiceIdAndOpenId(serviceId, wxUserOpenId)
            .map(user -> {
                user.getAuthorities().size();
                return user;
            });
    }

    private WxMpUser getWxMpUser(Long serviceId, String openId) throws WxErrorException {
        WxMpService wxMpService = wxConfiguration.getWxService(serviceId);
        String lang = "zh_CN"; //语言
        return wxMpService.userInfo(openId, lang);
    }

    private void downloadAndSaveWeixinHeadPic(User user, String headImgUrl) {
        URL url;
        try {
            Preconditions.checkNotNull(user.getId(), "用户ID不存在");
            url = new URL(headImgUrl);
            String savePath = environment.getProperty("app.upload.path");
            File subDir = getUserHeadPicDir(savePath, user);
            String fileName = new File(headImgUrl).getName();
            fileName = userService.getUserPicFileName(user, fileName);
            File userPicFile = new File(subDir, fileName);
            FileUtils.copyURLToFile(url, userPicFile);

            String imagePath = userPicFile.getAbsolutePath().substring(savePath.length() + 1);
//            qiniuService.uploadFile(userPicFile, qiniuPath);
            user.setHeadImgUrl(imagePath);
            userRepository.save(user);

        } catch (MalformedURLException e) {
            LOG.error("Weixin head image url error:" + e.getMessage(), e);
        } catch (IOException e) {
            LOG.error("Save Weixin head image error:" + e.getMessage(), e);
        }
    }

    private File getUserHeadPicDir(String fileRootPath, User user) throws IOException {
        File saveFir = new File(fileRootPath);
        File userModuleDir = new File(saveFir, "user");
        String subDirName = String.valueOf(user.getId() % 1000);
        File subDir = new File(userModuleDir, subDirName);
        if (!subDir.exists()) {
            FileUtils.forceMkdir(subDir);
        }
        return subDir;
    }
}
