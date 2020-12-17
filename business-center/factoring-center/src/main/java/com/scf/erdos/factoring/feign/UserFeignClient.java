package com.scf.erdos.factoring.feign;

import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.feign.FeignExceptionConfig;
import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.common.model.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description : 获取用户信息
 * @author：bao-clm
 * @date: 2020/5/13
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

    @GetMapping(value = "/users-anon/userIds", params = "userIds")
    List<SysUser> findByUserIds(@RequestParam("userIds") List<String> userIds);

    @GetMapping(value = "/users-anon/userId", params = "userId")
    SysUser findByUserId(@RequestParam("userId") String userId);

    @PutMapping(value = "/users-anon/setCompanyRole")
    boolean updateCompanyUser(@RequestParam Map<String, Object> params);

    @GetMapping(value = "/users-anon/roleCode", params = "code")
    List<SysRole> getRoles(@RequestParam("code") String code);
}
