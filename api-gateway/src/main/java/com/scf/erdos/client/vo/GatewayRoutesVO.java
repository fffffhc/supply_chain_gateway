package com.scf.erdos.client.vo;

import lombok.*;

import java.util.Date;

@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayRoutesVO {

    // id
    private String id;
    // uri路径
    private String uri;
    // 判定器（谓词）
    private String predicates;
    // 过滤器
    private String filters;
    // 排序
    private Integer order;
    // 描述
    private String description;
    // 删除标志（0，不删除 1，删除）
    private Integer delFlag;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;

}
