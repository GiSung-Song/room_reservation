package com.study.reservation.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around("execution(* com.study.reservation.member..*(..))")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
        log.info("start -> " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());

        Object result = pjp.proceed();

        log.info("finish -> " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());

        return result;
    }

}
