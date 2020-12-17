package com.scf.erdos.log.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Description : log-spring-boot-starter 自动装配
 * @author：bao-clm
 * @date: 2019/2/3
 * @version：1.0
 */
public class LogImportSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		// TODO Auto-generated method stub
		return new String[] {
				"com.scf.erdos.log.aop.LogAnnotationAOP",
				"com.scf.erdos.log.service.impl.LogServiceImpl",
				"com.scf.erdos.log.config.LogAutoConfig"
		};
	}

}