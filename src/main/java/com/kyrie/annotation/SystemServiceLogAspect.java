package com.kyrie.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SystemServiceLogAspect {

    @Pointcut("@annotation(com.kyrie.annotation.SystemServiceLog)")
    public void serviceAspect(){
    }

    @Before("serviceAspect()")
    public void doBefore(ProceedingJoinPoint Point){
        //执行逻辑,从point中取


    }
}
