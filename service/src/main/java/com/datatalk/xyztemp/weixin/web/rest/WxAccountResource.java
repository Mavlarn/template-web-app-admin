package com.datatalk.xyztemp.weixin.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.datatalk.xyztemp.domain.User;
import com.datatalk.xyztemp.security.WeixinUsernamePasswordAuthenticationToken;
import com.datatalk.xyztemp.security.xauth.Token;
import com.datatalk.xyztemp.security.xauth.TokenProvider;
import com.datatalk.xyztemp.service.UserService;
import com.datatalk.xyztemp.web.rest.dto.ManagedUserDTO;
import com.datatalk.xyztemp.web.rest.dto.UserDTO;
import com.datatalk.xyztemp.weixin.service.WxConfiguration;
import com.datatalk.xyztemp.weixin.service.WxUserService;
import com.google.common.collect.Maps;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/12
 */
@RestController
@RequestMapping("/api/wx")
public class WxAccountResource {
    private static final Logger LOG = LoggerFactory.getLogger(WxAccountResource.class);

    @Inject
    private WxConfiguration wxConfiguration;
    @Inject
    private WxUserService wxUserService;
    @Inject
    private UserService userService;
    @Inject
    private AuthenticationManager authenticationManager;
    @Inject
    private UserDetailsService userDetailsService;
    @Inject
    private TokenProvider tokenProvider;

    /**
     * @param code 是微信返回的用户的code
     * @param serviceId 是服务号所对应的Id
     */
    @RequestMapping(value = "/auth",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Map weixinAuth(@RequestParam String code, @RequestParam Long serviceId)
            throws WxErrorException {
        LOG.debug("weixin user authenticate with code:{}, service:{}", code, serviceId);

        WxMpService wxMpService = wxConfiguration.getWxService(serviceId);
        try {
            WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(code);
            return wxUserService.getWxUser(serviceId, accessToken.getOpenId())
                .map(user -> {
                    if (user.isDeleted()) {
                        return authErrorResult(accessToken.getOpenId(), "用户已经被删除");
                    } else {
                        ManagedUserDTO userDetailDto = new ManagedUserDTO(user);
                        LOG.debug("Got weixin user:{}", userDetailDto);
                        return authSuccessResult(userDetailDto, user, accessToken.getOpenId());
                    }
                })
                .orElseGet(() -> authErrorResult(accessToken.getOpenId(), "用户不存在"));
        } catch (WxErrorException wxe) {
            LOG.error("Weixin error:" + wxe.getMessage(), wxe);
            return authErrorResult(null, "微信错误!");
        }
    }

    private Map authErrorResult(String openId, String msg) {
        Map rtnMap = Maps.newHashMap();
        rtnMap.put("message", msg);
        rtnMap.put("openId", openId);
        rtnMap.put("success", false);
        return rtnMap;
    }

    /**
     * Use both UserDTO and User, because only user has password.
     *
     * @param userDetailTO
     * @param user
     * @param openId
     * @return
     */
    private Map authSuccessResult(UserDTO userDetailTO, User user, String openId) {
        WeixinUsernamePasswordAuthenticationToken authToken = new WeixinUsernamePasswordAuthenticationToken(user
                .getLogin(), user.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails details = this.userDetailsService.loadUserByUsername(user.getLogin());
        Token token = tokenProvider.createToken(details);
        Map rtnMap = Maps.newHashMap();
        rtnMap.put("user", userDetailTO);
        rtnMap.put("token", token);
        rtnMap.put("openId", openId);
        rtnMap.put("success", true);
        return rtnMap;
    }

    /**
     * Bind this user with some system user to activate. And make it as authenticated.
     */
    @RequestMapping(value = "/activateUser",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Map activateUser(@RequestBody ActivateDTO activateDTO) throws WxErrorException {
        LOG.debug("Activate user:{}", activateDTO);
        WxMpUser wxUser = getWxMpUser(activateDTO.getServiceId(), activateDTO.getOpenId());
        User user = wxUserService.activateUser(activateDTO, wxUser);
        ManagedUserDTO userDTO = new ManagedUserDTO(user);
        return authSuccessResult(userDTO, user, activateDTO.getOpenId());
    }

    private WxMpUser getWxMpUser(Long serviceId, String openId) throws WxErrorException {
        WxMpService wxMpService = wxConfiguration.getWxService(serviceId);
        String lang = "zh_CN"; //语言
        return wxMpService.userInfo(openId, lang);
    }

}
