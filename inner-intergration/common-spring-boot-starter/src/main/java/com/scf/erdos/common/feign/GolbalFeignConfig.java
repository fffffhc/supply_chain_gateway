package com.scf.erdos.common.feign;

import org.springframework.context.annotation.Bean;

import feign.Logger.Level;
/**
 * @Description :
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
public class GolbalFeignConfig {

	@Bean
	public Level levl(){
		return Level.FULL;
	}
}
