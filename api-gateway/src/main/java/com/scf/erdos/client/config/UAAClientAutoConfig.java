package com.scf.erdos.client.config;

import com.scf.erdos.client.token.AuthorizeConfigManager;
import com.scf.erdos.client.token.TokenAuthenticationConverter;
import com.scf.erdos.client.token.TokenAuthenticationManager;
import com.scf.erdos.client.handler.ResAccessDeniedHandler;
import com.scf.erdos.client.handler.ResAuthenticationEntryPoint;
import com.scf.erdos.client.handler.ResAuthenticationFailureHandler;
import com.scf.erdos.client.handler.ResAuthenticationSuccessHandler;
import com.scf.erdos.common.auth.props.PermitUrlProperties;
import com.scf.erdos.common.constant.UaaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.WebFilter;

/**
 * @Description : 配置资源服务器
 * @author：bao-clm
 * @date: 2019/12/24
 * @version：1.0
 */

@Configuration
@SuppressWarnings("all")
@EnableConfigurationProperties(PermitUrlProperties.class)
@EnableWebFluxSecurity
public class UAAClientAutoConfig {
    @Autowired
    private PermitUrlProperties permitUrlProperties;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    //自定义SecurityWebFilterChain
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        //认证处理器
        ReactiveAuthenticationManager tokenAuthenticationManager = new TokenAuthenticationManager(tokenStore);
        //认证异常处理自定处理
        ResAuthenticationEntryPoint resAuthenticationEntryPoint = new ResAuthenticationEntryPoint();
        //授权异常自定义处理
        ResAccessDeniedHandler resAccessDeniedHandler = new ResAccessDeniedHandler();

        /**
         * 构建Bearer Token
         * 请求参数强制加上 Authorization BEARER token
         *
         * http.addFilterAt(A,B);
         * 两个作用：
         * 1，将A 的 在Filter中的order 设置为与B 相同。
         * 2，将A 交给过滤器
         *
         * 其它说明：
         * 1，如果B 有实例 ；A 的实例order 会排在B 实例前面。
         */
        http.addFilterAt((WebFilter) (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = request.getQueryParams().getFirst(UaaConstant.TOKEN_PARAM);
            if ( token != null) {
                exchange.getRequest().mutate().headers(httpHeaders ->
                        httpHeaders.add(
                                UaaConstant.Authorization,
                                OAuth2AccessToken.BEARER_TYPE + " " + token)
                );
            }
            return chain.filter(exchange);
        }, SecurityWebFiltersOrder.FIRST);

        //身份认证: AuthenticationWebFilter 过滤器
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(tokenAuthenticationManager);
        //登陆验证失败
        authenticationWebFilter.setAuthenticationFailureHandler(new ResAuthenticationFailureHandler());
        //认证成功
        authenticationWebFilter.setAuthenticationSuccessHandler(new ResAuthenticationSuccessHandler());
        //通过请求头或者请求参数构建BearerTokenAuthenticationToken
        TokenAuthenticationConverter tokenAuthenticationConverter = new TokenAuthenticationConverter();
        tokenAuthenticationConverter.setAllowUriQueryParameter(true);
        authenticationWebFilter.setServerAuthenticationConverter(tokenAuthenticationConverter);


        /**
         * http.addFilterAt(A,B);
         * 两个作用：
         * 1，将A 的 在Filter中的order 设置为与B 相同。
         * 2，将A 交给过滤器
         *
         * 其它说明：如果B 有实例 ；A 的实例order 会排在B 实例前面。
         */
        http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        //访问授权
        //AuthorizationWebFilter authorizationWebFilter=new AuthorizationWebFilter(delegatingAuthorizationManager);
        //http.addFilterAt(authorizationWebFilter, SecurityWebFiltersOrder.FORM_LOGIN);

        ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange = http.authorizeExchange();
        //无需进行权限过滤的请求路径
        authorizeExchange.matchers(EndpointRequest.toAnyEndpoint()).permitAll();
        //无需进行权限过滤的请求路径
        authorizeExchange.pathMatchers(permitUrlProperties.getIgnored()).permitAll();
        authorizeExchange
                .pathMatchers(HttpMethod.OPTIONS).permitAll()    //OPTIONS 请求默认放行
                //.anyExchange().access(authorizeConfigManager)  // 应用api权限控制
                .anyExchange().authenticated()                  //token 有效性控制
                .and()
                .exceptionHandling()
                .accessDeniedHandler(resAccessDeniedHandler)
                .authenticationEntryPoint(resAuthenticationEntryPoint)
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .httpBasic().disable()
                .csrf().disable();
        return http.build();
    }

}
