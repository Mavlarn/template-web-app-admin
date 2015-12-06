package com.datatalk.xyztemp.weixin.web.rest;

import me.chanjar.weixin.common.bean.WxJsapiSignature;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/7/10
 */
public class WxJsapiSignatureDTO {

    private String appId;

    private String noncestr;
    private String jsapiTicket;
    private long timestamp;
    private String url;
    private String signature;

    public WxJsapiSignatureDTO(WxJsapiSignature wxJsapiSignature) {
        this.noncestr = wxJsapiSignature.getNoncestr();
//        this.jsapiTicket = wxJsapiSignature.getJsapiTicket();
        this.timestamp = wxJsapiSignature.getTimestamp();
        this.url = wxJsapiSignature.getUrl();
        this.signature = wxJsapiSignature.getSignature();
        this.appId = wxJsapiSignature.getAppid();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getJsapiTicket() {
        return jsapiTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
