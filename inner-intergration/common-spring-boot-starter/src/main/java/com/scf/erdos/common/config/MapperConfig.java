package com.scf.erdos.common.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @Description : dto pojo 互转
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */

@Configuration
public class MapperConfig {
	
	@Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
