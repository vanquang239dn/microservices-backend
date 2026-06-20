package com.pm.patientservice.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "PATIENT-CONTROLLER")
@Aspect
@Component
public class ControllerLoggingAspect {

    // Apply to all the method have @RestController
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerMethods() {
    }

    @Around("restControllerMethods()")
    public Object logControllerApi(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        HttpServletRequest request = getCurrentHttpRequest();

        String httpMethod = request != null ? request.getMethod() : "UNKNOWN";
        String requestUri = request != null ? request.getRequestURI() : "UNKNOWN";

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String className = AopUtils.getTargetClass(joinPoint.getTarget()).getSimpleName();
        String methodName = signature.getName();

        String operationSummary = getOperationSummary(method);

        log.info("API START | {} {} | {}.{} | summary={}",
                httpMethod,
                requestUri,
                className,
                methodName,
                operationSummary);

        try {
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - startTime;
            int statusCode = getStatusCode(result);

            log.info("API END   | {} {} | {}.{} | status={} | duration={}ms | summary={}",
                    httpMethod,
                    requestUri,
                    className,
                    methodName,
                    statusCode,
                    duration,
                    operationSummary);

            return result;

        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;

            log.error("API ERROR | {} {} | {}.{} | duration={}ms | error={}",
                    httpMethod,
                    requestUri,
                    className,
                    methodName,
                    duration,
                    ex.getMessage(),
                    ex);

            throw ex;
        }
    }

    // Get current http request
    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return null;
        }

        return attributes.getRequest();
    }

    // Get operation summary
    private String getOperationSummary(Method method) {
        Operation operation = method.getAnnotation(Operation.class);

        if (operation == null || operation.summary().isBlank()) {
            return method.getName();
        }

        return operation.summary();
    }

    // Get http response status code
    private int getStatusCode(Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            return responseEntity.getStatusCode().value();
        }

        return 200;
    }

}
