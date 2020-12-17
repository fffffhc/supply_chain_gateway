package com.scf.erdos;

import com.scf.erdos.common.annotation.EnableApiIdempotent;
import com.scf.erdos.common.feign.GolbalFeignConfig;
import com.scf.erdos.common.port.PortApplicationEnvironmentPreparedEventListener;
import com.scf.erdos.log.annotation.EnableLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @Description : 保理系统启动类
 * @author：bao-clm
 * @date: 2020/5/11
 * @version：1.0
 */

@Configuration
@EnableLogging
@EnableDiscoveryClient
@SpringBootApplication
@EnableApiIdempotent
@EnableFeignClients(defaultConfiguration= GolbalFeignConfig.class)
public class FactoringCenterApp {
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(FactoringCenterApp.class);
        app.addListeners(new PortApplicationEnvironmentPreparedEventListener());
        app.run(args);

    }
}
