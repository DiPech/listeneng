package ru.dipech.listeneng.controller;

import ru.dipech.listeneng.entity.api.ApiResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    public static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(path = ErrorController.ERROR_PATH)
    public ApiResponse<Object> error(HttpServletRequest request) {
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (throwable != null) {
            return ApiResponse.error(ExceptionUtils.getRootCauseMessage(throwable));
        }
        HttpStatus httpStatus = getHttpStatus(request);
        if (httpStatus == HttpStatus.NO_CONTENT) {
            return ApiResponse.ok();
        }
        return ApiResponse.error(httpStatus);
    }

    protected HttpStatus getHttpStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
