package com.datatalk.xyztemp.service.util;


import com.datatalk.xyztemp.web.rest.errors.BusinessException;

/**
 * Created by mavlarn on 15/11/18.
 */
public class ServiceConditionUtils {

    public static void checkState(boolean expression, String errorMessage) {
        if (!expression) {
            throw new BusinessException(errorMessage);
        }
    }
}
