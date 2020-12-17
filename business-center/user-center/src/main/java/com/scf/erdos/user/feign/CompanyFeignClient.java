package com.scf.erdos.user.feign;


import com.scf.erdos.common.feign.FeignExceptionConfig;
import com.scf.erdos.common.model.CompanyInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "factoring-center", configuration = FeignExceptionConfig.class)
public interface CompanyFeignClient {

    @GetMapping(value = "/company/getCompanyInfo/userId", params = "userId")
    CompanyInfoVo findConpanyInfoByUserId(@RequestParam("userId") Long userId);
}
