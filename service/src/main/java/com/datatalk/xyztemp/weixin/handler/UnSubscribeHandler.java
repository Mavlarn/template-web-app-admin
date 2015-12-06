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
public class UnSubscribeHandler implements WxMpMessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(UnSubscribeHandler.class);

    private WxMPConfig mpConfig;

    private WxHandlerService wxHandlerService;

    public UnSubscribeHandler(WxMPConfig mpConfig, WxHandlerService wxHandlerService) {
        this.mpConfig = mpConfig;
        this.wxHandlerService = wxHandlerService;
    }

    /**
     * Delete the user family if he un-subscribed.
     * @param wxMessage
     * @param context
     * @param wxMpService
     * @param sessionManager
     * @return
     * @throws WxErrorException
     */
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        String userOpenId = wxMessage.getFromUserName();
        String openId = wxMessage.getToUserName();
        wxHandlerService.unSubscribeUser(mpConfig.getId(), userOpenId);

        WxMpXmlOutMessage rtnMsg = WxMpXmlOutMessage.TEXT().fromUser(openId).toUser(userOpenId).content("").build();
        return rtnMsg;
    }

}
