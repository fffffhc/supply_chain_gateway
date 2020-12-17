package com.scf.erdos.user.Utils;

import java.util.Random;

/**
 * @Description : 短信验证码生成器
 * @author：bao-clm
 * @date: 2020/5/27
 * @version：1.0
 */
public class Util {
    private static String[] NUMBERS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    private static Random RANDOM = new Random();

    /**
     * 生成length位随机数值字符串
     *
     * @param length
     * @return
     */
    public static String randomCode(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(NUMBERS[RANDOM.nextInt(NUMBERS.length)]);
        }

        return builder.toString();
    }
}
