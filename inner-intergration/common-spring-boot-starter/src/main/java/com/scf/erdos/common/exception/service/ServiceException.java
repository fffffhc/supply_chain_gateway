package com.scf.erdos.common.exception.service;

import com.scf.erdos.common.exception.BaseException;

/**
 * @Description : service处理异常
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
public class ServiceException extends BaseException {

	private static final long serialVersionUID = -2437160791033393978L;

	public ServiceException(String msg) {
	  super(msg);
	}

	public ServiceException(Exception e){
	  this(e.getMessage());
	}
}
