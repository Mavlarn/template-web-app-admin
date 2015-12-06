package com.datatalk.xyztemp.web.rest;

import com.datatalk.xyztemp.service.SMSService;
import com.datatalk.xyztemp.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/23
 */
@RestController
@RequestMapping("/api")
public class SysConfigResource {

    private final Logger log = LoggerFactory.getLogger(SysConfigResource.class);

    @Inject
    private SMSService smsService;

    /**
     * GET  /users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/sendConfig/sms",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public void setSMSConfig(@RequestParam Boolean isSend) {
        log.debug("REST request to set sms config : {}", isSend);
        smsService.setIsSend(isSend);
    }

    @RequestMapping(value = "/sendConfig/sms",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public Boolean getSMSConfig() {
        log.debug("REST request to get sms config.");
        return smsService.isSend();
    }

}
