package com.datatalk.xyztemp.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/13
 */
public class WeixinUsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;

    public WeixinUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
