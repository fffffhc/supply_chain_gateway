package com.scf.erdos.log.service.impl;

import java.util.Date;

import com.scf.erdos.common.model.SysLog;
import com.scf.erdos.log.dao.LogDao;
import com.scf.erdos.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.scf.erdos.datasource.annotation.DataSource;

/**
 * @Description : 切换数据源，存储log-center
 * @author：bao-clm
 * @date: 2019/2/3
 * @version：1.0
 */
public class LogServiceImpl implements LogService {

	@Autowired
	private LogDao logDao;

	@Async
	@Override
	@DataSource(name="log")
	public void save(SysLog log) {
		if (log.getCreateTime() == null) {
			log.setCreateTime(new Date());
		}
		if (log.getFlag() == null) {
			log.setFlag(Boolean.TRUE);
		}

		logDao.save(log);
	}

	 
}
