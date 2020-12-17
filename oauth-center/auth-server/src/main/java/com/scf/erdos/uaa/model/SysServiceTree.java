package com.scf.erdos.uaa.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 服务封装
 * @author：bao-clm
 * @date: 2020/4/8
 * @version：1.0
 */

@Data
public class SysServiceTree implements Serializable {

    private Long id;
    private Long parentId;
    private String name;
    private Boolean checked = false;
    private List<SysServiceTree> children;

}
