package com.scf.erdos.user.dao;

import java.util.List;
import java.util.Map;

import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.user.model.SysRoleMenus;
import org.apache.ibatis.annotations.*;

/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
 * 角色
 */
@Mapper
public interface SysRoleDao {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into sys_role(company_label,code, name, createTime, updateTime) values(#{companyLabel},#{code}, #{name}, #{createTime}, #{createTime})")
	int save(SysRole sysRole);

	@Update("update sys_role t set t.name = #{name},t.company_label = #{companyLabel},t.code = #{code} ,t.updateTime = #{updateTime} where t.id = #{id}")
	int updateByOps(SysRole sysRole);

	@Select("select * from sys_role t where t.id = #{id}")
	SysRole findById(Long id);

	@Select("select * from sys_role t where t.code = #{code}")
	SysRole findByCode(String code);

	@Delete("delete from sys_role where id = #{id}")
	int delete(Long id);

	int count(Map<String, Object> params);

	List<SysRole> findList(Map<String, Object> params);

	int saveRoleMenus(SysRole sysRole);

	@Select("select * from sys_role where company_label = #{companyLabel} and name = #{name}")
	SysRole findBySysRole(SysRole sysRole);

	@Select("select * from sys_role where company_label = #{companyLabel} order by id desc limit 1")
	SysRole findCodeSysRole(SysRole sysRole);

	@Delete("delete from sys_role_menu where roleId = #{id}")
	int deleteMenus(@Param("id") Long id);

	@Select("select * from sys_role where id = #{id}")
	SysRoleMenus getRoleInfo(SysRole sysRole);
}
