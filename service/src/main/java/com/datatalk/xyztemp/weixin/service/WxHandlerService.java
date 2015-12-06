package com.datatalk.xyztemp.weixin.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import com.datatalk.xyztemp.config.Constants;
import com.datatalk.xyztemp.domain.Authority;
import com.datatalk.xyztemp.domain.User;
import com.datatalk.xyztemp.repository.UserRepository;
import com.datatalk.xyztemp.security.xauth.Token;
import com.datatalk.xyztemp.security.xauth.TokenProvider;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/7/16
 */
@Service
public class WxHandlerService {

    @Inject
    private WxUserService wxUserService;
    @Inject
    private UserRepository userRepository;
    @Inject
    private Environment environment;
    @Inject
    private TokenProvider tokenProvider;

    private String serverUrl = null;
    private String serviceUrl = null;

    // server url for weixin.
    public String getServerUrl() {
        if (serverUrl != null) {
            return serverUrl;
        }
        if (environment.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            serverUrl = "http://app.data-talks.org";
        } else {
            serverUrl = "http://app.data-talks.org";
        }
        return serverUrl;
    }

    // back service url
    public String getServiceUrl() {
        if (serviceUrl != null) {
            return serviceUrl;
        }
        if (environment.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            serviceUrl = "http://service.data-talks.org";
        } else {
            serviceUrl = "http://service.data-talks.org";
        }
        return serviceUrl;
    }


    public void subscribeWxUser(Long serviceId, String userOpenId) throws WxErrorException {
        wxUserService.subscribeWxUser(serviceId, userOpenId);
    }

    public void unSubscribeUser(Long serviceId, String userOpenId) {
        wxUserService.unSubscribeUser(serviceId, userOpenId);
    }

    public User getUser(Long serviceId, String userOpenId) {
        return userRepository.findOneByUseServiceIdAndOpenId(serviceId, userOpenId).get();
    }

    @Transactional(readOnly = true)
    public User getUserWithAuth(Long serviceId, String userOpenId) {
        return userRepository.findOneByUseServiceIdAndOpenId(serviceId, userOpenId).map(user -> {
            user.getAuthorities().size();
            return user;
        }).orElse(null);
    }

    @Transactional(readOnly = true)
    public String getUserToken(User theUser) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority : theUser.getAuthorities()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
            grantedAuthorities.add(grantedAuthority);
        }
        UserDetails details = new org.springframework.security.core.userdetails.User(theUser.getLogin(),
                theUser.getPassword(), grantedAuthorities);
        Token token = tokenProvider.createToken(details);
        return token.getToken();
    }

}
