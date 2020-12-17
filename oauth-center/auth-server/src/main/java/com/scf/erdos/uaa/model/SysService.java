package com.scf.erdos.uaa.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * @Description : 服务 model
 * @author：bao-clm
 * @date: 2019/1/12
 * @version：1.0
 */
@Data
public class SysService implements Serializable {

    private static final long serialVersionUID = 749360940290141180L;

    private Long id;
    private Long parentId;
    private String name;
    private String css;
    private String url;
    private String path;
    private Integer sort;
    private Date createTime;
    private Date updateTime;
    private Integer isMenu;
    private Boolean checked = false;

}