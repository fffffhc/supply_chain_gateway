package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.credit.ProductCredit;
import com.scf.erdos.factoring.service.ICredictService;
import com.scf.erdos.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description : 授信管理 controller
 * @author：bao-clm
 * @date: 2020/6/8
 * @version：1.0
 */

@Slf4j
@RestController
@RequestMapping("/credit")
@Api(tags = "CREDIT API")
public class CreditController {

    @Autowired
    private ICredictService iCredictService;

    @ApiOperation(value = "授信申请")
    @PostMapping("/apply")
    @LogAnnotation(module = "credict",recordRequestParam = false)
    public Result apply(@RequestParam Map<String, Object> params){
        return iCredictService.apply(params);
    }

    @ApiOperation(value = "获取授信申请列表")
    @GetMapping("/getCredicts")
    @LogAnnotation(module = "credict",recordRequestParam = false)
    public Result getCredicts(@RequestParam Map<String, Object> params){
        return iCredictService.getCredicts(params);
    }

    @ApiOperation(value = "获取授信申请详情")
    @GetMapping("/getCreditInfo")
    @LogAnnotation(module = "credict",recordRequestParam = false)
    public Result getCreditInfo(@RequestParam Map<String, Object> params){
        return iCredictService.getCreditInfo(params);
    }

    @ApiOperation(value = "审批授信申请")
    @PostMapping("/audit")
    @LogAnnotation(module = "credict",recordRequestParam = false)
    public Result add(@Validated ProductCredit productCredit){
        return iCredictService.audit(productCredit);
    }
}
