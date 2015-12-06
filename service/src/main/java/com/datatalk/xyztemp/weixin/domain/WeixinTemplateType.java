package com.datatalk.xyztemp.weixin.domain;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/12
 */
public enum WeixinTemplateType {

    MESSAGE_NOTIFY("留言提醒"),
    MESSAGE_REPLY("回复提醒"),
    CLASS_NOTIFICATION("通知");

    private String value;

    WeixinTemplateType(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

}
