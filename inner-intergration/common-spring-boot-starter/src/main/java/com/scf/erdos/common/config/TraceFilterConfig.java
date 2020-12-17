package com.scf.erdos.common.config;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.scf.erdos.common.filter.TraceContextFilter;


@Configuration
@SuppressWarnings("all") 
@ConditionalOnClass(WebMvcConfigurer.class)
public class TraceFilterConfig {
	
	@Bean
    public FilterRegistrationBean requestContextRepositoryFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new TraceContextFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
