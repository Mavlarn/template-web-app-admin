package com.datatalk.xyztemp.weixin.web.rest;

import javax.inject.Inject;

import com.codahale.metrics.annotation.Timed;
import com.datatalk.xyztemp.web.rest.BaseResource;
import com.datatalk.xyztemp.weixin.service.WxMenuService;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Used to manage menu in weixin custom menus.
 * Author: mavlarn
 * Date: 15/6/16
 */
@RestController
@RequestMapping("/api/wx")
public class WxMenuResource extends BaseResource {

    @Inject
    private WxMenuService menuService;

    @RequestMapping(value = "/menus/{serviceId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity createMenu(@PathVariable Long serviceId) {
        try {
            menuService.createMenu(serviceId);
        } catch (WxErrorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap("创建菜单失败:" + e.getMessage()));
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/menus/{serviceId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public WxMenu getServiceMenu(@PathVariable Long serviceId) throws WxErrorException {
        return menuService.getMenu(serviceId);
    }
}
