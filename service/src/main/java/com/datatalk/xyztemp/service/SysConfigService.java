package com.datatalk.xyztemp.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/8/31
 */
@Component
public class SysConfigService {

    private boolean isWxAppDebug = false;

    private String storageServer;

    @Inject
    private Environment environment;

    public boolean isWxAppDebug() {
        return isWxAppDebug;
    }

    public void setIsWxAppDebug(boolean isWxAppDebug) {
        this.isWxAppDebug = isWxAppDebug;
    }

    public String getStorageServer() {
        return environment.getProperty("app.storage.serverUrl");
    }

    public void setStorageServer(String storageServer) {
        this.storageServer = storageServer;
    }
}
