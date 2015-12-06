package com.datatalk.xyztemp.exception;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by mavlarn on 15/11/17.
 */
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * This is just business error, not the print this error in log.
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessError(HttpServletRequest request, Exception ex) {
        return handleError(request, ex, "Business Error");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(HttpServletRequest request, Exception ex) {
        LOG.error("Exception:" + ex.getMessage(), ex);
        return handleError(request, ex, "Internal Server Error");
    }

    private ResponseEntity handleError(HttpServletRequest request, Exception ex, String value) {
        Map<String, String> rtnMap = Maps.newHashMap();
        rtnMap.put("error", value);
        rtnMap.put("exception", ex.getClass().getName());
        rtnMap.put("message", ex.getMessage());
        if (ex.getCause() != null) {
            rtnMap.put("cause", ex.getCause().toString());
        }
        rtnMap.put("path", request.getServletPath());
        rtnMap.put("status", "500");
        return ResponseEntity.status(500).body(rtnMap);
    }
}
