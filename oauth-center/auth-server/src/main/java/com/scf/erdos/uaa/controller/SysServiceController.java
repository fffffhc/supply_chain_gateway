package com.scf.erdos.uaa.controller;

import java.util.*;
import com.scf.erdos.common.web.CodeEnum;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.log.annotation.LogAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.scf.erdos.uaa.dto.SysClientDto;
import com.scf.erdos.uaa.model.SysService;
import com.scf.erdos.uaa.service.SysServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description : service 控制器
 * @author：bao-clm
 * @date: 2019/1/12
 * @version：1.0
 */
@Slf4j
@RestController
@Api(tags = "SERVICE API")
@RequestMapping("/services")
@SuppressWarnings("all")
public class SysServiceController {

    @Autowired
    private SysServiceService sysServiceService;

    /**
     * 查询所有服务
     * @return
     */
    @ApiOperation(value = "查询所有服务")
    @GetMapping("/findAlls")
    @PreAuthorize("hasAuthority('service:get/service/findAlls')")
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    public Result findAlls() {
        List<SysService> list = sysServiceService.findAll();
        return Result.succeedWith(list,CodeEnum.SUCCESS.getCode(),"操作成功");
    }

    /**
     * 获取服务以及顶级服务
     * @return
     */
    @ApiOperation(value = "获取服务以及顶级服务")
    @GetMapping("/findOnes")
    @PreAuthorize("hasAuthority('service:get/service/findOnes')")
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    public Result findOnes(){
        List<SysService> list = sysServiceService.findOnes();
        return Result.succeedWith(list,CodeEnum.SUCCESS.getCode(),"操作成功");
    }

    /**
     * 删除服务
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除服务")
    @PreAuthorize("hasAuthority('service:delete/service/{id}')")
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    public Result delete(@PathVariable Long id){
        try {
            sysServiceService.delete(id);
            return Result.succeedWith(null, CodeEnum.SUCCESS.getCode(),"删除服务成功");
        }catch (Exception ex){
        	log.error("SysServiceController->delete:{}" ,ex.getMessage());
        	return Result.failedWith(null,CodeEnum.ERROR.getCode(),"删除服务失败");
        }
    }

    
    @ApiOperation(value = "新增服务")
    @PostMapping("/saveOrUpdate")
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    @PreAuthorize("hasAnyAuthority('service:post/saveOrUpdate')")
    public Result saveOrUpdate(@RequestBody SysService service) {
        try{
            if (service.getId() != null){
                sysServiceService.update(service);
            }else {
                sysServiceService.save(service);
            }
            return Result.succeedWith(null, CodeEnum.SUCCESS.getCode(),"新增服务成功");
        }catch (Exception ex){
        	log.error("SysServiceController->saveOrUpdate:{}" ,ex.getMessage());
            return Result.failedWith(null,CodeEnum.ERROR.getCode(),"操作失败");
        }
    }

    /**
     * 应用服务管理
     */
    @PostMapping("/granted")
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    public Result setMenuToClient(@RequestBody SysClientDto clientDto) {
        sysServiceService.setMenuToClient(clientDto.getId(), clientDto.getServiceIds());
        return Result.succeedWith(null, CodeEnum.SUCCESS.getCode(),"操作成功");
    }

    @ApiOperation(value = "根据clientId获取对应的服务")
    @GetMapping("/services/{clientId}")
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    public Result findServicesByclientId(@PathVariable Long clientId) {
        return sysServiceService.findServicesByclientId(clientId);
    }

}
