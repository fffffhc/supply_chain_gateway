package com.scf.erdos.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */

@EnableEurekaServer
@SpringBootApplication
//@EnableHystrixDashboard
//@EnableTurbine
public class EurekaServerApp {

	public static void main(String[] args) {

//    	1本地启动采用此方法加载profiles文件
//    	ConfigurableApplicationContext context = new SpringApplicationBuilder(EurekaServerApp.class).
//				profiles("slave0").run(args);
		SpringApplication.run(EurekaServerApp.class, args);
//    	2服务器采用此方法 java -jar   --spring.profiles.active=slave3;
//    	 SpringApplication.run(EurekaServerApp.class, args);

	}

}