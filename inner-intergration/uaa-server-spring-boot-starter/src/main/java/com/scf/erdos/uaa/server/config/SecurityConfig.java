package com.scf.erdos.uaa.server.config;

import com.scf.erdos.common.auth.props.PermitUrlProperties;
import com.scf.erdos.uaa.server.handle.OauthLogoutHandler;
import com.scf.erdos.uaa.server.provider.SmsCodeAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

/**
 * @Description : Spring Security配置：在WebSecurityConfigurerAdapter不拦截oauth要开放的资源
 *
 *  1，认证用户的来源【内存或数据库】
 *  2，
 *
 *
 *
 * @author：bao-clm
 * @date: 2020/3/20
 * @version：1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(PermitUrlProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired(required = false)
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OauthLogoutHandler oauthLogoutHandler;

    @Autowired
    private PermitUrlProperties permitUrlProperties;

    @Autowired
    private ValidateCodeConfig validateCodeConfig;

    @Autowired
    private SmsCodeAuthenticationProvider smsCodeAuthenticationProvider;

    /**
     * WebSecurity 全局请求忽略规则配置
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**", "/doc.html", "/login.html");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/health");
        // 忽略登录界面
        web.ignoring().antMatchers("/login.html");
        web.ignoring().antMatchers("/index.html");
        web.ignoring().antMatchers("/oauth/user/token");
        web.ignoring().antMatchers("/oauth/client/token");
        web.ignoring().antMatchers("/validata/code/**");
        web.ignoring().antMatchers("/sms/**");
        web.ignoring().antMatchers("/authentication/**");
        web.ignoring().antMatchers(permitUrlProperties.getIgnored());

    }

    /**
     * AuthenticationManager对象在OAuth2认证服务中要使用，提前放入IOC容器中。
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * HttpSecurity 具体的权限控制规则配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin().loginPage("/login.html").loginProcessingUrl("/user/login")
                .successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler);
        // 基于密码 等模式可以无session,不支持授权码模式
        if (authenticationEntryPoint != null) {
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        } else {
            // 授权码模式单独处理，需要session的支持，此模式可以支持所有oauth2的认证
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        }

        http.logout().logoutSuccessUrl("/login.html")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .addLogoutHandler(oauthLogoutHandler).clearAuthentication(true);

        // 注册到AuthenticationManager中去
        http.authenticationProvider(smsCodeAuthenticationProvider);
        //增加验证码处理
        http.apply(validateCodeConfig);
        // 解决不允许显示在iframe的问题
        http.headers().frameOptions().disable();
        http.headers().cacheControl();

    }

    /**
     * AuthenticationManagerBuilder用来配置全局的认证相关的信息，
     * 其实就是AuthenticationProvider和UserDetailsService，前者是认证服务提供者，后者是认证用户（及其权限）。
     */
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}
