package com.scf.erdos.log.dao;

import javax.sql.DataSource;

import com.scf.erdos.common.model.SysLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

/**
 * @Description : 保存日志，需要配置多数据源才可以支持
 * @author：bao-clm
 * @date: 2019/2/3
 * @version：1.0
 */
@Mapper
@ConditionalOnBean(DataSource.class)
public interface LogDao {

	@Insert("insert into sys_log(username, module, params, remark, flag, createTime) " +
			"values(#{username}, #{module}, #{params}, #{remark}, #{flag}, #{createTime})")
	int save(SysLog log);

}
