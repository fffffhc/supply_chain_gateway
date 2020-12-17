package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.service.ISignUpOnlineService;
import com.scf.erdos.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description : 在线签约
 * @author：bao-clm
 * @date: 2020/8/6
 * @version：1.0
 */

@Slf4j
@RestController
@RequestMapping("/signUpOnline")
@Api(tags = "SignUpOnline API")
public class SignUpOnlineController {

    @Autowired
    private ISignUpOnlineService iSignUpOnlineService;

    @ApiOperation(value = "获取在线合同列表-分页")
    @GetMapping("/getOnlineContracts")
    @LogAnnotation(module = "signUpOnline",recordRequestParam = false)
    public Result getOnlineContracts(@RequestParam Map<String, Object> params){
        return iSignUpOnlineService.getOnlineContracts(params);
    }

    @ApiOperation(value = "获取合同签约情况")
    @GetMapping("/getSignupInfo/{id}")
    @LogAnnotation(module = "signUpOnline",recordRequestParam = false)
    public Result getSignupInfo(@PathVariable Integer id){
        return iSignUpOnlineService.getSignupInfo(id);
    }

}
