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


public class MyImageHandler implements WxMpMessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MyImageHandler.class);

    private WxMPConfig mpConfig;
    private WxHandlerService wxHandlerService;

    public MyImageHandler(WxMPConfig mpConfig, com.datatalk.xyztemp.weixin.service.WxHandlerService wxHandlerService) {
        this.mpConfig = mpConfig;
        this.wxHandlerService = wxHandlerService;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        String userOpenId = wxMessage.getFromUserName();
        String openId = wxMessage.getToUserName();

        User theUser = wxHandlerService.getUser(mpConfig.getId(), userOpenId);
        String content;
        if (theUser == null) {
            content = "亲, 请先绑定再操作!";
        } else {
            content = "亲，请通过微信菜单中“互动”-“发布”按钮发布图片或视频，谢谢!";
        }
        WxMpXmlOutMessage rtnMsg = WxMpXmlOutMessage.TEXT().fromUser(openId).toUser(userOpenId)
                .content(content)
                .build();
        return rtnMsg;
    }
}
