package com.scf.erdos.user.dao;

import java.util.List;
import java.util.Map;

import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.common.model.SysUser;
import org.apache.ibatis.annotations.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
 * 用户管理
 */
@Mapper
public interface SysUserDao  extends BaseMapper<SysUser> {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into sys_user(username, password,realname,id_num,nickname, headImgUrl,phone, " +
			"birthday,sex,post,company_id,company_addr,telephone, enabled,email, type, createTime, updateTime) "
			+ "values(#{username}, #{password}, #{realname},#{idNum},#{nickname}, #{headImgUrl},#{phone}," +
			" #{birthday},#{sex},#{post},#{companyId},#{companyAddr},#{telephone}, #{enabled},#{email}, #{type}, #{createTime}, #{updateTime})")
	int save(SysUser sysUser);

	@Insert("insert into sys_user(username, password,company_name,credit_no,realname,contacter,phone, createTime) "
			+ "values(#{username}, #{password},#{companyName},#{creditNo},#{contacter},#{contacter},#{phone},#{createTime})")
	int insertCompanyUser(SysUser sysUser);

	int updateByOps(SysUser sysUser);

	@Select("select * from sys_user t where t.username = #{username}")
	SysUser findByUsername(String username);
	@Select("select * from sys_user t where t.company_name = #{companyName}")
	SysUser findByCompanyName(String companyName);
	@Select("select * from sys_user t where t.credit_no = #{creditNo}")
	SysUser findByCreditNo(String creditNo);

	SysUser findById(Long id);

	int count(Map<String, Object> params);

	List<SysUser> findList(Map<String, Object> params);

	@Select("select u.* from sys_user u   where u.username = #{username}")
	SysUser findUserByUsername(String username);

	@Select("select u.* from sys_user u   where u.phone = #{mobile}")
	SysUser findUserByMobile(String mobile);

	@Select("select * from sys_role")
	List<SysRole> getRoles();
	List<SysRole> getRolesByCode(@Param("code") String code);

	@Select("select roleId id FROM sys_role_user WHERE userId = #{id}")
	SysRole getRoleByUserId(long id);

	@Select("select * FROM sys_user WHERE id = #{userId}")
	SysUser findByUserId(@Param("userId") String userId);

	List<SysUser> findByUserIds(List<String> list);

	int updateCompanyUser(Map<String, Object> params);

	int setRole(Map<String, Object> params);

	@Delete("delete from sys_role_user where userId = #{userId}")
	int deleteUserRole(Map<String, Object> params);
}
