package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.financing.Financing;
import com.scf.erdos.factoring.service.IFinancingService;
import com.scf.erdos.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description : 融资管理 Controller
 * @author：bao-clm
 * @date: 2020/6/8
 * @version：1.0
 */

@Slf4j
@RestController
@RequestMapping("/financing")
@Api(tags = "FINANCING API")
public class FinancingController {

    @Autowired
    private IFinancingService iFinancingService;

    @ApiOperation(value = "获取融资列表")
    @GetMapping("/getFinancings")
    @LogAnnotation(module = "financing",recordRequestParam = false)
    public Result getFinancings(@RequestParam Map<String, Object> params){
        return iFinancingService.getFinancings(params);
    }

    @ApiOperation(value = "获取融资详情")
    @GetMapping("/getFinancingInfo/{id}")
    @LogAnnotation(module = "financing",recordRequestParam = false)
    public Result getFinancingInfo(@PathVariable Integer id){
        return iFinancingService.getFinancingInfo(id);
    }

    @ApiOperation(value = "获取客户基础信息")
    @GetMapping("/getCustomerInfo/{id}")
    @LogAnnotation(module = "financing",recordRequestParam = false)
    public Result getCustomerInfo(@PathVariable Integer id){
        return iFinancingService.getCustomerInfo(id);
    }

    @ApiOperation(value = "决策意见书")
    @GetMapping("/financeingReview/{id}")
    @LogAnnotation(module = "financing",recordRequestParam = false)
    public Result financeingReview(@PathVariable Integer id){
        return iFinancingService.financeingReview(id);
    }

    @ApiOperation(value = "融资申请审批")
    @PostMapping("/audit")
    @LogAnnotation(module = "audit",recordRequestParam = false)
    public Result audit(@RequestBody Financing financing){
        return iFinancingService.audit(financing);
    }

    @ApiOperation(value = "融资申请驳回")
    @PostMapping("/reject")
    @LogAnnotation(module = "reject",recordRequestParam = false)
    public Result reject(Financing financing){
        return iFinancingService.reject(financing);
    }

    @ApiOperation(value = "融资申请驳回")
    @PostMapping("/ht")
    @LogAnnotation(module = "reject",recordRequestParam = false)
    public Result ht(){
        return iFinancingService.ht();
    }
}
