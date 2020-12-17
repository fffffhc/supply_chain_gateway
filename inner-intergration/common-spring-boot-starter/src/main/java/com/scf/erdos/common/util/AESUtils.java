package com.scf.erdos.common.util;


import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description : aes 可逆性加密算法
 * @author：bao-clm
 * @date: 2020/3/25
 * @version：1.0
 */

@Slf4j
@SuppressWarnings("all")
public class AESUtils {
    private static final String AES = "AES";
    private static final String CHAR_SET_NAME1 = "UTF-8";
    private static final String CHAR_SET_NAME2 = "ASCII";
    private static final String CIPHER_KEY = "AES/CBC/PKCS5Padding";

    /**
     * 偏移量 ：可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private static final String IV_PARAMETER = "#sd93.s_@56434!2";
    /**
     * 密钥 ：可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，需要为16位。
     */
    private static final String S_KEY = "!d.e9d0o#4!didk.";


    /**
     * 加密
     *
     * @param param
     * @return
     * @throws Exception
     */
    public static String encryption(String param) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_KEY);
            SecretKeySpec skeySpec = new SecretKeySpec(S_KEY.getBytes(), AES);
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            // 此处使用BASE64做转码。
            return new BASE64Encoder().encode(cipher.doFinal(param.getBytes(CHAR_SET_NAME1)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密
     *
     * @param value
     * @return
     * @throws Exception
     */
    public static String decrypt(String value) throws Exception {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(S_KEY.getBytes(CHAR_SET_NAME2), AES);
            Cipher cipher = Cipher.getInstance(CIPHER_KEY);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            return new String(cipher.doFinal(new BASE64Decoder().decodeBuffer(value)), CHAR_SET_NAME1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
