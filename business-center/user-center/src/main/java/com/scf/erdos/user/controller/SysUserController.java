package com.scf.erdos.user.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.scf.erdos.common.annotation.ApiIdempotent;
import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.exception.controller.ControllerException;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.common.model.SysUser;
import com.scf.erdos.common.util.StringUtil;
import com.scf.erdos.common.util.SysUserUtil;
import com.scf.erdos.common.util.ValidatorUtil;
import com.scf.erdos.log.annotation.LogAnnotation;
import com.scf.erdos.common.web.Result;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scf.erdos.user.model.SysUserExcel;
import com.scf.erdos.user.service.SysUserService;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description : 用户管理控制器
 * @author：bao-clm
 * @date: 2020/3/19
 * @version：1.0
 */
@Slf4j
@RestController
@Api(tags = "USER API")
@SuppressWarnings("all")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 当前登录用户信息 LoginAppUser （用户基础信息、用户角色信息、用户权限信息）
     *
     * @return
     * @throws ControllerException
     * @throws JsonProcessingException
     */
    @ApiOperation(value = "根据access_token当前登录用户")
    @GetMapping("/users/current")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result getLoginAppUser() throws ControllerException {

        try {
            return sysUserService.getLoginAppUser();
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 根据用户名查询用户
     *
     * @return
     * @throws ControllerException
     */
    @GetMapping(value = "/users-anon/login", params = "username")
    @ApiOperation(value = "根据用户名查询用户")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public LoginAppUser findByUsername(String username) throws ControllerException {
        try {
            return sysUserService.findByUsername(username);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 根据手机号查询用户
     *
     * @return
     * @throws ControllerException
     */
    @GetMapping(value = "/users-anon/mobile", params = "mobile")
    @ApiOperation(value = "根据用户名查询手机号")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public LoginAppUser findByMobile(String mobile) throws ControllerException {
        try {
            return sysUserService.findByMobile(mobile);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 根据用户id
     *
     * @return
     * @throws ControllerException
     */
    @GetMapping(value = "/users-anon/userIds", params = "userIds")
    @ApiOperation(value = "根据用户id获取用户信息")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public List<SysUser> findByUserIds(@RequestParam(value = "userIds") List<String> userIds) throws ControllerException {
        try {
            return sysUserService.findByUserIds(userIds);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 根据用户id
     *
     * @return
     * @throws ControllerException
     */
    @GetMapping(value = "/users-anon/userId", params = "userId")
    @ApiOperation(value = "根据用户id获取用户信息")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public SysUser findByUserId(@RequestParam(value = "userId") String userId) throws ControllerException {
        try {
            return sysUserService.findByUserId(userId);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 获取角色
     *
     * @return
     * @throws ControllerException
     */
    @GetMapping(value = "/users-anon/roleCode", params = "code")
    @ApiOperation(value = "根据用户id获取用户信息")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public List<SysRole> getRoles(@RequestParam(value = "code") String code) throws ControllerException {
        try {
            return sysUserService.getRoles(code);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 企业审批通过，赋予用户对应角色及企业id
     *
     * @return
     * @throws ControllerException
     */
    @PutMapping(value = "/users-anon/setCompanyRole")
    @ApiOperation(value = "企业审批通过，赋予用户对应角色及企业id")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public boolean setCompanyRole(@RequestParam Map<String, Object> params) throws ControllerException {
        try {
            return sysUserService.setCompanyRole(params);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 根据用户id查询用户
     *
     * @return
     * @throws ControllerException
     */
    //@PreAuthorize("hasAuthority('user:get/users/{id}')")
    @GetMapping("/users/{id}")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result findUserById(@PathVariable Long id) throws ControllerException {
        try {
            SysUser sysUser = sysUserService.findById(id);
            return Result.succeed(sysUser,"根据用户id查询用户信息获取成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 管理后台，给用户重置密码
     *
     * @param id
     * @param newPassword
     * @throws ControllerException
     */
    @PreAuthorize("hasAnyAuthority('user:put/users/password','user:post/users/{id}/resetPassword')")
    @PutMapping(value = "/users/password/{id}", params = {"newPassword"})
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result resetPassword(@PathVariable Long id, String newPassword) throws ControllerException {
        try {
            sysUserService.updatePassword(id, null, newPassword);
            return Result.succeed(null,"给用户重置成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 管理后台修改用户
     *
     * @param sysUser
     * @throws JsonProcessingException
     */
    @PreAuthorize("hasAuthority('user:put/users/me')")
    @PutMapping("/users")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result updateSysUser(@RequestBody SysUser sysUser) throws ControllerException {
        try {
            sysUserService.updateSysUser(sysUser);
            return Result.succeed(null, "管理后台修改用户信息成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 管理后台给用户分配角色
     *
     * @param id
     * @param roleIds
     * @throws JsonProcessingException
     */
    @PreAuthorize("hasAuthority('user:post/users/{id}/roles')")
    @PostMapping("/users/roles/{id}")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result setRoleToUser(@PathVariable Long id, @RequestBody Set<Long> roleIds) throws ControllerException {
        try {
            sysUserService.setRoleToUser(id, roleIds);
            return Result.succeed("管理后台给用户分配角色成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 获取用户的角色
     *
     * @param
     * @return
     * @throws ControllerException
     */
    @PreAuthorize("hasAnyAuthority('user:get/users/{id}/roles')")
    @GetMapping("/users/roles/{id}")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result findRolesByUserId(@PathVariable Long id) throws ControllerException {
        try {
            Set<SysRole> sysRole = sysUserService.findRolesByUserId(id);
            return Result.succeed(sysRole,"获取用户的角色成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 用户查询（分页）
     *
     * @param params
     * @return
     * @throws ControllerException
     */
    @PreAuthorize("hasAuthority('user:get/users')")
    @ApiOperation(value = "用户查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页展示条数", required = true, dataType = "Integer")
    })
    @GetMapping("/users")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result findUsers(@RequestHeader(name = "trace_id", required = false) String traceId, @RequestParam Map<String, Object> params) throws ControllerException {

        try {
            return sysUserService.findUsers(params);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 修改自己的个人信息
     *
     * @param sysUser
     * @return
     * @throws ControllerException
     */
    @PutMapping("/users/me")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    @PreAuthorize("hasAnyAuthority('user:put/users/me','user:post/users/saveOrUpdate')")
    public Result updateMe(@RequestBody SysUser sysUser) throws ControllerException {
        try {
            SysUser user = sysUserService.updateSysUser(sysUser);
            return Result.succeed("修改自己的个人信息成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 修改密码
     *
     * @param sysUser
     * @throws ControllerException
     */
    @PutMapping(value = "/users/password")
    @PreAuthorize("hasAuthority('user:put/users/password')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result updatePassword(@RequestBody SysUser sysUser) throws ControllerException {
        //通过 UserDetails 获取用户信息
        LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
        long i = loginUser.getId();
        String username = loginUser.getUsername();

        try {
            if (StringUtils.isBlank(sysUser.getOldPassword())) {
                return Result.failed("旧密码不能为空");
            }
            if (StringUtils.isBlank(sysUser.getNewPassword())) {
                return Result.failed("新密码不能为空");
            }

            if (StringUtil.isNotEmpty(username) && "admin".equals(username)) {
                return Result.failed("超级管理员不给予修改");
            }

            return sysUserService.updatePassword(i, sysUser.getOldPassword(), sysUser.getNewPassword());
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 修改用户状态
     *
     * @param params
     * @return
     * @throws ControllerException
     * @author gitgeek
     */
    @ApiOperation(value = "修改用户状态")
    @GetMapping("/users/updateEnabled")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "enabled", value = "是否启用", required = true, dataType = "Boolean")
    })
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    @PreAuthorize("hasAnyAuthority('user:get/users/updateEnabled' ,'user:put/users/me')")
    public Result updateEnabled(@RequestParam Map<String, Object> params) throws ControllerException {
        try {
            Long id = MapUtils.getLong(params, "id");
            if (id == 1L) {
                return Result.failed("超级管理员不给予修改");
            }
            return sysUserService.updateEnabled(params);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 管理后台，给用户重置密码
     *
     * @param id
     * @throws ControllerException
     * @author gitgeek
     */
    @PreAuthorize("hasAuthority('user:post/users/{id}/resetPassword' )")
    @PostMapping(value = "/users/resetPassword/{id}")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result resetPassword(@PathVariable Long id) throws ControllerException {
        try {
            if (id == 1L) {
                return Result.failed("超级管理员不给予修改");
            }
            sysUserService.updatePassword(id, null, "123456");
            return Result.succeed("管理后台，给用户重置密码重置成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 获取角色-用于给用户设置角色
     *
     * @throws ControllerException
     * @author gitgeek
     */
    @PreAuthorize("hasAuthority('user:post/users/{id}/resetPassword' )")
    @GetMapping(value = "/users/getRoles")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result getRoles() throws ControllerException {
        try {
            List<SysRole> list = sysUserService.getRolesByCode();
            return Result.succeed(list,"获取成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }


    /**
     * 新增or更新
     *
     * @param sysUser
     * @return
     * @throws ControllerException
     */
    @PostMapping("/users/saveOrUpdate")
    @PreAuthorize("hasAnyAuthority('user:post/users/saveOrUpdate')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result saveOrUpdate(@RequestBody SysUser sysUser) throws ControllerException {
        try {
            return sysUserService.saveOrUpdate(sysUser);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }



    /**
     * 企业入驻申请（相当于企业用户注册）
     *
     * @param sysUser
     * @return
     * @throws ControllerException
     */
    @PostMapping("/users/register")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result register(@RequestParam Map<String, Object> params) throws ControllerException {
        try {
            return sysUserService.register(params);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 校验用户是否首次登陆
     *
     * @param sysUser
     * @return
     * @throws ControllerException
     */
    @GetMapping("/users/getInvitationCode")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result getInvitationCode(@RequestParam Map<String, Object> params) throws ControllerException {
        try {
            return sysUserService.getInvitationCode(params);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 导出数据
     *
     * @return
     * @throws ControllerException
     */
    @GetMapping("/users/exportUser")
    @PreAuthorize("hasAuthority('user:post/users/exportUser')")
    public Result exportUser(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws ControllerException {
        try {
            Integer page = MapUtils.getInteger(params, "page");
            Integer limit = MapUtils.getInteger(params, "limit");
            if(page<1 || limit<1){
                return Result.failed("page和limit不能为空，page不能小于1，首页从1开始！");
            }

            List<SysUserExcel> result = sysUserService.findAllUsers(params);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=myExcel.xls");
            @Cleanup OutputStream ouputStream = null;
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("用户导出", "用户"),
                    SysUserExcel.class, result);
            ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            return Result.succeed("导出成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        } catch (IOException e) {
            throw new ControllerException(e);
        }
    }


    /**
     * 测试幂等接口
     *
     * @param sysUser
     * @return
     * @throws ControllerException
     */
    @PostMapping("/users/save")
    @ApiIdempotent
    public Result save(@RequestBody SysUser sysUser) throws ControllerException {
        try {
            return sysUserService.saveOrUpdate(sysUser);
        } catch (ServiceException e) {
            log.error("执行" + this.getClass().getSimpleName() + ":" + new Exception().getStackTrace()[0].getMethodName());
            throw new ControllerException(e);
        }
    }

    @ApiOperation(value = "import-excel")
    @PostMapping("/importExcel")
    public Result importExcel(HttpServletRequest request,@RequestParam("file") MultipartFile file) {
        ImportParams params = new ImportParams();
        //从第一行开始读
        params.setHeadRows(1);
        try {
            List<ExcelCertificateInfo> result = ExcelImportUtil.importExcel(file.getInputStream(),
                    ExcelCertificateInfo.class, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
