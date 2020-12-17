package com.scf.erdos.user.controller;

import java.util.Map;

import com.scf.erdos.common.exception.controller.ControllerException;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.log.annotation.LogAnnotation;
import com.scf.erdos.user.service.SysRoleService;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description : 角色管理
 * @author：bao-clm
 * @date: 2020/3/31
 * @version：1.0
 */

@RestController
@Api(tags = "ROLE API")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;
	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 后台管理查询角色
	 * @param params
	 * @return
	 * @throws JsonProcessingException 
	 */
	@PreAuthorize("hasAuthority('role:get/roles')")
	@ApiOperation(value = "后台管理查询角色")
	@GetMapping("/roles")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result findRoles(@RequestParam Map<String, Object> params) throws ControllerException {
		try {
			return sysRoleService.findRoles(params);
		} catch (ServiceException e) {
			 throw new ControllerException(e);
		}
	}

	/**
	 * 角色新增或者更新
	 * @param sysRole
	 * @return
	 * @throws ControllerException 
	 */
	@PreAuthorize("hasAnyAuthority('role:post/roles','role:put/roles')")
	@PostMapping("/roles/saveOrUpdate")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result saveOrUpdate(@RequestBody SysRole sysRole) throws ControllerException {
		try {
			return sysRoleService.saveOrUpdate(sysRole);
		} catch (ServiceException e) {
			 throw new ControllerException(e);
		}
	}

	/**
	 * 角色新增或者更新V1.1
	 * @param sysRole
	 * @return
	 * @throws ControllerException
	 */
	//@PreAuthorize("hasAnyAuthority('role:post/roles','role:put/roles')")
	@PostMapping("/roles/saveOrUpdateV1")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result saveOrUpdateV1_1(@Validated SysRole sysRole) throws ControllerException {
		try {
			return sysRoleService.saveOrUpdateV1_1(sysRole);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}
	/**
	 * 获取角色详情
	 * @param sysRole
	 * @return
	 * @throws ControllerException
	 */
	//@PreAuthorize("hasAnyAuthority('role:post/roles','role:getRoleInfo/roles')")
	@GetMapping("/roles/getRoleInfo")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result getRoleInfo(@Validated SysRole sysRole) throws ControllerException {
		try {
			return sysRoleService.getRoleInfo(sysRole);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}


	/**
	 * 后台管理删除角色
	 * delete /role/1
	 * @param id
	 * @throws ControllerException 
	 */
	@PreAuthorize("hasAuthority('role:delete/roles/{id}')")
	@ApiOperation(value = "后台管理删除角色")
	@DeleteMapping("/roles/{id}")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result deleteRole(@PathVariable Long id) throws ControllerException {
		try {
			if (id == 1L){
				return Result.failed("管理员不可以删除");
			}
			sysRoleService.deleteRole(id);
			return Result.succeed("操作成功");
		}catch (Exception e){
			 throw new ControllerException(e);
		}
	}

}
