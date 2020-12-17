package com.scf.erdos.client.handler;

import java.nio.charset.Charset;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @Description : 认证异常处理自定义异常
 *              HTTP 401 错误 - 未授权(Unauthorized) ：
 *              一般来说该错误消息表明首先需要登录（输入有效的用户名和密码）。
 *             如果输入这些信息，立刻就看到一个 401 错误，就意味着，无论出于何种原因您的用户名和密码其中之
 *             一或两者都无效（输入有误，用户名暂时停用，账户被锁定，凭证失效等） 。
 *
 * @author：bao-clm
 * @date: 2019/1/12
 * @version：1.0
 */
@Slf4j
@SuppressWarnings("all")
public class ResAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        JSONObject message = new JSONObject();
        message.put("data",null);
        message.put("resp_code", HttpStatus.UNAUTHORIZED.value());
        message.put("resp_msg", "无效的token");

        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setAccessControlAllowCredentials(true);
        response.getHeaders().setAccessControlAllowOrigin("*");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer buffer = dataBufferFactory.wrap(message.toJSONString().getBytes(Charset.defaultCharset()));
        return response.writeWith(Mono.just(buffer)).doOnError((error) -> {
            DataBufferUtils.release(buffer);
        });
    }
}
