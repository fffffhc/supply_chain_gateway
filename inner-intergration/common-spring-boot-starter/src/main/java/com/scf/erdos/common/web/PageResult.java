package com.scf.erdos.common.web;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Description : 分页实体类
 * 				  page  当前页
 * 				  limit 每页显示条数
 *  			 count 总数
 *  			 data 当前页结果集
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = -275582248840137389L;
	private int page;
	private int limit;
	private Long count;
	private List<T> data;
}
