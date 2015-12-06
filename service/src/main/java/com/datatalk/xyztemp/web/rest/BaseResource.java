package com.datatalk.xyztemp.web.rest;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/11
 */
public abstract class BaseResource {

    protected static final String SUCCESS = "success";
    protected static final String MESSAGE = "message";
    protected static final String DATA = "data";

    public Map errorMap(String msg) {
        Map rtnMap = Maps.newHashMap();
        rtnMap.put(SUCCESS, false);
        rtnMap.put(MESSAGE, msg);
        return rtnMap;
    }

    public Map successMap(Object data) {
        Map rtnMap = Maps.newHashMap();
        rtnMap.put(SUCCESS, true);
        rtnMap.put(DATA, data);
        return rtnMap;
    }

}
