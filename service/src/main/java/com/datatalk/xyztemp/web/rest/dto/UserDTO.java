package com.datatalk.xyztemp.web.rest.dto;

import com.datatalk.xyztemp.domain.Authority;
import com.datatalk.xyztemp.domain.User;

import com.datatalk.xyztemp.domain.type.SexType;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private Long id;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 20)
    private String login;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = 20)
    private String name;

    @Size(max = 20)
    private String realName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private String mobile;

    private SexType sex;

    private String headImgUrl;

    private String address;

    private Set<String> authorities;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.name = user.getName();
        this.realName = user.getRealName();
        this.email = user.getEmail();
        this.mobile = user.getMobile();
        this.sex = user.getSex();
        this.headImgUrl = user.getHeadImgUrl();
        this.address = user.getAddress();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
    }
    public User toUser() {
        User newUser = new User();
        newUser.setId(this.id);
        newUser.setLogin(this.getLogin());
        newUser.setPassword(this.getPassword());
        newUser.setName(this.getName());
        newUser.setRealName(this.getRealName());
        newUser.setEmail(this.getEmail());
        newUser.setMobile(this.getMobile());
        newUser.setSex(this.getSex());
        newUser.setAddress(this.address);
        // set authorities outside.
        return newUser;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getRealName() {
        return realName;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public SexType getSex() {
        return sex;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public String getAddress() {
        return address;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", password='" + password + '\'' +
            ", name='" + name + '\'' +
            ", realName='" + realName + '\'' +
            ", mobile='" + mobile + '\'' +
            ", email='" + email + '\'' +
            ", authorities=" + authorities +
            "}";
    }
}
