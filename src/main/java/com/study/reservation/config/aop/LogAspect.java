package com.study.reservation.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("execution(* com.study.reservation..*(..))")
    public void all() {}

    @Around("all()")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
        log.info("start -> " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());

        Object result = pjp.proceed();

        log.info("finish -> " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());

        return result;
    }

}
