package com.github.ezequielarroyo.postservice.config.logs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTR = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_ATTR, startTime);

        log.info("--> HTTP INICIADO [{} {}]", request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, @NonNull Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute(START_TIME_ATTR);
        long duration = System.currentTimeMillis() - startTime;
        int status = response.getStatus();

        //defining the log level
        if (status >= 500) {
            log.error("<-- HTTP FINALIZADO [{} {}] - Estado: {} - Tiempo: {}ms", request.getMethod(), request.getRequestURI(), status, duration);
        } else if (status >= 400) {
            log.warn("<-- HTTP FINALIZADO [{} {}] - Estado: {} - Tiempo: {}ms", request.getMethod(), request.getRequestURI(), status, duration);
        } else {
            log.info("<-- HTTP FINALIZADO [{} {}] - Estado: {} - Tiempo: {}ms", request.getMethod(), request.getRequestURI(), status, duration);
        }
    }
}
