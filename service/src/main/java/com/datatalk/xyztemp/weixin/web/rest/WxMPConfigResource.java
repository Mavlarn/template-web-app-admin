package com.datatalk.xyztemp.weixin.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import com.codahale.metrics.annotation.Timed;
import com.datatalk.xyztemp.weixin.domain.WxMPConfig;
import com.datatalk.xyztemp.weixin.repository.WxMPConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/16
 */
@RestController
@RequestMapping("/api/wx")
public class WxMPConfigResource {
    private final Logger log = LoggerFactory.getLogger(WxMPConfigResource.class);

    @Inject
    private WxMPConfigRepository wxMPConfigRepository;


    /**
     * POST /wxMPConfig ->Create a new wxMPConfig
     */
    @RequestMapping(value = "/wxMPConfig",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity create(@Valid @RequestBody WxMPConfig wxMPConfig) throws URISyntaxException {
        log.debug("REST request to update wxMPConfig : {}", wxMPConfig);
        wxMPConfigRepository.save(wxMPConfig);
        return ResponseEntity.ok(wxMPConfig);
    }

    /**
     * PUT  /wxMPConfig -> Updates an existing wxMPConfig.
     */
    @RequestMapping(value = "/wxMPConfig",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody WxMPConfig wxMPConfig) throws URISyntaxException {
        log.debug("REST request to update wxServiceMenu : {}", wxMPConfig);
        wxMPConfigRepository.save(wxMPConfig);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /wxMPConfig -> get one wxMPConfig
     */
    @RequestMapping(value = "/wxMPConfig/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public WxMPConfig get(@RequestParam Long id){
        log.debug("REST request to get one wxMPConfig:{}", id);
        return wxMPConfigRepository.findOne(id);
    }

    /**
     * GET  /wxMPConfig -> search from all the wxMPConfig
     */
    @RequestMapping(value = "/wxMPConfig",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WxMPConfig> getAll(){
        log.debug("REST request to get all wxMPConfig.");
        return wxMPConfigRepository.findAll();
    }

    /**
     * DELETE  /wxMPConfig ->delete the "id" wxMPConfig
     */
    @RequestMapping(value = "/wxMPConfig/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete WxMPMessageTemplate : {}", id);
        wxMPConfigRepository.delete(id);
    }
}
