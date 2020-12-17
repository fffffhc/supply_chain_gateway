package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.workflow.CompanyWorkflow;
import com.scf.erdos.factoring.service.IWorkflowService;
import com.scf.erdos.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description : 自定义工作流
 * @author：bao-clm
 * @date: 2020/5/19
 * @version：1.0
 */

@Slf4j
@RestController
@RequestMapping("/workflow")
@Api(tags = "WORKFLOW API")
public class WorkflowController {

    @Autowired
    private IWorkflowService iWorkflowService;

    @ApiOperation(value = "保存工作流详情")
    @PostMapping("/add")
    @LogAnnotation(module = "workflow",recordRequestParam = false)
    public Result save(@RequestBody(required=false) CompanyWorkflow companyWorkflow){
        return iWorkflowService.add(companyWorkflow);
    }

    @ApiOperation(value = "修改工作流详情")
    @PutMapping("/update")
    @LogAnnotation(module = "workflow",recordRequestParam = false)
    public Result update(@RequestBody(required=false) CompanyWorkflow companyWorkflow){
        return iWorkflowService.update(companyWorkflow);
    }

    @ApiOperation(value = "删除工作流详情")
    @DeleteMapping("/delete")
    @LogAnnotation(module = "workflow",recordRequestParam = false)
    public Result delete(@RequestParam String id){
        return iWorkflowService.delete(id);
    }

    @ApiOperation(value = "获取工作流-分页")
    @GetMapping("/getWorkflows")
    @LogAnnotation(module = "workflow",recordRequestParam = false)
    public Result getWorkflows(@RequestParam Map<String, Object> params){
        return iWorkflowService.getWorkflows(params);
    }

    @ApiOperation(value = "获取工作流详情")
    @GetMapping("/getWorkflowInfo")
    @LogAnnotation(module = "workflow",recordRequestParam = false)
    public Result getWorkflowInfo(@RequestParam Map<String, Object> params){
        return iWorkflowService.getWorkflowInfo(params);
    }

    @ApiOperation(value = "启用/停用工作流")
    @PutMapping("/handleWork")
    @LogAnnotation(module = "workflow",recordRequestParam = false)
    public Result handleWork(@RequestParam Map<String, Object> params){
        return iWorkflowService.handleWork(params);
    }

    @ApiOperation(value = "获取资金机构角色")
    @GetMapping("/getRoles")
    @LogAnnotation(module = "workflow",recordRequestParam = false)
    public Result getRoles(@RequestParam Map<String, Object> params){
        return iWorkflowService.getRoles(params);
    }

}
