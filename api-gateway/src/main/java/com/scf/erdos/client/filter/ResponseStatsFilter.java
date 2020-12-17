package com.scf.erdos.client.filter;

import com.scf.erdos.common.constant.TraceConstant;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @Description : 应答traceId
 * @author：bao-clm
 * @date: 2019/1/12
 * @version：1.0
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class ResponseStatsFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String traceId = MDC.get(TraceConstant.LOG_B3_TRACEID);
        MDC.put(TraceConstant.LOG_TRACE_ID, traceId);
        ServerHttpRequest request = exchange.getRequest();
        // 这里可以修改ServerHttpRequest实例
        ServerHttpResponse response = exchange.getResponse();
        // 这里可以修改ServerHttpResponse实例
        response.getHeaders().add(TraceConstant.HTTP_HEADER_TRACE_ID, traceId);
        // 构建新的ServerWebExchange实例

        log.info("response url " + request.getURI().getPath() + ", traceId = " + traceId);
        ServerWebExchange newExchange = exchange.mutate().request(exchange.getRequest()).response(response).build();

        return chain.filter(newExchange);
    }

}
