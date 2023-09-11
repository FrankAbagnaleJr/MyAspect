package com.kyrie.annotation;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class SystemControllerLogAspect {
    //注入Service用于把日志保存数据库


    //本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(SystemControllerLogAspect.class);


    //Controller层切点

    /**
     * 指定加了这个注解的类为切入点
     * com.wimoor.common.service.impl.SystemControllerLog 这是自定义注解的全限定名
     */
    @Pointcut("@annotation(com.kyrie.annotation.SystemControllerLog)")
    public void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        //执行逻辑

        //加入该注解的方法应该用public修饰
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //读取session中的用户
        UserInfo userinfo = UserInfoContext.get();
        //请求的IP
        String ip = request.getRemoteAddr();
        //获取用户请求方法的参数并序列化为JSON格式字符串

        String params = "";
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            if (joinPoint.getArgs()[0] != null) {
                params = joinPoint.getArgs()[0].toString();
            }
        }

        IOperationLogService operationLogService;

        try {
            String desc = getControllerMethodDescription(joinPoint);
            if (desc == null) return;
            operationLogService = SpringUtil.getBean("operationLogService");
            //创建一个数据库表对象，来向数据库记录执行的日志
            OperationLog log = new OperationLog();
            log.setDescription(desc);
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            log.setIp(ip);
            log.getId();
            log.setParam(params);
            if (userinfo != null) {
                log.setUserid(userinfo.getId());
            }
            log.setTime(new Date());
            //保存数据库
            operationLogService.save(log);
        } catch (Exception e) {
            //记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息:{}", e.getMessage());
        }
    }

    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        String des = targetClass.getAnnotation(SystemControllerLog.class).toString();
        des = des.substring(des.indexOf("(") + 1, des.indexOf(")"));
        description = des.split("=")[1];
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                @SuppressWarnings("rawtypes")
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    if (method.getAnnotation(SystemControllerLog.class) == null) return null;
                    description = description + "-" + method.getAnnotation(SystemControllerLog.class).value();
                    break;
                }
            }
        }
        return description;
    }
}
