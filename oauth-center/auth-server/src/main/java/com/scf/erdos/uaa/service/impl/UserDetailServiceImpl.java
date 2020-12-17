package com.scf.erdos.uaa.service.impl;

import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.util.StringUtil;
import com.scf.erdos.log.annotation.LogAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scf.erdos.uaa.feign.UserFeignClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Primary
@SuppressWarnings("all")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserFeignClient  userFeignClient;

    @Override
    @LogAnnotation(module="auth-server",recordRequestParam=false)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginAppUser loginAppUser = null;

        /**
         * 登陆
         *  通过账号获取用户信息
         *  /api-auth/oauth/token
         *  2020/07/09
         */
       /* if (StringUtil.isPhone(username)){
            loginAppUser = userFeignClient.findByMobile(username);
        }else {*/
            //      后续考虑集成spring socail,支持多种类型登录
            loginAppUser = userFeignClient.findByUsername(username);
            //方式1  feign调用       对外feign resttemplate
//        loginAppUser = userLoginGrpc.findByUsername(username);		  //方式2  gprc调用		对内grpc dubbo
        //}

        if (loginAppUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }else if (StringUtil.isBlank(loginAppUser.getUsername())) {
            throw new ProviderNotFoundException("系统繁忙中");
        } else if (!loginAppUser.isEnabled()) {
             throw new DisabledException("用户已作废");
        }

        return loginAppUser;
    }


     
}
