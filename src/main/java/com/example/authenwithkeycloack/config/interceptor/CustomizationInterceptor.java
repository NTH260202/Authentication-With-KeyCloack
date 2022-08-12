package com.example.authenwithkeycloack.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CustomizationInterceptor implements HandlerInterceptor {
    @Value(value = "${api-key}")
    private String apiKey;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getServletPath().equals("/api/users/login")) {
            return true;
        }
        String requestApiKey = request.getHeader("Api-Key");
        log.info("Api-key from service" + apiKey);
        return requestApiKey != null && requestApiKey.equals(apiKey);
    }
}
