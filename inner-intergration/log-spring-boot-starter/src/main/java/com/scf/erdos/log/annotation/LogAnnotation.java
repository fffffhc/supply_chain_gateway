package com.scf.erdos.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description : 日志注解
 * @author：bao-clm
 * @date: 2019/2/3
 * @version：1.0
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    /**
     * 模块
     *
     * @return
     */
    String module();

    /**
     * 记录执行参数
     * recordRequestParam:true需要配置log数据源
     *
     * @return
     */
    boolean recordRequestParam() default false;
}
