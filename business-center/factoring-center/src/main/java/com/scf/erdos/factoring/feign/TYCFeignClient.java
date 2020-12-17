package com.scf.erdos.factoring.feign;

import com.scf.erdos.common.constant.TYCConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description : 天眼查工商数据同步
 * @author：bao-clm
 * @date: 2020/11/11
 * @version：1.0
 */

@FeignClient(name = "tyc-api-feign-client", url = TYCConfig.URL)
@SuppressWarnings("all")
public interface TYCFeignClient {

    /**
     * api 天眼查工商数据同步
     *
     * @param token
     * @return
     */

    @RequestMapping(method = RequestMethod.GET, value = TYCConfig.BASEINFO_2_0)
    String getTYCdata(@RequestParam("name") String name,
                      @RequestHeader("Authorization") String token);
}
