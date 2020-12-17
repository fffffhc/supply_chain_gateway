package com.scf.erdos.uaa.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Description : 装配密码匹配器
 *
 * Bcrypt 有四个变量：
 * 1.saltRounds: 正数，代表hash杂凑次数，数值越高越安全，默认10次。
 * 2.myPassword: 明文密码字符串。
 * 3.salt: 盐，一个128bits随机字符串，22字符。
 * 4.myHash: 经过明文密码password和盐salt进行hash，个人的理解是默认10次下 ，
 * 循环加盐hash10次，得到myHash。
 *
 * 每次明文字符串myPassword过来，就通过10次循环加盐salt加密后得到myHash,
 * 然后拼接BCrypt版本号+salt盐+myHash等到最终的bcrypt密码 ，存入数据库中。
 *
 * 例如：数据库加密后的密码 = $2a$10$Wtw81uu43fGKw9lkOr1RAOTNWxQIZBsB3YDwc/5yDnr/yeG5x92EG
 *  Bcrypt  $2a$
 *  Rounds  10
 *  Salt    Wtw81uu43fGKw9lkOr1RAO
 *  Hash    TNWxQIZBsB3YDwc/5yDnr/yeG5x92EG
 *
 * 那如果黑客使用彩虹表进行hash碰撞呢?
 * 有文章指出bcrypt一个密码出来的时间比较长，需要0.3秒，而MD5只需要一微秒（百万分之一秒），
 * 一个40秒可以穷举得到明文的MD5，在bcrypt需要12年，时间成本太高。
 *
 *  穷举法
 *  彩虹表
 *
 * @author：bao-clm
 * @date: 2020/3/18
 * @version：1.0
 */
@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
