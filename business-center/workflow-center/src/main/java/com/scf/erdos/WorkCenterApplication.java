package com.scf.erdos;

import com.scf.erdos.config.AppDispatcherServletConfiguration;
import com.scf.erdos.config.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 工作流中心
 *
 * @author persie
 * @date 2018/07/19
 */
@Import({
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class
})
//@SpringBootApplication
    //    (exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication(exclude={SecurityAutoConfiguration.class, OAuth2AutoConfiguration.class,
        SecurityFilterAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})

public class WorkCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkCenterApplication.class, args);
    }

}

