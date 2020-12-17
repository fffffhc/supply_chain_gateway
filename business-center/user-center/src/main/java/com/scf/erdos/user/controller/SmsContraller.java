package com.scf.erdos.user.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.user.service.ISmsServer;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description : 短信验证
 * @author：bao-clm
 * @date: 2020/5/27
 * @version：1.0
 */

@Slf4j
@RestController
@Api(tags = "SMS API")
@SuppressWarnings("all")
public class SmsContraller {

    @Autowired
    private ISmsServer iValidateServer;

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @PostMapping(value = "/sms/sendSmsCode")
    public Result sendSmsCode(@RequestParam Map<String, Object> params) {
        return iValidateServer.sendSmsCode(params);
    }

    @PostMapping(value = "/sms/sendInvalidCode")
    public Result sendInvalidCode(@RequestParam Map<String, Object> params) {
        return iValidateServer.sendInvalidCode(params);
    }
}
