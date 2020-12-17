package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.service.IRepayService;
import com.scf.erdos.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/9/23 17:08
 */

@Slf4j
@RestController
@RequestMapping("/repay")
@Api(tags = "Repay API")
@SuppressWarnings("all")
public class RepayController {


    @Autowired
    private IRepayService iRepayService;

    @ApiOperation(value = "获取代还款融资列表")
    @GetMapping("/getRepay")
    @LogAnnotation(module = "pay",recordRequestParam = false)
    public Result getPendingRepaymentInformation(@RequestParam Map<String, Object> params){
        return iRepayService.getPendingRepaymentInformation(params);
    }
}
