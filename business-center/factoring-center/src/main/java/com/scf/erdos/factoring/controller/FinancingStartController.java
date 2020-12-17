package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.financing.Financing;
import com.scf.erdos.factoring.model.financing.FinancingSave;
import com.scf.erdos.factoring.service.IFinancingStartService;
import com.scf.erdos.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description : 融资申请发起
 * @author：bao-clm
 * @date: 2020/7/29
 * @version：1.0
 */

@Slf4j
@RestController
@RequestMapping("/financingStart")
@Api(tags = "financingStart API")
public class FinancingStartController {

    @Autowired
    private IFinancingStartService iFinancingStartService;

    @ApiOperation(value = "获取授信额度情况")
    @GetMapping("/getProductCredit")
    @LogAnnotation(module = "financingStart",recordRequestParam = false)
    public Result getProductCredit(@RequestParam Map<String, Object> params){
        return iFinancingStartService.getProductCredit(params);
    }

    @ApiOperation(value = "获取融资产品")
    @GetMapping("/getProducts")
    @LogAnnotation(module = "financingStart",recordRequestParam = false)
    public Result getProducts(@RequestParam Map<String, Object> params){
        return iFinancingStartService.getProducts(params);
    }

    @ApiOperation(value = "融资申请")
    @GetMapping("/applyFinancing")
    @LogAnnotation(module = "financingStart",recordRequestParam = false)
    public Result applyFinancing(@RequestParam Map<String, Object> params){
        return iFinancingStartService.applyFinancing(params);
    }

    @ApiOperation(value = "提交融资申请")
    @PostMapping("/save")
    @LogAnnotation(module = "financingStart",recordRequestParam = false)
    public Result save(FinancingSave financingSave){
        return iFinancingStartService.save(financingSave);
    }

    @ApiOperation(value = "获取买方企业列表")
    @GetMapping("/getBuyerCompanys")
    @LogAnnotation(module = "financingStart",recordRequestParam = false)
    public Result getBuyerCompanys(@RequestParam Map<String, Object> params){
        return iFinancingStartService.getBuyerCompanys(params);
    }

}
