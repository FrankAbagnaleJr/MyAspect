package com.kyrie.annotation2;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
//运行时还在，可以通过反射来获取加了这个注解 类的参数
@Retention(RetentionPolicy.RUNTIME)
//生产文档时显示
@Documented
public @interface SystemControllerLog {
    String value() default "";
}
