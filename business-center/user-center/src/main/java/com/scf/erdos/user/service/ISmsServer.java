package com.scf.erdos.user.service;

import com.scf.erdos.common.web.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ISmsServer {

    /**
     * 获取验证码
     *
     * @param deviceId 前端唯一标识/手机号
     */
    String getCode(String deviceId);

    /**
     * 删除验证码
     *
     * @param deviceId 前端唯一标识/手机号
     */
    void remove(String deviceId);

    /**
     * 验证验证码
     */
    Result validate(HttpServletRequest request);

    /**
     * 发送短信验证码
     */
    Result sendSmsCode(Map<String, Object> params);

    /**
     * 发送邀请码
     */
    Result sendInvalidCode(Map<String, Object> params);
}
