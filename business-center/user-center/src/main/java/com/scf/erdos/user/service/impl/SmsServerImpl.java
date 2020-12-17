package com.scf.erdos.user.service.impl;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.scf.erdos.common.util.ValidatorUtil;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.user.Utils.Util;
import com.scf.erdos.user.service.ISmsServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2020/5/27
 * @version：1.0
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class SmsServerImpl implements ISmsServer {
    @Value("${aliyun.sms.expire-minute:15}")
    private Integer expireMinute;
    @Value("${aliyun.sms.day-count:5}")
    private Integer dayCount;
    @Autowired
    private IAcsClient acsClient;
    @Value("${aliyun.sms.sign.name}")
    private String signName;
    @Value("${aliyun.sms.template.code1}")
    private String templateCode1;
    @Value("${aliyun.sms.template.code2}")
    private String templateCode2;
    @Autowired
    private StringRedisTemplate redisTemplate ;

    @Override
    public String getCode(String deviceId) {
        String code = "";
        try {
            code = redisTemplate.execute(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    // redis info
                    byte[] temp = "".getBytes();
                    temp = connection.get(buildKey(deviceId).getBytes());
                    connection.close();
                    return new String(temp);
                }
            });
        } catch (Exception e) {
            throw new AuthenticationException("图片验证码不存在") {
            };
        }
        return code;
    }

    @Override
    public void remove(String deviceId) {
        redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {

                // redis info
                connection.del(buildKey(deviceId).getBytes());
                connection.close();

                return "";
            }
        });
    }

    @Override
    public Result validate(HttpServletRequest request) {
        String deviceId = request.getParameter("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            return Result.failed("请在请求参数中携带deviceId参数");
        }

        String code = this.getCode(deviceId);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request, "validCode");
        } catch (ServletRequestBindingException e) {
            return Result.failed("获取验证码的值失败");
        }
        if (StringUtils.isBlank(codeInRequest)) {
            return Result.failed("请填写验证码");
        }

        if (code == null) {
            return Result.failed("验证码不存在或已过期");
        }

        if (!StringUtils.equalsIgnoreCase(code, codeInRequest)) {
            return Result.failed("验证码不正确");
        }

        this.remove(deviceId);
        return Result.succeed("成功");
    }

    @Override
    public Result sendSmsCode(Map<String, Object> params) {
        /**
         * 图片验证码校验
         */
        String type = (String)params.get("type");
        String deviceId = (String)params.get("deviceId");
        String code = this.getCode(deviceId);

        String validCode = (String)params.get("validCode");
        if (!StringUtils.equalsIgnoreCase(code, validCode)) {
            return Result.failed("图片验证码不正确");
        }else{
            remove(deviceId);
        }

        String phone = (String)params.get("phone");
        if (!ValidatorUtil.checkPhone(phone)) {
            return Result.failed("手机号格式不正确");
        }
        /**
         * 每天同一个手机号最多发送 5次验证码
         */
        checkTodaySendCount(phone);

        String templateCode = "";
        String smsCode = "";

        /**
         * 生成随机验证码及UUID
         */
        String uuid = "sms:" + phone;
        smsCode = Util.randomCode(6);
        templateCode = templateCode1;

        /**
         * 短信验证码缓存15分钟
         */
        redisTemplate.opsForValue().set(uuid, smsCode, expireMinute,
                TimeUnit.MINUTES);
        log.info("缓存验证码：{}", smsCode);


        /**
         * 发送阿里短信验证码
         */
        String finalTemplateCode = templateCode;
        String finalSmsCode = smsCode;
        CompletableFuture.runAsync(() -> {
            try {

                // 阿里云短信官网demo代码
                SendSmsRequest request = new SendSmsRequest();
                request.setMethod(MethodType.POST);
                request.setPhoneNumbers(phone);
                request.setSignName(signName);
                request.setTemplateCode(finalTemplateCode);
                request.setTemplateParam("{code:"+finalSmsCode+"}");
                request.setOutId("100");

                SendSmsResponse response = null;
                try {
                        response = acsClient.getAcsResponse(request);
                        if (response != null) {
                            log.info("发送短信结果：code:{}，message:{}，requestId:{}，bizId:{}", response.getCode(), response.getMessage(),
                                    response.getRequestId(), response.getBizId());
                        }

                    } catch (ClientException e) {
                        log.error("SmsServiceImpl->sendSmsMsg:{}" ,e.getMessage());
                    }
            } catch (Exception e) {
                log.error("发送短信失败：{}", e.getMessage());
            }

        });

        // 当天发送验证码次数+1
        String countKey = countKey(phone);
        redisTemplate.opsForValue().increment(countKey, 1L);
        redisTemplate.expire(countKey, 1, TimeUnit.DAYS);

        return Result.succeed("发送成功");
    }

    @Override
    public Result sendInvalidCode(Map<String, Object> params) {

        String phone = (String)params.get("phone");
        if (!ValidatorUtil.checkPhone(phone)) {
            return Result.failed("手机号格式不正确");
        }
        /**
         * 每天同一个手机号最多发送 5次验证码
         */
        checkTodaySendCount(phone);

        String templateCode = templateCode2;
        String smsCode = "";
            String uuid = "sms:" + phone;
            smsCode = Util.randomCode(4);
            templateCode = templateCode2;
            /**
             * 短信验证码缓存15分钟
             */
            redisTemplate.opsForValue().set("yqm" + phone,smsCode);
            log.info("缓存验证码：{}", smsCode);

        /**
         * 发送阿里短信验证码
         */
        String finalTemplateCode = templateCode;
        String finalSmsCode = smsCode;
        CompletableFuture.runAsync(() -> {
            try {

                // 阿里云短信官网demo代码
                SendSmsRequest request = new SendSmsRequest();
                request.setMethod(MethodType.POST);
                request.setPhoneNumbers(phone);
                request.setSignName(signName);
                request.setTemplateCode(finalTemplateCode);
                request.setTemplateParam("{code:"+finalSmsCode+"}");
                request.setOutId("100");

                SendSmsResponse response = null;
                try {
                    response = acsClient.getAcsResponse(request);
                    if (response != null) {
                        log.info("发送短信结果：code:{}，message:{}，requestId:{}，bizId:{}", response.getCode(), response.getMessage(),
                                response.getRequestId(), response.getBizId());
                    }

                } catch (ClientException e) {
                    log.error("SmsServiceImpl->sendSmsMsg:{}" ,e.getMessage());
                }
            } catch (Exception e) {
                log.error("发送短信失败：{}", e.getMessage());
            }

        });

        // 当天发送验证码次数+1
        String countKey = countKey(phone);
        redisTemplate.opsForValue().increment(countKey, 1L);
        redisTemplate.expire(countKey, 1, TimeUnit.DAYS);

        return Result.succeed("发送成功");
    }

    private String countKey(String phone) {
        return "sms:count:" + LocalDate.now().toString() + ":" + phone;
    }
    private String buildKey(String deviceId) {
        return "DEFAULT_CODE_KEY:" + deviceId;
    }

    /**
     * 获取当天发送验证码次数
     * 限制号码当天次数
     * @param phone
     * @return
     */
    private void checkTodaySendCount(String phone) {
        String value =   redisTemplate.opsForValue().get(countKey(phone));
        if (value != null) {
            Integer count = Integer.valueOf(value );
            if (count > dayCount) {
                throw new IllegalArgumentException("已超过当天最大次数");
            }
        }
    }
}
