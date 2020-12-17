package com.scf.erdos.client.filter;

import com.scf.erdos.client.utils.TokenUtil;
import com.scf.erdos.common.constant.TraceConstant;
import com.scf.erdos.common.constant.UaaConstant;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @Description : traceId
 * @author：bao-clm
 * @date: 2019/1/12
 * @version：1.0
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class RequestStatsFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return -501;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String traceId = MDC.get(TraceConstant.LOG_B3_TRACEID);
        MDC.put(TraceConstant.LOG_TRACE_ID, traceId);

        String accessToken = TokenUtil.extractToken(exchange.getRequest());

        //构建head
        ServerHttpRequest traceHead = exchange.getRequest().mutate()
                .header(TraceConstant.HTTP_HEADER_TRACE_ID, traceId)
                .header(UaaConstant.TOKEN_HEADER, accessToken).build();
        //将现在的request 变成 change对象

        log.info("request url = " + exchange.getRequest().getURI() + ", traceId = " + traceId);

        ServerWebExchange build = exchange.mutate().request(traceHead).build();

        return chain.filter(build);

    }


}
