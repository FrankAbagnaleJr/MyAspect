package com.kyrie.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Method;

@Aspect
@Component
public class SystemServiceLogAspect {

    //本地异常记录对象
    private static final Logger logger = LoggerFactory.getLogger(SystemServiceLogAspect.class);

    @Pointcut("@annotation(com.kyrie.annotation.SystemServiceLog)")
    public void serviceAspect() {
    }

    /**
     * 把方法执行后执行记录日志
     *
     * @param point
     * @throws Throwable
     */
    @Before("serviceAspect()")
    public void doBefore(ProceedingJoinPoint point) throws Throwable {
        //执行逻辑,从point中取

        //得到调用的方法名
        String name = point.getSignature().getName();

        //得到方法的返回值
        Object proceed = point.proceed();
        String methodResult = (String) proceed;

        //得到方法的参数
        String params = "";
        if (point.getArgs() != null && point.getArgs().length > 0) {
            if (point.getArgs()[0] != null) {
                params = point.getArgs()[0].toString();
            }
        }

        //得到签名 强转成方法签名
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        //得到签名 强转成字段签名
        //FieldSignature signature = (FieldSignature) point.getSignature();

        //得到注解标记的方法
        Method method = methodSignature.getMethod(); //通过反射得到加注解的方法
        //得到这个方法上的所有注解
        SystemServiceLog annotation = method.getAnnotation(SystemServiceLog.class);
        //如果不是null那说明加了注解
        if (annotation != null) {
            //得到自定义注解上的值”通过id查询用户“ ，例如 @SystemServiceLog(”通过id查询用户“)
            String value = annotation.value();
            //记录日志
            logger.info("当前操作：" + value + "，调用了" + name + "方法，方法参数是：" + params + ",返回值是：" + methodResult);   //当前操作：过id查询用户，调用了getById方法，返回值是 User(id=1,name=张三)
        }
    }

    @Around("serviceAspect()")
    public void joinpointDetailsTest(JoinPoint joinPoint) {
        try {
            //得到类对象
            Class<?> clazz = joinPoint.getTarget().getClass();
            //得到方法名
            String methodName = joinPoint.getSignature().getName();
            //得到参数类型的对象
            Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
            //根据类对象、方法名字、参数类型得到方法
            Method method = clazz.getMethod(methodName, parameterTypes);
            //得到方法上的PostMapping对象
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            //从PostMapping中得到路径
            String[] paths = postMapping.value();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
