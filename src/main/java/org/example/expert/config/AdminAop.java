package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class AdminAop {

    private final ObjectMapper objectMapper;

    @Around("execution(* org.example.expert.domain.user.controller.UserAdminController.*(..)) || " +
            "execution(* org.example.expert.domain.comment.controller.CommentAdminController.*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        log.info("this is aop");
        String requestURI = request.getRequestURI();
        Long requestUserId = (Long) request.getAttribute("userId");
        String requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());

        Object result =  joinPoint.proceed();

        String responseBody = objectMapper.writeValueAsString(result);
        log.info("ADMIN AOP >> requestUserId = [{}], requestTime = [{}], requestURL = [{}], requestBody = [{}], responseBody = [{}] ",
               requestUserId, LocalDateTime.now(), requestURI, requestBody, responseBody );
        return result;
    }
}
