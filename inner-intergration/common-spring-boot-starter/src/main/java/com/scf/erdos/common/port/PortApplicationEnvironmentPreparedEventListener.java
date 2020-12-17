package com.scf.erdos.common.port;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import com.scf.erdos.common.port.props.RandomServerPortPropertySource;

/**
 * @Description : 用于监听随机端口问题
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
public class PortApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        event.getEnvironment().getPropertySources().addLast(new RandomServerPortPropertySource());
    }
}
