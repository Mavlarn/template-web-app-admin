package com.datatalk.xyztemp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractAuditingEntity {

    @NotNull
    @Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
    @Size(min = 1, max = 20)
    @Column(length = 20, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash",length = 60)
    private String password;

    @Size(max = 20)
    @Column(name = "name", length = 20)
    private String name;

    @Size(max = 20)
    @Column(name = "real_name", length = 20)
    private String realName;

    @Email
    @Size(max = 100)
    @Column(length = 100, unique = true)
    private String email;

    @Size(max = 15)
    @Column(length = 15, unique = true)
    private String mobile;

    @Column(name = "sex")
    private Integer sex; // 1 is male, 2 is female

    @Column(nullable = false)
    private boolean activated = false;

    @Column(nullable = false)
    private boolean deleted = false;

    @Size(min = 2, max = 5)
    @Column(name = "lang_key", length = 5)
    private String langKey;

    @Size(max = 20)
    @Column(name = "activation_key", length = 10)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "dynamic_key", length = 10)
    private String dynamicKey;

    @Column(name = "dynamic_key_date")
    private ZonedDateTime dynamicKeyDate;

    @Column(name = "head_img_url", length = 255)
    private String headImgUrl;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Authority> authorities = new HashSet<>();

    @Column(name = "open_id", length = 50)
    private String openId;

    // this is the id of WxMPConfig of the MP.
    @Column(name = "use_service_id")
    private Long useServiceId;

    @Column(name = "province", length = 20)
    private String province;

    @Column(name = "city", length = 20)
    private String city;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "bind_date")
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean getActivated() {
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
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

    public ZonedDateTime getBindDate() {
        return bindDate;
    }

    public void setBindDate(ZonedDateTime bindDate) {
        this.bindDate = bindDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id + '\''+
            ", login='" + login + '\'' +
            ", name='" + name + '\'' +
            ", realName='" + realName + '\'' +
            ", email='" + email + '\'' +
            ", activated='" + activated + '\'' +
            ", mobile='" + mobile + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }
}
