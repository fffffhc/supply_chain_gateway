package com.scf.erdos.user.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.scf.erdos.common.exception.controller.ControllerException;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysMenu;
import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.common.util.SysUserUtil;
import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.web.CodeEnum;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.log.annotation.LogAnnotation;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scf.erdos.user.service.SysMenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description : 菜单管理控制器
 * @author：bao-clm
 * @date: 2020/3/19
 * @version：1.0
 */
@Slf4j
@RestController
@Api(tags = "MENU API")
@RequestMapping("/menus")
@SuppressWarnings("all")
public class SysMenuController {

    @Autowired
    private SysMenuService menuService;
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 删除菜单
     *
     * @param id
     * @throws ControllerException
     */
    @PreAuthorize("hasAuthority('menu:post/menus')")
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/{id}")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result delete(@PathVariable Long id) throws ControllerException {

        try {
            try {
                menuService.delete(id);
                return Result.succeed("操作成功");
            } catch (Exception ex) {
                log.error("SysMenuController->run:{}", ex.getMessage());
                return Result.failed("操作失败");
            }
        } catch (Exception e) {
            throw new ControllerException(e);
        }

    }

    @PreAuthorize("hasAuthority('menu:get/menus/{roleId}/menus')")
    @ApiOperation(value = "根据roleId获取对应的菜单")
    @GetMapping("/menus/{roleId}")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result findMenusByRoleId(@PathVariable Long roleId) throws ControllerException {
      return menuService.findMenusByRoleId(roleId);
    }

    /**
     * 给角色分配菜单
     *
     * @throws ControllerException
     */
    @PreAuthorize("hasAuthority('menu:post/menus/granted')")
    @ApiOperation(value = "角色分配菜单")
    @PostMapping("/granted")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result setMenuToRole(@RequestBody SysMenu sysMenu) throws ControllerException {

        try {
            menuService.setMenuToRole(sysMenu.getRoleId(), sysMenu.getMenuIds());

            return Result.succeed("操作成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

    }

    @PreAuthorize("hasAuthority('menu:get/menus/findAlls')")
    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/findAlls")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result findAlls() throws ControllerException {
        try {
            List<SysMenu> list = menuService.findAll();
            return Result.succeedWith(list,CodeEnum.SUCCESS.getCode(),"获取成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    @ApiOperation(value = "获取菜单以及顶级菜单")
    @GetMapping("/findOnes")
    @PreAuthorize("hasAuthority('menu:get/menus/findOnes')")
    public Result findOnes() throws ControllerException {
        try {
            List<SysMenu> list = menuService.findOnes();
            return Result.succeedWith(list,CodeEnum.SUCCESS.getCode(),"获取成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 添加菜单 或者 更新
     *
     * @param menu
     * @return
     * @throws ControllerException
     */
    @PreAuthorize("hasAnyAuthority('menu:post/menus','menu:put/menus')")
    @ApiOperation(value = "新增菜单")
    @PostMapping("saveOrUpdate")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result saveOrUpdate(@RequestBody SysMenu menu) throws ControllerException {
        try {
            if (menu.getId() != null) {
                menuService.update(menu);
            } else {
                menuService.save(menu);
            }
            return Result.succeed("操作成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 当前登录用户的菜单（左侧菜单树）
     *
     * @return
     * @throws ControllerException
     */
    @PreAuthorize("hasAuthority('menu:get/menus/current')")
    @GetMapping("/current")
    @ApiOperation(value = "查询当前用户菜单")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result findMyMenu() throws ControllerException {

        try {
            LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
            Set<SysRole> roles = loginAppUser.getSysRoles();
            if (CollectionUtils.isEmpty(roles)) {
                return Result.failedWith(null,CodeEnum.ERROR.getCode(),"当前用户无角色");
            }

            List<SysMenu> menus = menuService.findByRoles(roles.parallelStream().map(SysRole::getId).collect(Collectors.toSet()));
            List<SysMenu> sysMenus = TreeBuilder(menus);
            return Result.succeedWith(sysMenus,CodeEnum.SUCCESS.getCode(),"操作成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 两层循环实现建树
     *
     * @param sysMenus
     * @return
     * @throws ControllerException
     */
    public static List<SysMenu> TreeBuilder(List<SysMenu> sysMenus) throws ControllerException {

        try {
            List<SysMenu> menus = new ArrayList<SysMenu>();
            for (SysMenu sysMenu : sysMenus) {
                if (ObjectUtils.equals(-1L, sysMenu.getParentId())) {
                    menus.add(sysMenu);
                }
                for (SysMenu menu : sysMenus) {
                    if (menu.getParentId().equals(sysMenu.getId())) {
                        if (sysMenu.getSubMenus() == null) {
                            sysMenu.setSubMenus(new ArrayList<>());
                        }
                        sysMenu.getSubMenus().add(menu);
                    }
                }
            }
            return menus;
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

}
