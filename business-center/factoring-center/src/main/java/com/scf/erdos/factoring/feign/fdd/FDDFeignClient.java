package com.scf.erdos.factoring.feign.fdd;


import com.scf.erdos.common.constant.FddConstant;
import com.scf.erdos.factoring.model.company.FddCaApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/11/11 13:51
 */


@FeignClient(name = "fdd-api-feign-client", url = FddConstant.URL)
@SuppressWarnings("all")
public interface FDDFeignClient {

    /**
     * api 注册账号
     *
     * @param token
     * @return
     */

    @RequestMapping(method = RequestMethod.POST, value = FddConstant.REGISTERURL)
    String register(@RequestParam("app_id") String app_id,
                    @RequestParam("timestamp") String timestamp,
                    @RequestParam("v") String v,
                    @RequestParam("msg_digest") String msg_digest,
                    @RequestParam("open_id") String open_id,
                    @RequestParam("account_type") String account_type);


    /**
     * api 企业实名认证
     *
     * @param token
     * @return
     */

    @RequestMapping(method = RequestMethod.POST, value = FddConstant.COMPANY_VERIFIED )
    String companyCa(@RequestParam Map<String,Object> map) ;

    /**
     * api 个人实名认证
     *
     * @param token
     * @return
     */

    @RequestMapping(method = RequestMethod.POST, value = FddConstant.PERSON_VERIFIED)
    String personCa(@RequestParam Map<String,Object> map) ;

    /**
     * api 签章上传
     *
     * @param token
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = FddConstant.SEAL_UPLOAD)
    String sealUpload(@RequestParam Map<String,Object> map) ;

    /**
     * api 合同上传
     */
    @RequestMapping(method = RequestMethod.POST, value = FddConstant.CONTRACT_UPLOAD)
    String contractUpload(@RequestParam Map<String,Object> map) ;

    /**
     * 合同签署
     */
    @RequestMapping(method = RequestMethod.POST, value = FddConstant.EXTSIGN)
    String extsign(@RequestParam Map<String,Object> map) ;




}


