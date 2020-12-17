package com.scf.erdos.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

/**
 * @Description : 角色
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
@Data
public class SysRole implements Serializable {

	private static final long serialVersionUID = 4497149010220586111L;
	@JsonSerialize(using=ToStringSerializer.class)
	private Long id;
	private String code;
	private String companyLabel;
	private String companyLabelName;
	private String name;
	private Date createTime;
	private Date updateTime;
	private Long userId;
	private List<Integer> menuIds;

}
