package com.datatalk.xyztemp.weixin.service;


import javax.inject.Inject;

import com.datatalk.xyztemp.weixin.domain.WxServiceMenu;
import com.datatalk.xyztemp.weixin.repository.WxMPConfigRepository;
import com.datatalk.xyztemp.weixin.repository.WxMenuRepository;
import com.google.common.collect.Lists;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/5/20
 */
@Service
public class WxMenuService {

    private static final Logger LOG = LoggerFactory.getLogger(WxMenuService.class);

    @Inject
    public WxConfiguration wxConfiguration;
    @Inject
    private WxHandlerService wxHandlerService;
    @Inject
    private WxMenuRepository menuRepository;
    @Inject
    private WxMPConfigRepository mpConfigRepository;

    private String getBtnUrl(Long serviceId, String baseUrl, WxMpService wxMpService, String key) {
        String redirectUrl = baseUrl + "/wx/index/" + serviceId + "/" + key;
        String btnUrl = wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAUTH2_SCOPE_BASE, "1");
        return btnUrl;
    }

    @Transactional
    public void createMenu(Long serviceId) throws WxErrorException {
        WxMpService wxMpService = wxConfiguration.getWxService(serviceId);
        String baseUrl = wxHandlerService.getServiceUrl();

        WxMenu.WxMenuButton button1 = new WxMenu.WxMenuButton();
        button1.setName("公司");
        WxMenu.WxMenuButton subBtn1 = new WxMenu.WxMenuButton();
        subBtn1.setType(WxConsts.BUTTON_VIEW);
        subBtn1.setName("公司介绍");
        subBtn1.setUrl(getBtnUrl(serviceId, baseUrl, wxMpService, "COMPANY_INFO"));
        button1.setSubButtons(Lists.newArrayList(subBtn1));

        WxMenu.WxMenuButton button2 = new WxMenu.WxMenuButton();
        button2.setName("工作");
        WxMenu.WxMenuButton subBtn21 = new WxMenu.WxMenuButton();
        subBtn21.setType(WxConsts.BUTTON_VIEW);
        subBtn21.setName("我的客户");
        subBtn21.setUrl(getBtnUrl(serviceId, baseUrl, wxMpService, "JOB_MY_CUSTOMER"));
        button2.setSubButtons(Lists.newArrayList(subBtn21));

        WxMenu.WxMenuButton button3 = new WxMenu.WxMenuButton();
        button3.setName("关于");
        WxMenu.WxMenuButton subBtn31 = new WxMenu.WxMenuButton();
        subBtn31.setType(WxConsts.BUTTON_VIEW);
        subBtn31.setName("关于我们");
        subBtn31.setUrl(getBtnUrl(serviceId, baseUrl, wxMpService, "ABOUT_US"));
        button3.setSubButtons(Lists.newArrayList(subBtn31));

        WxMenu wxMenu = new WxMenu();
        wxMenu.setButtons(Lists.newArrayList(button1, button2, button3));

        wxMpService.menuCreate(wxMenu);
        createMenuUrl(serviceId);
    }

    private void createMenuUrl(Long serviceId) {
        // clear and recreate.
        menuRepository.deleteAllByServiceId(serviceId);

        List<WxServiceMenu> menus = Lists.newArrayList();
        WxServiceMenu menu = new WxServiceMenu();
        menu.setKeyCode("COMPANY_INFO");
        menu.setServiceId(serviceId);
        menu.setUrl("/app/info/company");
        menus.add(menu);

        menu = new WxServiceMenu();
        menu.setKeyCode("JOB_MY_CUSTOMER");
        menu.setServiceId(serviceId);
        menu.setUrl("/app/job/customer/");
        menus.add(menu);

        menu = new WxServiceMenu();
        menu.setKeyCode("ABOUT_US");
        menu.setServiceId(serviceId);
        menu.setUrl("/app/info/about/");
        menus.add(menu);

        menuRepository.save(menus);
    }

    public WxMenu getMenu(Long serviceId) throws WxErrorException {
        WxMpService wxMpService = wxConfiguration.getWxService(serviceId);
        return wxMpService.menuGet();
    }
}
