package com.datatalk.xyztemp.weixin.web.rest;

import javax.validation.constraints.NotNull;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/17
 */
public class ActivateDTO {

    @NotNull
    private String openId;

    @NotNull
    private Long serviceId;

    @NotNull
    private String activationKey;

    @NotNull
    private String realName;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "ActivateDTO{" +
                "openId=" + openId +
                ", serviceId='" + serviceId + "'" +
                ", activationKey='" + activationKey + "'" +
                ", realName='" + realName + "'" +
                '}';
    }
}
