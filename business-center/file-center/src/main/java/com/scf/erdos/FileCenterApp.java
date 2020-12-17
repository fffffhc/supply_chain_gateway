package com.scf.erdos;

import com.scf.erdos.common.port.PortApplicationEnvironmentPreparedEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description : 文件中心
 * @author：bao-clm
 * @date: 2020/3/26
 * @version：1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class FileCenterApp {

    public static void main(String[] args) {
        // 固定端口
        // SpringApplication.run(FileCenterApp.class, args);

        // 随机端口启动
        SpringApplication app = new SpringApplication(FileCenterApp.class);
        app.addListeners(new PortApplicationEnvironmentPreparedEventListener());
        app.run(args);
    }

}