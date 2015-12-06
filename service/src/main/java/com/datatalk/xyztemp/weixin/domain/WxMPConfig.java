package com.datatalk.xyztemp.weixin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datatalk.xyztemp.domain.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.ZonedDateTime;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/12
 */
@Entity
@Table(name = "WX_MP_CONFIG")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WxMPConfig extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "app_id", nullable = false)
    private String appId;

    @Column(name = "app_secret", nullable = false)
    private String appSecret;

    @Column(name = "aes_key")
    private String aesKey;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "update_time")
    private ZonedDateTime updateTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "hello", nullable = false)
    private String hello;

    @Column(name = "use_open_id", nullable = false)
    private String useOpenId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "token_expires_time")
    private long tokenExpiresTime;

    @Column(name = "default_service")
    private boolean defaultService;

    @Column(name = "oauth2_redirect_uri")
    private String oauth2redirectUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public String getUseOpenId() {
        return useOpenId;
    }

    public void setUseOpenId(String useOpenId) {
        this.useOpenId = useOpenId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getTokenExpiresTime() {
        return tokenExpiresTime;
    }

    public void setTokenExpiresTime(long tokenExpiresTime) {
        this.tokenExpiresTime = tokenExpiresTime;
    }

    public boolean isDefaultService() {
        return defaultService;
    }

    public void setDefaultService(boolean defaultService) {
        this.defaultService = defaultService;
    }

    public String getOauth2redirectUri() {
        return oauth2redirectUri;
    }

    public void setOauth2redirectUri(String oauth2redirectUri) {
        this.oauth2redirectUri = oauth2redirectUri;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
