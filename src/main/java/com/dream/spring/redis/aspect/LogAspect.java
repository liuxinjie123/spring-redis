package com.dream.spring.redis.aspect;

import com.dream.spring.redis.aop.Action;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class LogAspect {
    @Pointcut("@annotation(com.dream.spring.redis.aop.Action)")
    public void annotationPointCut(){};

    @Before("annotationPointCut()")
    public void annotationBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Action action = method.getAnnotation(Action.class);
        System.out.println("注解式拦截-before：" + action.name() + ", now=" + System.currentTimeMillis());
    }

    @After("annotationPointCut()")
    public void annotationAfter(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Action action = method.getAnnotation(Action.class);
        System.out.println("注解式拦截-after：" + action.name() + ", now=" + System.currentTimeMillis());
    }

    @Before("execution(* com.dream.spring.redis.controller.*.*.*(..))")
    public void methodBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        System.out.println("方法式拦截规则-before：" + method.getName() + ", now=" + System.currentTimeMillis());
    }

    @After("execution(* com.dream.spring.redis.controller.*.*.*(..))")
    public void methodAfter(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        System.out.println("方法式拦截规则-after：" + method.getName() + ", now=" + System.currentTimeMillis());
    }
}
