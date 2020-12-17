package com.scf.erdos.client.dto;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description : 路由断言模型
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
public class GatewayPredicateDefinition {
    private String name;
    @Builder.Default
    private Map<String, String> args = new LinkedHashMap<>();
}
