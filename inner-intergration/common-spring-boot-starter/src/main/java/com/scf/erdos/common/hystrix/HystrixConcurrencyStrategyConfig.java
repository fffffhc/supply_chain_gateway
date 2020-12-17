package com.scf.erdos.common.hystrix;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @Description : 配置熔断
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
@Configuration
public class HystrixConcurrencyStrategyConfig {
 
	@Bean
	public RequestAttributeHystrixConcurrencyStrategy requestAttributeHystrixConcurrencyStrategy() {
		return new RequestAttributeHystrixConcurrencyStrategy();
	}
	
}
