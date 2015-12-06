package com.datatalk.xyztemp.weixin.handler;

import java.util.Map;

import com.datatalk.xyztemp.domain.User;
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


public class MyVideoHandler implements WxMpMessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MyVideoHandler.class);

    private WxMPConfig mpConfig;
    private WxHandlerService wxHandlerService;

    public MyVideoHandler(WxMPConfig mpConfig, WxHandlerService wxHandlerService) {
        this.mpConfig = mpConfig;
        this.wxHandlerService = wxHandlerService;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        String userOpenId = wxMessage.getFromUserName();
        String openId = wxMessage.getToUserName();

        User theUser = wxHandlerService.getUserWithAuth(mpConfig.getId(), userOpenId);
        if (theUser == null) {
            String content = "亲, 请先绑定再操作!";
            return WxMpXmlOutMessage.TEXT().fromUser(openId).toUser(userOpenId).content(content).build();
        }

        String content = "not finished.";

        WxMpXmlOutMessage rtnMsg = WxMpXmlOutMessage.TEXT().fromUser(openId).toUser(userOpenId)
        		.content(content)
        		.build();
        return rtnMsg;
    }

}
