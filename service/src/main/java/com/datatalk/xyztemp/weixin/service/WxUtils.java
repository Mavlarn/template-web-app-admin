package com.datatalk.xyztemp.weixin.service;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/18
 */
public class WxUtils {

    public static final String getWxUserLogin(Long serviceId, String wxUserOpenId) {
        return serviceId + "_" + wxUserOpenId;
    }
}
