package com.scf.erdos.uaa.client.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description : 查询应用绑定的资源权限
 * @author：bao-clm
 * @date: 2020/3/25
 * @version：1.0
 */
@Mapper
@SuppressWarnings("all")
public interface SysServiceDao {

	@Select("select p.* from sys_service p inner join sys_client_service rp on p.id = rp.serviceId where rp.clientId = #{clientId} order by p.sort")
	List<Map> listByClientId(Long clientId);

}
