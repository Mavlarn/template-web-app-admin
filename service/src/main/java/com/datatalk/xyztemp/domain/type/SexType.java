package com.datatalk.xyztemp.domain.type;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/5/27
 */
public enum SexType {
    MALE(1, "男"),
    FEMALE(2, "女"),
    UNKNOWN(3, "未知");

    private int code;
    private String value;

    SexType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
