package com.scf.erdos.uaa.feign;

import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.feign.FeignExceptionConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description : 调用用户中心中的userdetail对象，用户oauth中的登录，获取的用户与页面输入的密码 进行BCryptPasswordEncoder匹配
 * @author：bao-clm
 * @date: 2019/1/24
 * @version：1.0
 */
@FeignClient(value = "user-center", configuration = FeignExceptionConfig.class)
public interface UserFeignClient {

    /**
     * feign rpc访问远程/users-anon/login接口
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/users-anon/login", params = "username")
    LoginAppUser findByUsername(@RequestParam("username") String username);

    @GetMapping(value = "/users-anon/mobile", params = "mobile")
    LoginAppUser findByMobile(@RequestParam("mobile") String mobile);

}
