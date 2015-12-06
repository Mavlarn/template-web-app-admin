package com.datatalk.xyztemp.weixin.web;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codahale.metrics.annotation.Timed;
import com.datatalk.xyztemp.config.Constants;
import com.datatalk.xyztemp.service.SysConfigService;
import com.datatalk.xyztemp.weixin.domain.WxMPConfig;
import com.datatalk.xyztemp.weixin.domain.WxServiceMenu;
import com.datatalk.xyztemp.weixin.repository.WxMPConfigRepository;
import com.datatalk.xyztemp.weixin.repository.WxMenuRepository;
import com.datatalk.xyztemp.weixin.service.WxConfiguration;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/5/20
 */
@Controller
@RequestMapping("/wx")
public class WxInitController {

    private static final Logger LOG = LoggerFactory.getLogger(WxInitController.class);

    @Inject
    private WxMenuRepository wxMenuRepository;
    @Inject
    private WxConfiguration wxConfiguration;
    @Inject
    private WxMPConfigRepository mpConfigRepository;
    @Inject
    private SysConfigService sysConfigService;
    @Inject
    private Environment environment;

    @PostConstruct
    public void initWeixinConfig() {

    }

    @RequestMapping(value = "/interface", method = RequestMethod.GET)
    @Timed
    public void wxInterface(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.debug("REST request from weixin.");

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        // because all tokens are same, so using default mpService to check signature.
        WxMpService wxMpService = wxConfiguration.getDefaultWxService();
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            LOG.info("BAD Signature from weixin.");
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().println(echostr);
            return;
        }

        response.getWriter().println("非法请求");
        return;

    }

    @RequestMapping(value = "/interface", method = RequestMethod.POST)
    @Timed
    public void wxInterfacePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");

        String encryptType = request.getParameter("encrypt_type");
        encryptType = StringUtils.isBlank(encryptType) ? "raw" : encryptType;

        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            LOG.debug("[RAW]REST post request from weixin user:{}", inMessage.getFromUserName());
            String serviceOpenId = inMessage.getToUserName();
            WxMPConfig mpConfig = mpConfigRepository.findOneByUseOpenId(serviceOpenId);
            WxMpMessageRouter wxMpMessageRouter = wxConfiguration.getWxMpMessageRouter(mpConfig.getId());
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (outMessage != null) {
                response.getWriter().write(outMessage.toXml());
            }
            return;
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            // get ToUserName from message
            String requestStr = IOUtils.toString(request.getInputStream(), "UTF-8");

//            WxMpEncXmlMessage tmpMsg = WxMpEncXmlMessage.fromEncriptedXML(requestStr);
            WxMpXmlMessage tmpMessage = WxMpXmlMessage.fromXml(requestStr); // user open id can be got with plain
            LOG.debug("[AES]REST post request from weixin user:{}", tmpMessage.getFromUserName());
            String serviceOpenId = tmpMessage.getToUserName();
            WxMPConfig mpConfig = mpConfigRepository.findOneByUseOpenId(serviceOpenId);
            WxMpMessageRouter wxMpMessageRouter = wxConfiguration.getWxMpMessageRouter(mpConfig.getId());
            WxMpConfigStorage configStorage = wxConfiguration.getWxMpConfigStorage(mpConfig.getId());


            String msgSignature = request.getParameter("msg_signature");
            String nonce = request.getParameter("nonce");
            String timestamp = request.getParameter("timestamp");
            // can not support
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestStr, configStorage, timestamp, nonce,
                    msgSignature);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (outMessage != null) {
                response.getWriter().write(outMessage.toEncryptedXml(configStorage));
            }
            return;
        }

        response.getWriter().println("不可识别的加密类型");
        return;

    }

    /**
     * @param code 是微信返回的用户的code
     * @param key 是自定义菜单所对应的菜单的key
     * @param serviceId 是服务号所对应的Id
     */
    @RequestMapping(value = "/index/{serviceId}/{key}")
    public String wxIndex(@RequestParam(required = false) String code, @PathVariable Long serviceId,
                          @PathVariable String key) {
        LOG.debug("REST request for weixin index.");
        String debugLogStr = sysConfigService.isWxAppDebug() ? "&debugLog=true" : "";
        WxServiceMenu wxMenu = wxMenuRepository.findOneByKeyCodeAndServiceId(key, serviceId);
        if (environment.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            return "redirect:http://app.data-talks.org/index.html#" + wxMenu.getUrl() + "?code=" + code +
                    "&serviceId=" + serviceId + debugLogStr;
        } else {
            return "redirect:http://app.data-talks.org/index.html#" + wxMenu.getUrl() + "?code=" + code +
                    "&serviceId=" + serviceId + debugLogStr;
        }
    }
}
