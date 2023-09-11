package com.kyrie.annotation2;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SystemLogAspect {
    //注入Service用于把日志保存数据库


    //本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);


    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     * @param joinPoint 切入点
     * @param systemControllerLog 自定义注解
     * @Before 扫描com.xiaojukeji包及此包下的所有带有SystemLogAnnotation注解的方法
     */
    @Before(("execution(* com.kyrie.annotation2.controller2.*.*(..)) && @annotation(systemControllerLog)"))
    public void doBefore(JoinPoint joinPoint, SystemControllerLog systemControllerLog) {
        String logValue = systemControllerLog.value();
        System.out.println("前置通知:开始执行 -> 控制层日志log -> 用户执行了【"+logValue+"】操作");

    }

    @After(("execution(* com.kyrie.annotation2.controller2.*.*(..)) && @annotation(systemControllerLog)"))
    public void doAfter(JoinPoint joinPoint, SystemControllerLog systemControllerLog) {

        logger.info("=========================================用户操作日志-后置通知开始执行......=========================================");
        String value = systemControllerLog.value();
        // fixme 调用方法将当前操作记录到数据库日志表中
        logger.info("=========================================用户操作日志-后置通知结束执行......=========================================");


    }

}
