package com.scf.erdos.uaa.controller;

import java.util.Map;
import com.scf.erdos.common.web.CodeEnum;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.log.annotation.LogAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.scf.erdos.uaa.dto.SysClientDto;
import com.scf.erdos.uaa.model.SysClient;
import com.scf.erdos.uaa.service.SysClientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description : 应用管理控制器
 * @author：bao-clm
 * @date: 2019/1/28
 * @version：1.0
 */

@RestController
@Api(tags = "CLIENT API")
@RequestMapping("/clients")
@SuppressWarnings("all")
public class SysClientController {

    @Autowired
    private SysClientService sysClientService;


    @GetMapping
    @ApiOperation(value = "应用列表")
    @PreAuthorize("hasAuthority('client:get/clients')")
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    public Result listRoles(@RequestParam Map<String, Object> params) {
        return sysClientService.listRoles(params) ;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取应用")
    @PreAuthorize("hasAuthority('client:get/clients/{id}')")
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    public Result get(@PathVariable Long id) {
        return Result.succeedWith(sysClientService.getById(id), CodeEnum.SUCCESS.getCode(),"操作成功");
    }

    @GetMapping("/all")
    @ApiOperation(value = "所有应用")
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    @PreAuthorize("hasAnyAuthority('client:get/clients')")
    public Result roles() {
        return Result.succeedWith(sysClientService.findList(Maps.newHashMap()), CodeEnum.SUCCESS.getCode(),"操作成功");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除应用")
    @PreAuthorize("hasAuthority('client:delete/clients')")
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    public Result delete(@PathVariable Long id) {
    	sysClientService.deleteClient(id);
        return Result.succeedWith(null, CodeEnum.SUCCESS.getCode(),"操作成功");
    }

	@PostMapping("/saveOrUpdate")
    @ApiOperation(value = "保存或者修改应用")
    @PreAuthorize("hasAuthority('client:post/clients')")
    public Result saveOrUpdate(@RequestBody SysClientDto clientDto){
        return  sysClientService.saveOrUpdate(clientDto);
    }

    @PutMapping("/updateEnabled")
    @ApiOperation(value = "修改状态")
    @PreAuthorize("hasAuthority('client:post/clients')")
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    public Result updateEnabled(@RequestBody Map<String, Object> params){
        return  sysClientService.updateEnabled(params);
    }
    
}
