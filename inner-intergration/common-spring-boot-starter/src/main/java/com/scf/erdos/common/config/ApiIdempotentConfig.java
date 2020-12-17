package com.scf.erdos.common.config;

import javax.annotation.Resource;

import com.scf.erdos.common.interceptor.AccessLimitInterceptor;
import com.scf.erdos.common.interceptor.ApiIdempotentInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.scf.erdos.redis.util.RedisUtil;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
public class ApiIdempotentConfig implements  WebMvcConfigurer {


    @Resource
    private RedisTemplate< String, Object> redisTemplate ;

    @Autowired
	private RedisUtil redisUtil;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new AccessLimitInterceptor(redisUtil)) ;
        registry.addInterceptor(new ApiIdempotentInterceptor(redisTemplate)).addPathPatterns("/**") ;

    }
}
