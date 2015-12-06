package com.datatalk.xyztemp.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

/**
 * Created by mavlarn on 15/12/6.
 */
public class CustomUserDetails extends User {

    private static final long serialVersionUID = 1L;

    private final Long id;

    /**
     * @param id
     * @param username
     * @param password
     * @param authorities
     */
    public CustomUserDetails(Long id, String username, String password, Set<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public boolean isUserInRole(String authority) {
        return getAuthorities().contains(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" + id + ',' + getUsername() + ',' + getAuthorities() + '}';
    }
}
