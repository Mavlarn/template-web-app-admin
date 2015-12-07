package com.datatalk.xyztemp.web.rest.errors;

/**
 * Created by mavlarn on 15/11/17.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
