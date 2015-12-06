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
 * @author 张恩科
 * @version V1.0
 * @ClassName: MyImageHandler.java
 * @Description: TODO
 * @Date 2015年6月18日 下午2:50:10
 */
public class MyVoiceHandler implements WxMpMessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MyVoiceHandler.class);

    private WxMPConfig mpConfig;
    private WxHandlerService wxHandlerService;

    public MyVoiceHandler(WxMPConfig mpConfig, WxHandlerService wxHandlerService) {
        this.mpConfig = mpConfig;
        this.wxHandlerService = wxHandlerService;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        String userOpenId = wxMessage.getFromUserName();
        String openId = wxMessage.getToUserName();
        //处理图片上传

        String content = "亲，目前还不支持该功能!";
        WxMpXmlOutMessage rtnMsg = WxMpXmlOutMessage.TEXT().fromUser(openId).toUser(userOpenId)
                .content(content)
                .build();
        return rtnMsg;
    }

}
