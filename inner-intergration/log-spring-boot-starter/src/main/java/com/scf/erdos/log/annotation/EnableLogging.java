package com.scf.erdos.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.scf.erdos.log.selector.LogImportSelector;
import org.springframework.context.annotation.Import;

/**
 * @Description : 启动日志框架支持,自动装配starter ，需要配置多数据源
 * @author：bao-clm
 * @date: 2019/2/2
 * @version：1.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogImportSelector.class)
public @interface EnableLogging{
//	String name() ;
}