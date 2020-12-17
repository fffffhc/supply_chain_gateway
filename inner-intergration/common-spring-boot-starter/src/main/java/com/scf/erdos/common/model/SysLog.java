package com.scf.erdos.common.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Description : 日志实体
 * @author：bao-clm
 * @date: 2019/2/3
 * @version：1.0
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLog implements Serializable {

	private static final long serialVersionUID = -5398795297842978376L;
	// 主键
	private Long id;
	// 用户名
	private String username;
	// 归属模块
	private String module;
	// 执行方法的参数值
	private String params;
	// 备注
	private String remark;
	// 是否执行成功
	private Boolean flag;
	// 记录开始时间
	private Date createTime;
}
