package com.scf.erdos.common.exception;

/**
 * @Description : 基础异常类，系统定义的所有异常都需要继承这个基本类
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 7859712770754900356L;

	public BaseException(String msg) {
	  super(msg);
	}

	public BaseException(Exception e){
	  this(e.getMessage());
	}
}
