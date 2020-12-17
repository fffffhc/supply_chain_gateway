package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.service.IPayService;
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
 * @Description : 放款 Controller
 * @author：FUHAOCHENG
 * @date: 2020/09/4
 * @version：1.0
 */

@Slf4j
@RestController
@RequestMapping("/pay")
@Api(tags = "pay API")
@SuppressWarnings("all")
public class PayController {


    @Autowired
    private IPayService iPayService;

    @ApiOperation(value = "获取待放款融资列表")
    @GetMapping("/getPay")
    @LogAnnotation(module = "getPay",recordRequestParam = false)
    public Result getPendingLondInformation(@RequestParam Map<String, Object> params){
        return iPayService.getPendingLondInformation(params);
    }

    @ApiOperation(value = "获取付款单")
    @GetMapping("/getPaymentNote")
    @LogAnnotation(module = "getPaymentNote",recordRequestParam = false)
    public Result getPaymentNote(@RequestParam Map<String, Object> params){
        return iPayService.getPaymentNote(params);
    }

    @ApiOperation(value = "支付")
    @GetMapping("/rePay")
    @LogAnnotation(module = "rePay",recordRequestParam = false)
    public Result pay(@RequestParam Map<String, Object> params){
        return iPayService.pay(params);
    }


}
