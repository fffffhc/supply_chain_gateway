package com.scf.erdos.user.controller;

import java.util.List;
import java.util.Map;
import com.scf.erdos.common.exception.controller.ControllerException;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysPermission;
import com.scf.erdos.common.web.CodeEnum;
import com.scf.erdos.log.annotation.LogAnnotation;
import com.scf.erdos.user.service.SysPermissionService;
import com.scf.erdos.common.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * @Description : 权限管理控制器
 * @author：bao-clm
 * @date: 2020/3/19
 * @version：1.0
 */

@RestController
@Api(tags = "PERMISSION API")
@SuppressWarnings("all")
public class SysPermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 删除权限标识
     * 参考 /permissions/1
     *
     * @param id
     * @throws ControllerException
     */
    @PreAuthorize("hasAuthority('permission:delete/permissions/{id}')")
    @ApiOperation(value = "后台管理删除权限标识")
    @DeleteMapping("/delete/permissions/{id}")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result delete(@PathVariable Long id) throws ControllerException {

        try {
            sysPermissionService.delete(id);
            return Result.succeed("操作成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

    }


    /**
     * 查询所有的权限标识
     *
     * @return
     * @throws ControllerException
     */
    @PreAuthorize("hasAuthority('permission:get/permissions')")
    @ApiOperation(value = "后台管理查询所有的权限标识")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "Integer")
    })
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    @GetMapping("/permissions")
    public Result findPermissions(@RequestParam Map<String, Object> params) throws ControllerException {

        try {
            return sysPermissionService.findPermissions(params);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 权限新增或者更新
     *
     * @param sysPermission
     * @return
     * @throws ControllerException
     */
    @PreAuthorize("hasAnyAuthority('permission:put/permissions','permission:post/permissions')")
    @PostMapping("/permissions/saveOrUpdate")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result saveOrUpdate(@RequestBody SysPermission sysPermission) throws ControllerException {
        try {
            if (sysPermission.getId() != null) {
                sysPermissionService.update(sysPermission);
            } else {
                sysPermissionService.save(sysPermission);
            }
            return Result.succeed("操作成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    @PreAuthorize("hasAuthority('permission:get/permissions/{roleId}/permissions')")
    @ApiOperation(value = "根据roleId获取对应的权限")
    @GetMapping("/permissions/permissions/{roleId}")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result findAuthByRoleId(@PathVariable Long roleId) throws ControllerException {
        try {
            List<Map<String, Object>> allAuths = sysPermissionService.findAuthByRoleId(roleId);
            return Result.succeedWith(allAuths,CodeEnum.SUCCESS.getCode(),"获取成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 给角色分配权限
     *
     * @throws ControllerException
     */
    @PreAuthorize("hasAuthority('permission:post/permissions/granted')")
    @ApiOperation(value = "角色分配权限")
    @PostMapping("/permissions/granted")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result setAuthToRole(@RequestBody SysPermission sysPermission) throws ControllerException {
        try {
            sysPermissionService.setAuthToRole(sysPermission.getRoleId(), sysPermission.getAuthIds());
            return Result.succeed("操作成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }


}
