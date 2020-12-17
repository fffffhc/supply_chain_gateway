package com.scf.erdos.user.model;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2020/4/8
 * @version：1.0
 */
@Data
public class SysMenuTree implements Serializable {

    private Long id;
    private Long parentId;
    private String name;
    private Integer sort;
    private Boolean checked = false;
    private List<SysMenuTree> children;
}
