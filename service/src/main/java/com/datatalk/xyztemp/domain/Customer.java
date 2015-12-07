package com.datatalk.xyztemp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer extends BaseEntity {

    @Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
    @Size(min = 1, max = 20)
    @Column(length = 20, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @Size(min = 60, max = 60)
    @Column(name = "password_hash",length = 60)
    private String password;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "real_name", length = 20)
    private String realName;

    @Column(name = "sex")
    private Integer sex; // 1 is male, 2 is female

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "mobile", length = 15)
    private String mobile;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(nullable = false)
    private boolean activated = false;

    @Column(nullable = false)
    private boolean deleted = false;

    @Size(max = 10)
    @Column(name = "activation_key", length = 10)
    @JsonIgnore
    private String activationKey;

    @Size(max = 10)
    @Column(name = "dynamic_key", length = 10)
    private String dynamicKey;

    @Column(name = "dynamic_key_date")
    private ZonedDateTime dynamicKeyDate;

    @Column(name = "head_img_url", length = 255)
    @JsonIgnore
    private String headImgUrl;

    @Column(name = "open_id", length = 50)
    @JsonIgnore
    private String openId;

    @Column(name = "use_service_id")
    @JsonIgnore
    private Long useServiceId;

    @Column(name = "bind_date")
    @JsonIgnore
    private ZonedDateTime bindDate;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getDynamicKey() {
        return dynamicKey;
    }

    public void setDynamicKey(String dynamicKey) {
        this.dynamicKey = dynamicKey;
    }

    public ZonedDateTime getDynamicKeyDate() {
        return dynamicKeyDate;
    }

    public void setDynamicKeyDate(ZonedDateTime dynamicKeyDate) {
        this.dynamicKeyDate = dynamicKeyDate;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getUseServiceId() {
        return useServiceId;
    }

    public void setUseServiceId(Long useServiceId) {
        this.useServiceId = useServiceId;
    }

    public ZonedDateTime getBindDate() {
        return bindDate;
    }

    public void setBindDate(ZonedDateTime bindDate) {
        this.bindDate = bindDate;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", login='" + login + "'" +
                ", type='" + type + "'" +
                ", name='" + name + "'" +
                ", mobile='" + mobile + "'" +
                ", description='" + description + "'" +
                ", activated='" + activated + "'" +
                '}';
    }
}
