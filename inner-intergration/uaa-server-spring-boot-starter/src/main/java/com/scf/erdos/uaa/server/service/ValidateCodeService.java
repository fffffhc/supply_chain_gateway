package com.scf.erdos.uaa.server.service;

import com.scf.erdos.common.web.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description : 图片验证码处理 服务
 * @author：bao-clm
 * @date: 2020/3/18
 * @version：1.0
 */
public interface ValidateCodeService {
    /**
     * 保存图形验证码
     *
     * @param deviceId  前端唯一标识
     * @param imageCode 验证码
     */
    void saveImageCode(String deviceId, String imageCode);

    /**
     * 获取验证码
     *
     * @param deviceId 前端唯一标识/手机号
     */
    String getCode(String deviceId);
    String getCode1(String deviceId);

    /**
     * 删除验证码
     *
     * @param deviceId 前端唯一标识/手机号
     */
    void remove(String deviceId);

    /**
     * 验证验证码
     */
    void validate(HttpServletRequest request);
}
