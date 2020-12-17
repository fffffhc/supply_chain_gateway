package com.scf.erdos.common.exception.dao;

import com.scf.erdos.common.exception.BaseException;

/**
 * @Description : 数据库相关异常
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */

public class DataAccessException extends BaseException {

	private static final long serialVersionUID = 8325096920926406459L;

	public DataAccessException(String msg) {
		super(msg);
	}

	public DataAccessException(Exception e) {
		this(e.getMessage());
	}
}
