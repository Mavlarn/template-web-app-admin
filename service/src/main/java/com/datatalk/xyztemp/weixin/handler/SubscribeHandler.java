package com.datatalk.xyztemp.weixin.handler;

import java.util.Map;

import com.datatalk.xyztemp.weixin.domain.WxMPConfig;
import com.datatalk.xyztemp.weixin.service.WxHandlerService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/12
 */
public class SubscribeHandler implements WxMpMessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeHandler.class);

    private WxMPConfig mpConfig;

    private WxHandlerService wxHandlerService;

    public SubscribeHandler(WxMPConfig mpConfig, WxHandlerService wxHandlerService) {
        this.mpConfig = mpConfig;
        this.wxHandlerService = wxHandlerService;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        long t1 = System.currentTimeMillis();
        String userOpenId = wxMessage.getFromUserName();
        String openId = wxMessage.getToUserName();

        wxHandlerService.subscribeWxUser(mpConfig.getId(), userOpenId);

        String url = wxHandlerService.getServerUrl() + "/index.html#/app/userHome" + "?openId=" + userOpenId
                + "&serviceId=" + mpConfig.getId();
        String content = mpConfig.getHello();
        content = content.replace("【这里】", " <a href=\""+ url + "\">【这里】</a> ");

        WxMpXmlOutMessage rtnMsg = WxMpXmlOutMessage.TEXT()
                .fromUser(openId).toUser(userOpenId)
                .content(content).build();
        LOG.debug("Hello content:{}, used time:{}", content, (System.currentTimeMillis() - t1));
        return rtnMsg;
    }

}
