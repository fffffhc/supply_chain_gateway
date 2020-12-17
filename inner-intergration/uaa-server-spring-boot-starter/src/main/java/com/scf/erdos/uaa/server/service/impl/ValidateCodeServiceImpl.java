package com.scf.erdos.uaa.server.service.impl;

import javax.servlet.http.HttpServletRequest;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.uaa.server.service.ValidateCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * @Description : 验证生成接口实现类
 * @author：bao-clm
 * @date: 2020/3/20
 * @version：1.0
 */
@Service
@SuppressWarnings("all")
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate1 ;

    /**
     * 保存用户验证码，和randomStr绑定
     *
     * @param deviceId  客户端生成
     * @param imageCode 验证码信息
     */
    @Override
    public void saveImageCode(String deviceId, String imageCode) {

        String text = imageCode.toLowerCase().toString();

        redisTemplate.execute(new RedisCallback<String>() {

            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {

                // redis info
                connection.set(buildKey(deviceId).getBytes(), imageCode.getBytes());
                // 过去时间 5分钟
                connection.expire(buildKey(deviceId).getBytes(), 60L * 5L);
                connection.close();
                return "";
            }
        });

    }

    /**
     * 获取验证码
     *
     * @param deviceId 前端唯一标识/手机号
     */

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
            throw new AuthenticationException("验证码不存在") {
            };
        }

        return code;

    }


    /**
     * 获取验证码
     *
     * @param deviceId 前端唯一标识/手机号
     */

    @Override
    public String getCode1(String deviceId) {

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
            throw new AuthenticationException("邀请码不存在") {
            };
        }

        return code;

    }

    /**
     * 删除验证码
     *
     * @param deviceId 前端唯一标识/手机号
     */
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

    /**
     * 验证验证码
     * 在 AuthenticationException 放验证码错误 msg
     *  msg = validateExcepton  代表验证码错误
     *  com.scf.erdos.uaa.server.config.SecurityHandlerConfig 类 loginFailureHandler方法
     *  判断并返回前端。
     */
    @Override
    public void validate(HttpServletRequest request) {
        String deviceId = request.getParameter("deviceId");
        String invalidCode = request.getParameter("invalidCode");//邀请码
        String username = request.getParameter("username");
        /**
         * 校验邀请码
         */
        boolean bl = redisTemplate.hasKey("yqm" + username);

        if(bl){

            if(StringUtils.isBlank(invalidCode)){
                 throw new AuthenticationException("FirstLogin") {};
            }else{
                String code =   redisTemplate1.opsForValue().get("yqm" + username);
                if(!code.equals(invalidCode)){
                    throw new AuthenticationException("invalidCodeExcepton") {
                    };
                }else{
                    redisTemplate.delete("yqm" + username);
                }
            }


        }else{
            if (StringUtils.isBlank(deviceId)) {
                throw new AuthenticationException("请在请求参数中携带deviceId参数") {
                };
            }
            String code = this.getCode(deviceId);
            String codeInRequest;
            try {
                codeInRequest = ServletRequestUtils.getStringParameter(request, "validCode");
            } catch (ServletRequestBindingException e) {
                throw new AuthenticationException("validateExcepton") {
                };
            }
            if (StringUtils.isBlank(codeInRequest)) {
                throw new AuthenticationException("validateExcepton") {
                };
            }

            if (code == null) {
                throw new AuthenticationException("validateExcepton") {
                };
            }

            if (!StringUtils.equalsIgnoreCase(code, codeInRequest)) {
                throw new AuthenticationException("validateExcepton") {
                };
            }
            this.remove(deviceId);
        }

    }

    private String buildKey(String deviceId) {
        return "DEFAULT_CODE_KEY:" + deviceId;
    }
}
