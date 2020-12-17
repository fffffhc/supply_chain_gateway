package com.scf.erdos.common.annotation;

import com.scf.erdos.common.selector.ApiIdempotentImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description : 启动幂等拦截器
 * @author：bao-clm
 * @date: 2019/2/3
 * @version：1.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@Import(ApiIdempotentImportSelector.class)
public @interface EnableApiIdempotent {
}
