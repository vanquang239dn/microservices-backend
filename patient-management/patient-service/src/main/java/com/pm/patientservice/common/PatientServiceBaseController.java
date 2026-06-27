package com.pm.patientservice.common;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pm.patientservice.dto.response.ApiResponse;

public abstract class PatientServiceBaseController {

    protected <T> ApiResponse<T> createSuccessResponse(T result) {

        return ApiResponse.success(getRequestUri(), result);
    }

    protected <T> ApiResponse<T> createErrorResponse(int errorStatus, String message, T result) {

        return ApiResponse.error(errorStatus, message, getRequestUri(), result);
    }

    private String getRequestUri() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return null;
        }

        return attributes.getRequest().getRequestURI();
    }

}
