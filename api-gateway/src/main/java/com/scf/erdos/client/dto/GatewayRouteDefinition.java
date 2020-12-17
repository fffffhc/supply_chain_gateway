package com.scf.erdos.client.dto;

import com.alibaba.fastjson.JSON;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description : 创建路由模型
 * @author：bao-clm
 * @date: 2019/1/12
 * @version：1.0
 */
@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class GatewayRouteDefinition {

    //路由的Id
    private String id;
    //路由断言集合配置
    @Builder.Default
    private List<GatewayPredicateDefinition> predicates = new ArrayList<>();
    //路由过滤器集合配置
    @Builder.Default
    private List<GatewayFilterDefinition> filters = new ArrayList<>();
    //路由规则转发的目标uri
    private String uri;
    //路由执行的顺序
    @Builder.Default
    private int order = 0;
    //路由描述
    private String description;

    //前端json 封装
    public static  void main(String[] args){
        Map m = new LinkedHashMap<>();
        m.put("1","11");
        m.put("2","22");

        List<GatewayPredicateDefinition> predicates = new ArrayList<>();
        List<GatewayFilterDefinition> f = new ArrayList<>();

        GatewayPredicateDefinition a = new GatewayPredicateDefinition();
        GatewayFilterDefinition b = new GatewayFilterDefinition();
        GatewayRouteDefinition c = new GatewayRouteDefinition();
        predicates.add(a);
        a.setName("predicates");
        a.setArgs(m);

        b.setName("filter");
        b.setArgs(m);

        c.setPredicates(predicates);
        String json = JSON.toJSONString(c);
        System.out.println(json);
    }
}
