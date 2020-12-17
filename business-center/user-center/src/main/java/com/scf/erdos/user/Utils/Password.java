package com.scf.erdos.user.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2020/7/7
 * @version：1.0
 */
public class Password {




    public static void main(String[] args) {
        BCryptPasswordEncoder n = new BCryptPasswordEncoder();
        System.out.println(n.encode("Aa123456"));
    }

}
