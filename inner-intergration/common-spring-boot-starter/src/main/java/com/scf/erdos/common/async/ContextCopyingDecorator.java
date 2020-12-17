package com.scf.erdos.common.async;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @Description : 传递RequestAttributes and MDC
 * @author：bao-clm
 * @date: 2019/2/3
 * @version：1.0
 */

public class ContextCopyingDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        try {
			RequestAttributes context = RequestContextHolder.currentRequestAttributes(); 
			Map<String,String> previous = MDC.getCopyOfContextMap(); 
			SecurityContext securityContext = SecurityContextHolder.getContext();// 1
			return () -> {
			    try {
			    	if(previous==null){
			    		MDC.clear();
			    	}else{
			    		MDC.setContextMap(previous);
			    	}
			    	
			        RequestContextHolder.setRequestAttributes(context);
			        SecurityContextHolder.setContext(securityContext);// 2
			        runnable.run();
			    } finally {
			        RequestContextHolder.resetRequestAttributes();
			        // 清除操作
			        SecurityContextHolder.clearContext();// 3

			        if(previous==null){
			    		MDC.clear();
			    	}else{
			    		MDC.setContextMap(previous);
			    	}
			    }
			};
		} catch (IllegalStateException e) {
			return runnable;
		}
    }
}
