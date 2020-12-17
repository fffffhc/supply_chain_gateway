package com.scf.erdos.user.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2020/7/30
 * @version：1.0
 */
@Data
public class SysRoleMenus implements Serializable {
    private static final long serialVersionUID = 4497149010220586111L;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    private String code;
    private String companyLabel;
    private String companyLabelName;
    private String name;
    private Date createTime;
    private Date updateTime;
    private Long userId;
    private List<Integer> menuIds;

    private List<SysMenuTree> treeDataList;
}
