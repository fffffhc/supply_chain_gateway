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

@Configuration
@EnableLogging
@EnableDiscoveryClient
@SpringBootApplication
@EnableApiIdempotent
@EnableFeignClients(defaultConfiguration= GolbalFeignConfig.class)
public class UserCenterApp {
	
	public static void main(String[] args) {
//		固定端口启动
//		SpringApplication.run(UserCenterApp.class, args);
		
		//随机端口启动
		SpringApplication app = new SpringApplication(UserCenterApp.class);
        app.addListeners(new PortApplicationEnvironmentPreparedEventListener());
        app.run(args);
		
	}

}
