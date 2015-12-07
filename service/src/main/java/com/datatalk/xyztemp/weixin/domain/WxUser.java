package com.datatalk.xyztemp.weixin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.datatalk.xyztemp.domain.BaseEntity;
import com.datatalk.xyztemp.domain.type.SexType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.ZonedDateTime;

/**
 * Weixin user. Used for weixin user statistic.
 * Author: mavlarn
 * Date: 15/6/12
 */
@Entity
@Table(name = "WX_USER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WxUser extends BaseEntity {

    @NotNull
    @Column(name = "open_id", nullable = false)
    private String openId;

    // NOT USED NOW. Just use useServiceId.
    @Column(name = "use_open_id")
    private String useOpenId;

    @NotNull
    @Column(name = "use_service_id", nullable = false)
    private Long useServiceId;

    @NotNull
    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @NotNull
    @Column(name = "subscribe_date", nullable = false)
    private ZonedDateTime subscribeDate;

    @NotNull
    @Column(name = "bind_date", nullable = false)
    private ZonedDateTime bindDate;

    @Column(name = "country", length = 20)
    private String country;

    @Column(name = "province", length = 20)
    private String province;

    @Column(name = "city", length = 20)
    private String city;

    @Column(name = "sex", length = 10)
    @Enumerated(EnumType.STRING)
    private SexType sex;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUseOpenId() {
        return useOpenId;
    }

    public void setUseOpenId(String useOpenId) {
        this.useOpenId = useOpenId;
    }

    public Long getUseServiceId() {
        return useServiceId;
    }

    public void setUseServiceId(Long useServiceId) {
        this.useServiceId = useServiceId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public ZonedDateTime getSubscribeDate() {
        return subscribeDate;
    }

    public void setSubscribeDate(ZonedDateTime subscribeDate) {
        this.subscribeDate = subscribeDate;
    }

    public ZonedDateTime getBindDate() {
        return bindDate;
    }

    public void setBindDate(ZonedDateTime bindDate) {
        this.bindDate = bindDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public SexType getSex() {
        return sex;
    }

    public void setSex(SexType sex) {
        this.sex = sex;
    }
}
