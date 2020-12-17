package com.scf.erdos.rabbitmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Coder编程
 * @version V1.0
 * @Title: RabbitMQProperties
 * @Package: com.scf.erdos.rabbitmq.config
 * @Description: 是否开启
 * @date 2019/8/25  21:02
 **/
@Data
@ConfigurationProperties(prefix = "scf.fast.rabbitmq")
public class RabbitMQProperties {

    private boolean enable;

    private String addresses;

    private String username;

    private String password;

    private String virtualHost;

    private boolean publisherConfirms = true;
}
