
package com.scf.erdos.uaa.server;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.scf.erdos.common.feign.FeignInterceptorConfig;
import com.scf.erdos.common.auth.props.PermitUrlProperties;
import com.scf.erdos.common.rest.RestTemplateConfig;
import com.scf.erdos.uaa.server.service.RedisAuthorizationCodeServices;
import com.scf.erdos.uaa.server.service.RedisClientDetailsService;
import com.scf.erdos.uaa.server.token.RedisTemplateTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

/**
 * @Description : 配置认证服务器
 * @author：bao-clm
 * @date: 2020/3/18
 * @version：1.0
 */

@Configuration
@SuppressWarnings("all")
@Import({RestTemplateConfig.class, FeignInterceptorConfig.class})
public class UAAServerConfig {

    /**
     * 声明 ClientDetails实现
     */
    @Bean
    public RedisClientDetailsService redisClientDetailsService(DataSource dataSource , RedisTemplate<String, Object> redisTemplate ) {
        RedisClientDetailsService clientDetailsService = new RedisClientDetailsService(dataSource);
        clientDetailsService.setRedisTemplate(redisTemplate);
        return clientDetailsService;
    }

    @Bean
    public RandomValueAuthorizationCodeServices authorizationCodeServices(RedisTemplate<String, Object> redisTemplate) {
        RedisAuthorizationCodeServices redisAuthorizationCodeServices = new RedisAuthorizationCodeServices();
        redisAuthorizationCodeServices.setRedisTemplate(redisTemplate);
        return redisAuthorizationCodeServices;
    }

    /**
     * @Description : 开启认证服务器（ @EnableAuthorizationServer ）
     *
     * 大概流程：哪些客户端可以请求令牌 ---> 请求令牌路径（端点）---> 令牌发放---> 配置请求令牌发放端点安全约束
     *
     * 1，AuthorizationServerEndpointsConfigurer 用来配置令牌（token）的访问端点和令牌
     * 服务（token services）。
     *
     * 2，ClientDetailsServiceConfigurer 用来配置客户端详情服务（ClientDetailsService）,
     * 客户端详情信息在这里进行初始化，能够把客户端详情信息写死在这里或者通过数据库来存储调取
     * 详情信息。
     *
     * 3，AuthorizationServerSecurityConfigurer 用来配置令牌端点的安全约束（那些请求可以访问）。
     *
     * @author：bao-clm
     * @date: 2020/3/18
     * @version：1.0
     */
    @Component
    @Configuration
    @EnableAuthorizationServer
    @AutoConfigureAfter(AuthorizationServerEndpointsConfigurer.class)
    public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
        //授权模式专用对象
        @Autowired
        private AuthenticationManager authenticationManager;

        //认证业务对象
        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired(required = false)
        private RedisTemplateTokenStore redisTokenStore;

        @Autowired(required = false)
        private JwtTokenStore jwtTokenStore;

        @Autowired(required = false)
        private JwtAccessTokenConverter jwtAccessTokenConverter;

		@Autowired
        private WebResponseExceptionTranslator webResponseExceptionTranslator;

		//客户端来源信息
        @Autowired
        private RedisClientDetailsService redisClientDetailsService;

        @Autowired(required = false)
        private RandomValueAuthorizationCodeServices authorizationCodeServices;

        /**
         * 令牌访问端点
         * 密码模式授权 ：authenticationManager(authenticationManager)
         * 授权码模式：authorizationCodeServices(authorizationCodeServices)
         */
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            if (jwtTokenStore != null) {
                endpoints.tokenStore(jwtTokenStore)
                         .authenticationManager(authenticationManager)//密码模式需要
                        //.authorizationCodeServices(authorizationCodeServices)//授权码模式需要
                         .userDetailsService(userDetailsService);

            } else if (redisTokenStore != null) {
                endpoints.tokenStore(redisTokenStore)
                         .authenticationManager(authenticationManager)//密码模式需要
                         .userDetailsService(userDetailsService);
            }

            if (jwtAccessTokenConverter != null) {
                endpoints.accessTokenConverter(jwtAccessTokenConverter);
            }

            endpoints.authorizationCodeServices(authorizationCodeServices);
            endpoints.exceptionTranslator(webResponseExceptionTranslator);

        }

        /**
         * 配置客户端详情信息服务
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(redisClientDetailsService);
            redisClientDetailsService.loadAllClientToCache();
        }

        /**
         * 令牌访问端点安全策略
         */
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.tokenKeyAccess("permitAll()")         // oauth/token_key 提供公钥密钥的端点公开
                    .checkTokenAccess("isAuthenticated()") // oauth/check_token 检测token
                    .allowFormAuthenticationForClients();   // 表单认证，申请令牌
        }
    }

    /**
     * @Description : 资源服务器配置
     *    认证服务器也是资源服务器，
     *    所以需要资源服务器配置
     * @author：bao-clm
     * @date: 2020/4/28
     * @version：1.0
     */
    @Configuration
    @EnableResourceServer
    @EnableConfigurationProperties(PermitUrlProperties.class)
    public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private PermitUrlProperties permitUrlProperties;

        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/health");
            web.ignoring().antMatchers("/oauth/user/token");
            web.ignoring().antMatchers("/oauth/client/token");
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.requestMatcher(
                    /**
                     * 判断来源请求是否包含oauth2授权信息
                     */
                    new RequestMatcher() {
                        private AntPathMatcher antPathMatcher = new AntPathMatcher();

                        @Override
                        public boolean matches(HttpServletRequest request) {
                            // 请求参数中包含access_token参数
                            if (request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) != null) {
                                return true;
                            }

                            // 头部的Authorization值以Bearer开头
                            String auth = request.getHeader("Authorization");
                            if (auth != null) {
                                if (auth.startsWith(OAuth2AccessToken.BEARER_TYPE)) {
                                    return true;
                                }
                            }
                            if (antPathMatcher.match(request.getRequestURI(), "/oauth/userinfo")) {
                                return true;
                            }
                            if (antPathMatcher.match(request.getRequestURI(), "/oauth/remove/token")) {
                                return true;
                            }
                            if (antPathMatcher.match(request.getRequestURI(), "/oauth/get/token")) {
                                return true;
                            }
                            if (antPathMatcher.match(request.getRequestURI(), "/oauth/refresh/token")) {
                                return true;
                            }

                            if (antPathMatcher.match(request.getRequestURI(), "/oauth/token/list")) {
                                return true;
                            }

                            if (antPathMatcher.match("/clients/**", request.getRequestURI())) {
                                return true;
                            }

                            if (antPathMatcher.match("/services/**", request.getRequestURI())) {
                                return true;
                            }
                            if (antPathMatcher.match("/redis/**", request.getRequestURI())) {
                                return true;
                            }
                            return false;
                        }
                    }

            ).authorizeRequests().antMatchers(permitUrlProperties.getIgnored()).permitAll().anyRequest()
                    .authenticated();
        }
    }

}
