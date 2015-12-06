package com.datatalk.xyztemp.weixin.service;

import java.io.File;

import com.datatalk.xyztemp.weixin.domain.WxMPConfig;
import com.datatalk.xyztemp.weixin.repository.WxMPConfigRepository;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;

import javax.net.ssl.SSLContext;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/12
 */
public class WxMpDBConfigStorage implements WxMpConfigStorage {

    private WxMPConfigRepository configRepository;

    private Long serviceId;
    protected volatile String accessToken;
    protected volatile long expiresTime;

    protected volatile String oauth2redirectUri;

    protected volatile String jsapiTicket;
    protected volatile long jsapiTicketExpiresTime;

    public void setConfigRepository(WxMPConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public long getExpiresTime() {
        return expiresTime;
    }

    @Override
    public boolean isAccessTokenExpired() {
        return accessToken == null || System.currentTimeMillis() > expiresTime;
    }

    @Override
    public void expireAccessToken() {
        expiresTime = 0;
    }

    @Override
    public void updateAccessToken(WxAccessToken accessToken) {
        updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
    }

    @Override
    public void updateAccessToken(String accessToken, int expiresInSeconds) {
        this.accessToken = accessToken;
        this.expiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l;

        WxMPConfig mpConfig = configRepository.findOne(serviceId);
        mpConfig.setAccessToken(accessToken);
        mpConfig.setTokenExpiresTime(expiresTime);
        configRepository.save(mpConfig);
    }

    @Override
    public String getAppId() {
        WxMPConfig mpConfig = configRepository.findOne(serviceId);
        return mpConfig.getAppId();
    }

    @Override
    public String getSecret() {
        WxMPConfig mpConfig = configRepository.findOne(serviceId);
        return mpConfig.getAppSecret();
    }

    @Override
    public String getPartnerId() {
        return null;
    }

    @Override
    public String getPartnerKey() {
        return null;
    }

    @Override
    public String getToken() {
        WxMPConfig mpConfig = configRepository.findOne(serviceId);
        return mpConfig.getToken();
    }

    @Override
    public String getAesKey() {
        WxMPConfig mpConfig = configRepository.findOne(serviceId);
        return mpConfig.getAesKey();
    }
    public String getJsapiTicket() {
        return jsapiTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public long getJsapiTicketExpiresTime() {
        return jsapiTicketExpiresTime;
    }

    public void setJsapiTicketExpiresTime(long jsapiTicketExpiresTime) {
        this.jsapiTicketExpiresTime = jsapiTicketExpiresTime;
    }

    public boolean isJsapiTicketExpired() {
        return System.currentTimeMillis() > this.jsapiTicketExpiresTime;
    }

    public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
        this.jsapiTicket = jsapiTicket;
        // 预留200秒的时间
        this.jsapiTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l;
    }

    public void expireJsapiTicket() {
        this.jsapiTicketExpiresTime = 0;
    }

    @Override
    public String getOauth2redirectUri() {
        WxMPConfig mpConfig = configRepository.findOne(serviceId);
        return mpConfig.getOauth2redirectUri();
    }

    public void setOauth2redirectUri(String oauth2redirectUri) {
        this.oauth2redirectUri = oauth2redirectUri;
    }

    @Override
    public String getHttp_proxy_host() {
        return null;
    }

    @Override
    public int getHttp_proxy_port() {
        return 0;
    }

    @Override
    public String getHttp_proxy_username() {
        return null;
    }

    @Override
    public String getHttp_proxy_password() {
        return null;
    }

    @Override
    public File getTmpDirFile() {
        return null;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public SSLContext getSSLContext() {
        return null;
    }
}
