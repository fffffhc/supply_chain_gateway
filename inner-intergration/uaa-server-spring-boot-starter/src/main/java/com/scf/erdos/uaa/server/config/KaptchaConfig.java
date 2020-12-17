package com.scf.erdos.uaa.server.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Description : Kaptcha 生成验证码配置
 * @author：bao-clm
 * @date: 2020/3/18
 * @version：1.0
 */
@Configuration
public class KaptchaConfig {
    private static final String KAPTCHA_BORDER = "kaptcha.border";
    private static final String KAPTCHA_BORDER_COLOR = "kaptcha.border.color";
    private static final String KAPTCHA_TEXTPRODUCER_FONT_COLOR = "kaptcha.textproducer.font.color";
    private static final String KAPTCHA_TEXTPRODUCER_CHAR_SPACE = "kaptcha.textproducer.char.space";
    private static final String KAPTCHA_IMAGE_WIDTH = "kaptcha.image.width";
    private static final String KAPTCHA_IMAGE_HEIGHT = "kaptcha.image.height";
    private static final String KAPTCHA_TEXTPRODUCER_CHAR_LENGTH = "kaptcha.textproducer.char.length";
    private static final Object KAPTCHA_IMAGE_FONT_SIZE = "kaptcha.textproducer.font.size";
    private static final String KAPTCHA_TEXTPRODUCER_CHAR_STRING = "kaptcha.textproducer.char.string";
    private static final String KAPTCHA_BACKGROUND_CLEAR_FROM = "kaptcha.background.clear.from";
    private static final String KAPTCHA_NOISE_IMPL = "kaptcha.noise.impl";
    private static final String KAPTCHA_NOISE_COLOR = "kaptcha.noise.color";

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put(KAPTCHA_BORDER, "yes");
        properties.put(KAPTCHA_BORDER_COLOR,"white");
        properties.put(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "64,158,255");
        properties.put(KAPTCHA_NOISE_COLOR, "64,158,255");
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, 5);
        properties.put(KAPTCHA_IMAGE_WIDTH, "120");
        properties.put(KAPTCHA_IMAGE_HEIGHT, "38");
        properties.put(KAPTCHA_IMAGE_FONT_SIZE, "30");
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_STRING,"abcde2345678gfynmnpwx");
        properties.put(KAPTCHA_BACKGROUND_CLEAR_FROM,"white");
        properties.put(KAPTCHA_NOISE_IMPL,"com.google.code.kaptcha.impl.NoNoise");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
