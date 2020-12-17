package com.scf.erdos.factoring.controller.api;


import com.scf.erdos.common.constant.TYCConfig;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.feign.TYCFeignClient;
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
 * @date 2020/11/10 14:30
 */

@Slf4j
@RestController
@RequestMapping("/tyc")
@Api(tags = "TYF INTERFACE API")
@SuppressWarnings("all")
public class TYCController {

    @Autowired
    private TYCFeignClient tycFeignClient;

    @ApiOperation(value = "TYC 数据同步")
    @GetMapping("/getTYCdata")
    @LogAnnotation(module = "getTYCdata",recordRequestParam = false)
    public Result getTYCdata(@RequestParam Map<String, Object> params){

        String name = "中航重机股份有限公司";
        String token = TYCConfig.TOKEN;
        String str = tycFeignClient.getTYCdata(name,token);

        System.out.println(str);
        return null;
    }

}
