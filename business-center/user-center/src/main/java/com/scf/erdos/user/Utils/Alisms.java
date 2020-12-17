package com.scf.erdos.user.Utils;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Description : 发送短信
 * @author：bao-clm
 * @date: 2020/6/18
 * @version：1.0
 */

@SuppressWarnings("all")
@Slf4j
@Component
public class Alisms {

    @Autowired
    private IAcsClient acsClient;
    @Value("${aliyun.sms.sign.name}")
    private String signName;

    public void sendSms(Map<String, Object> params){

        CompletableFuture.runAsync(() -> {
            try {
                // 阿里云短信官网demo代码
                SendSmsRequest request = new SendSmsRequest();
                request.setMethod(MethodType.POST);
                request.setPhoneNumbers(params.get("phone").toString());
                request.setSignName(signName);
                request.setTemplateCode(params.get("templateCode").toString());
                request.setTemplateParam("{code:"+params.get("smsCode").toString()+"}");
                request.setOutId("100");
                SendSmsResponse response = null;
                try {
                    response = acsClient.getAcsResponse(request);
                    if (response != null) {
                        log.info("发送短信结果：code:{}，message:{}，requestId:{}，bizId:{}", response.getCode(), response.getMessage(),
                                response.getRequestId(), response.getBizId());
                    }

                } catch (ClientException e) {
                    log.error("SmsServiceImpl->sendSmsMsg:{}" ,e.getMessage());
                }
            } catch (Exception e) {
                log.error("发送短信失败：{}", e.getMessage());
            }

        });
    }


}
