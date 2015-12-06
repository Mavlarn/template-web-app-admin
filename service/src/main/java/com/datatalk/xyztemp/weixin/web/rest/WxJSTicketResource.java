package com.datatalk.xyztemp.weixin.web.rest;

import java.util.Map;

import javax.inject.Inject;

import com.codahale.metrics.annotation.Timed;
import com.datatalk.xyztemp.weixin.domain.WxMPConfig;
import com.datatalk.xyztemp.weixin.repository.WxMPConfigRepository;
import com.datatalk.xyztemp.weixin.service.WxConfiguration;
import com.google.common.collect.Maps;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/7/8
 */
@RestController
@RequestMapping("/api/wx")
public class WxJSTicketResource {

    private static final Logger LOG = LoggerFactory.getLogger(WxJSTicketResource.class);

    @Inject
    private WxConfiguration wxConfiguration;
    @Inject
    private WxMPConfigRepository configRepository;

    @RequestMapping(value = "/jsticket/{serviceId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Map getJSTicket(@PathVariable Long serviceId, @RequestParam String url) {
        WxMpService wxMpService = wxConfiguration.getWxService(serviceId);
        WxMPConfig mpConfig = configRepository.findOne(serviceId);
        try {
            WxJsapiSignature signature = wxMpService.createJsapiSignature(url);
            WxJsapiSignatureDTO signatureDTO = new WxJsapiSignatureDTO(signature);
            signatureDTO.setAppId(mpConfig.getAppId());
            return getResult(true, null, signatureDTO);
        } catch (WxErrorException e) {
            LOG.error(e.getMessage(), e);
            return getResult(false, e.getMessage(), null);
        }
    }

    private Map getResult(boolean success, String msg, Object data) {
        Map rtnMap = Maps.newHashMap();
        rtnMap.put("message", msg);
        rtnMap.put("data", data);
        rtnMap.put("success", success);
        return rtnMap;
    }
}
