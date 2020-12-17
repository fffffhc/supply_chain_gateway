package com.scf.erdos.uaa.client;

import javax.annotation.Resource;

import com.scf.erdos.common.feign.FeignInterceptorConfig;
import com.scf.erdos.common.feign.GolbalFeignConfig;
import com.scf.erdos.common.auth.props.PermitUrlProperties;
import com.scf.erdos.common.rest.RestTemplateConfig;
import com.scf.erdos.uaa.client.authorize.AuthorizeConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description : 资源服务器配置
 * @author：bao-clm
 * @date: 2020/3/25
 * @version：1.0
 */
@Component
@Configuration
@EnableResourceServer
@AutoConfigureAfter(TokenStore.class)
@EnableConfigurationProperties(PermitUrlProperties.class)
@Import({RestTemplateConfig.class, FeignInterceptorConfig.class})
@EnableFeignClients(defaultConfiguration = GolbalFeignConfig.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UAAClientAutoConfig extends ResourceServerConfigurerAdapter {

    /**
     * 对应oauth_client_details的 resource_ids字段 如果表中有数据
     * client_id只能访问响应resource_ids的资源服务器
     */
    private static final String DEMO_RESOURCE_ID = "";

    @Resource
    private ObjectMapper objectMapper; // springmvc启动时自动装配json处理类

    @Resource
    private TokenStore redisTokenStore;

    @Autowired(required = false)
    private JwtTokenStore jwtTokenStore;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Autowired
    private OAuth2WebSecurityExpressionHandler expressionHandler;

    @Autowired
    private OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler;

    @Autowired
    private PermitUrlProperties permitUrlProperties;

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(permitUrlProperties.getIgnored());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        if (jwtTokenStore != null) {
            resources.tokenStore(jwtTokenStore);
        } else if (redisTokenStore != null) {
            resources.tokenStore(redisTokenStore);
        }
        resources.stateless(true);
        resources.authenticationEntryPoint(authenticationEntryPoint);
        resources.expressionHandler(expressionHandler);
        resources.accessDeniedHandler(oAuth2AccessDeniedHandler);

    }

    /**
     * 通过重载，配置如何通过拦截器保护请求
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        authorizeConfigManager.config(http.authorizeRequests());
    }

}
