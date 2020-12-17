package com.scf.erdos.common.exception.controller;

import com.scf.erdos.common.exception.BaseException;

/**
 * @Description : controller Exception 包装
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */

public class ControllerException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1412104290896291466L;

	public ControllerException(String msg) {
		super(msg);
	}

	public ControllerException(Exception e) {
		this(e.getMessage());
	}

}
