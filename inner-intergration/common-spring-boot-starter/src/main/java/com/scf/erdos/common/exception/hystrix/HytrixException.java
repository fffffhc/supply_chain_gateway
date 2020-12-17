package com.scf.erdos.common.exception.hystrix;

import com.netflix.hystrix.exception.HystrixBadRequestException;

/**
 * @Description : feign client 避免熔断异常处理
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
public class HytrixException extends HystrixBadRequestException {

	private static final long serialVersionUID = -2437160791033393978L;

	public HytrixException(String msg) {
	  super(msg);
	}

	public HytrixException(Exception e){
	  this(e.getMessage());
	}
}
