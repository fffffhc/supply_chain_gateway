package com.scf.erdos.factoring.feign.fdd;

import com.scf.erdos.common.constant.FddConstant;
import com.scf.erdos.common.util.ApiSign;
import com.scf.erdos.factoring.model.company.FddCaApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.security.DigestException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/11/13 14:08
 */

@Service
public class FDDFeignClientImpl {

    @Autowired
    private FDDFeignClient fddFeignClient;

    static final String app_id = FddConstant.APP_ID;//接入方的ID
    static final String app_secret = FddConstant.APP_SECRET;//接入方的密钥
    static final String v = FddConstant.V;//接口版本号
    static final String timestamp = String.valueOf(System.currentTimeMillis());

    /**
     * 法大大 用户注册
     *
     * @param userId
     * @return
     * @throws DigestException
     */
    public String fddRegister(String userId, String account_type) {
        String result = "";
        String msg_digest = "";
        try{
            Map<String, Object> map = new HashMap<>();
            map.put("open_id", userId);//接入方给用户定义的唯一标识（用户id）
            map.put("account_type", account_type);//1:个人；2:企业

            msg_digest = ApiSign.fddParam(app_id, timestamp, app_secret, map);//签名
            result = fddFeignClient.register(app_id, timestamp, v, msg_digest, userId, account_type);//调用法大大注册接口
        }catch (Exception e){
            e.getMessage();
        }finally {
            System.out.println(msg_digest);
            return result;
        }

    }

    /**
     * 企业CA认证
     *
     * @param customer_id
     * @return
     * @throws DigestException
     */
    public String fddCompanyCa(String customer_id){
            Map<String, Object> map = new HashMap<>();
                map.put("customer_id", customer_id);
                map.put("page_modify", FddConstant.PAGE_MODIFY_2);
                map.put("notify_url", FddConstant.FDD_CA_AUTH_CALLBACK);
                map.put("msg_digest", ApiSign.fddParam(app_id, timestamp, app_secret, map));//签名
                map.put("app_id", app_id);
                map.put("timestamp", timestamp);
                map.put("v", v);
            return fddFeignClient.companyCa(map);
    }


    /**
     * 个人CA认证
     *
     * @param customer_id
     * @return
     * @throws DigestException
     */
    public String fddPersonCa(String customer_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("customer_id", customer_id);
        map.put("verified_way", FddConstant.U_VERIFIED_WAY_0);
        map.put("page_modify",FddConstant.PAGE_MODIFY_2);
        map.put("notify_url", FddConstant.FDD_CA_AUTH_CALLBACK);
        map.put("msg_digest", ApiSign.fddParam(app_id, timestamp, app_secret, map));

        map.put("app_id", app_id);
        map.put("timestamp", timestamp);
        map.put("v", v);

        return fddFeignClient.personCa(map);

    }

    /**
     * 签章上传
     *
     * @param
     * @return
     * @throws DigestException
     */
    public String sealUpload(String customerId,String signatureImgBase64){
        Map<String,Object> map = new HashMap<>();
        map.put("customer_id", customerId);
        map.put("signature_img_base64", signatureImgBase64);
        map.put("msg_digest", ApiSign.fddParam(app_id, timestamp, app_secret, map));

        map.put("app_id", app_id);
        map.put("timestamp", timestamp);
        map.put("v", v);
        return fddFeignClient.sealUpload(map);
    }

    /**
     * 合同上传
     */
    public String contractUpload(String contractId, String docTitle, String docUrl, String docType){
        Map<String, Object> map = new HashMap<>();
        map.put("contract_id", contractId);
        map.put("msg_digest", ApiSign.fddParam(app_id, timestamp, app_secret, map));

        map.put("doc_title", docTitle);
        map.put("doc_url",docUrl);
        map.put("doc_type",docType);
        map.put("app_id", app_id);
        map.put("timestamp", timestamp);
        map.put("v", v);
        return fddFeignClient.contractUpload(map);
    }

    /**
     * 合同签署
     */
    public String extsign(String transactionId,String contractId,String customerId,String docTitle,String signKeyword,String returnUrl){
        Map<String, Object> map = new HashMap<>();
        map.put("transaction_id",transactionId);
        map.put("contract_id",contractId);
        map.put("customer_id",customerId);
        map.put("doc_title",docTitle);
        map.put("sign_keyword",signKeyword);
        map.put("return_url",returnUrl);
        map.put("msg_digest", ApiSign.fddParam(app_id, timestamp, app_secret, map));

        map.put("app_id", app_id);
        map.put("timestamp", timestamp);
        map.put("v", v);
        return fddFeignClient.extsign(map);
    }




}
