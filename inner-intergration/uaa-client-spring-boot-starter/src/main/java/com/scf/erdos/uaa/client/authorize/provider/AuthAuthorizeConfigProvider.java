package com.scf.erdos.uaa.client.authorize.provider;

import com.scf.erdos.uaa.client.authorize.AuthorizeConfigProvider;
import com.scf.erdos.common.auth.props.PermitUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * @Description : 白名单处理
 * @author：bao-clm
 * @date: 2020/3/25
 * @version：1.0
 */
@Component
@Order(Integer.MAX_VALUE - 1)
@EnableConfigurationProperties(PermitUrlProperties.class)
public class AuthAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Autowired(required = false)
    private PermitUrlProperties permitUrlProperties;

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        // 免token登录设置
        config.antMatchers(permitUrlProperties.getIgnored()).permitAll();
        /**
         * 浏览器会在发送真正请求之前，先发送一个方法为OPTIONS的预检请求 Preflighted requests
         * 这个请求是用来验证本次请求是否安全的。所以OPTIONS 放行。
         */
        config.antMatchers(HttpMethod.OPTIONS).permitAll();
        return true;
    }

}
