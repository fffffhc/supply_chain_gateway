package com.scf.erdos.factoring.vo.dict;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 产品相关字典表视图
 * @author：bao-clm
 * @date: 2020/5/25
 * @version：1.0
 */
@Data
public class DictListVo implements Serializable {

    /**
     * 标识
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 名称
     */
    private String type;

    /**
     * 排序
     */
    private Integer sort;

}
