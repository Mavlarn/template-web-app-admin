package com.datatalk.xyztemp.weixin.service;

import java.util.Map;

import javax.inject.Inject;

import com.datatalk.xyztemp.weixin.domain.WxMPConfig;
import com.datatalk.xyztemp.weixin.handler.*;
import com.datatalk.xyztemp.weixin.repository.WxMPConfigRepository;
import com.google.common.collect.Maps;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/5/20
 */
@Component
public class WxConfiguration {

    private Map<Long, WxMpService> wxMpServiceMap = Maps.newHashMap();
    private Map<Long, WxMpMessageRouter> wxMpMessageRouterMap = Maps.newHashMap();
    private Map<Long, WxMpDBConfigStorage> wxMpConfigStorageMap = Maps.newHashMap();

    @Inject
    private WxMPConfigRepository mpConfigRepository;
    @Inject
    @Lazy(value=true)
    private WxHandlerService wxHandlerService;

    public WxMpService getWxService(Long serviceId) {
        WxMpService wxMpService = wxMpServiceMap.get(serviceId);
        if (wxMpService == null) {
            WxMPConfig mpConfig = mpConfigRepository.findOne(serviceId);
            wxMpService = initMpService(mpConfig);
        }
        return wxMpService;
    }

    public WxMpService getDefaultWxService() {
        WxMPConfig defaultMP = mpConfigRepository.findOneByDefaultServiceTrue();
        WxMpService wxMpService = wxMpServiceMap.get(defaultMP.getId());
        if (wxMpService == null) {
            wxMpService = initMpService(defaultMP);
        }
        return wxMpService;
    }

    public WxMpMessageRouter getWxMpMessageRouter(Long serviceId) {
        WxMpMessageRouter theRouter = wxMpMessageRouterMap.get(serviceId);
        if (theRouter == null) {
            WxMPConfig theConfig = mpConfigRepository.findOne(serviceId);
//            WxMPConfig theConfig = mpConfigRepository.findOneByDefaultServiceTrue();
            initMpService(theConfig);
        }
        return wxMpMessageRouterMap.get(serviceId);
    }

    public WxMpConfigStorage getWxMpConfigStorage(Long serviceId) {
        WxMpConfigStorage theConfig = wxMpConfigStorageMap.get(serviceId);
        if (theConfig == null) {
            WxMPConfig mpConfig = mpConfigRepository.findOne(serviceId);
            initMpService(mpConfig);
        }
        return wxMpConfigStorageMap.get(serviceId);
    }

    private WxMpService initMpService(WxMPConfig wxMPConfig) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDBConfigStorage configStorage = new WxMpDBConfigStorage();
        configStorage.setServiceId(wxMPConfig.getId());
        configStorage.setConfigRepository(mpConfigRepository);
        wxMpService.setWxMpConfigStorage(configStorage);
        wxMpConfigStorageMap.put(wxMPConfig.getId(), configStorage);
        wxMpServiceMap.put(wxMPConfig.getId(), wxMpService);
        initMessageRouter(wxMPConfig, wxMpService);
        return wxMpService;
    }

    public static final String XML_MSG_SHORT_VIDEO = "shortvideo";

    private WxMpMessageRouter initMessageRouter(WxMPConfig wxMPConfig, WxMpService wxMpService) {

        WxMpMessageHandler handler = new WxMpMessageHandler() {
            @Override
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
                WxMpXmlOutTextMessage m
                        = WxMpXmlOutMessage.TEXT().content("测试加密消息").fromUser(wxMessage.getToUserName())
                        .toUser(wxMessage.getFromUserName()).build();
                return m;
            }
        };

        SubscribeHandler subscribeHandler = new SubscribeHandler(wxMPConfig, wxHandlerService);
        UnSubscribeHandler unSubscribeHandler = new UnSubscribeHandler(wxMPConfig, wxHandlerService);
        MyImageHandler imageHandler = new MyImageHandler(wxMPConfig, wxHandlerService);
        MyVideoHandler videoHandler = new MyVideoHandler(wxMPConfig, wxHandlerService);
        MyVoiceHandler voiceHandler = new MyVoiceHandler(wxMPConfig, wxHandlerService);

        WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
        wxMpMessageRouter
                .rule()// 拦截内容为“哈哈”的消息
                .async(false).content("哈哈").handler(handler).end()
                .rule().async(false).event(WxConsts.EVT_SUBSCRIBE).handler(subscribeHandler).end()
                .rule().event(WxConsts.EVT_UNSUBSCRIBE).handler(unSubscribeHandler).end()
                .rule().async(false).msgType(WxConsts.XML_MSG_IMAGE).handler(imageHandler).end()
                .rule().async(false).msgType(WxConsts.XML_MSG_VIDEO).handler(videoHandler).end()
                .rule().async(false).msgType(XML_MSG_SHORT_VIDEO).handler(videoHandler).end()
                .rule().async(false).msgType(WxConsts.XML_MSG_VOICE).handler(voiceHandler).end();

        wxMpMessageRouterMap.put(wxMPConfig.getId(), wxMpMessageRouter);
        return wxMpMessageRouter;
    }
}
