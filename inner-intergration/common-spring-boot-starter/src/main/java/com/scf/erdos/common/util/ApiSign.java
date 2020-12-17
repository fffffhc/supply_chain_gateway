package com.scf.erdos.common.util;

import java.security.DigestException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/11/12 11:51
 * <p>
 * API 签名获取
 */
public class ApiSign {

    /**
     * fdd 接口调用 签名摘要 加密方法
     *
     * @param app_id
     * @param timestamp
     * @param app_secret
     * @return
     *
     *  Base64(SHA1 ( app_id + MD5 ( timestamp)
     *                                     +SHA1(app_secret+
     *                                     account_type+open_id)
     *                     )
     *             )
     *  业务参数：account_type+open_id
     *
     */
    public static String fddParam(String app_id, String timestamp, String app_secret, Map<String, Object> map) {
        byte[] bytes = new byte[]{};
        try {
            String ASCIISortparams = ParamsASCIISort.getOrderByLexicographic(map); //获取业务数据ASCII 排序后的结果
            String param = Encryption.SHA1(app_id
                    + Encryption.UpperMD5(timestamp)
                    + Encryption.SHA1(app_secret + ASCIISortparams));
            bytes = param.getBytes();

        } catch (DigestException e) {
            e.getMessage();
        } finally {
            return Base64.getEncoder().encodeToString(bytes);
        }

    }

}
