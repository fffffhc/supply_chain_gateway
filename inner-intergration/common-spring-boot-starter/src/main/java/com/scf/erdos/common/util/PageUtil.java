package com.scf.erdos.common.util;

import java.util.Map;

import com.scf.erdos.common.web.CodeEnum;
import com.scf.erdos.common.web.Result;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

 
/**
 * @Description : 分页参数处理工具
 * @author：bao-clm
 * @date: 2020/3/31
 * @version：1.0
 */
@Slf4j
public class PageUtil {

	/**
	 * 当前页，第一页从 1 开始
	 */
	public static final String CURPAGE  = "page";
	/**
	 * 分页参数，每页数据条数
	 */
	public static final String PAGESIZE = "limit";

	/**
	 * 转换并校验分页参数<br>
	 * mybatis中limit #{start, JdbcType=INTEGER}, #{length,
	 * JdbcType=INTEGER}里的类型转换貌似失效<br>
	 * 我们这里先把他转成Integer的类型
	 * 
	 * @param params
	 * @param required
	 * 分页参数是否是必填
	 */
	public Result pageParamConver(Map<String, Object> params, boolean required) {
		if (required) {
			// 分页参数必填时，校验参数
			if (params == null || !params.containsKey(CURPAGE) || !params.containsKey(PAGESIZE)) {
				return Result.failedWith(null, CodeEnum.ERROR.getCode(),"page，limit不能为空！");
			}
		}

		Integer page = MapUtils.getInteger(params, CURPAGE);
		Integer limit = MapUtils.getInteger(params, PAGESIZE);
		 if(page < 1){
			 return Result.failedWith(null, CodeEnum.ERROR.getCode(),"page不能小于1，首页从1开始！");
		 }
		/**
		 *
		 *  mybatis中limit #{start, JdbcType=INTEGER}, #{length,JdbcType=INTEGER}
		 */
		params.put("currentPage",(page - 1)*limit);
		params.put("pageSize",limit);
		return Result.succeedWith(params, CodeEnum.SUCCESS.getCode(),"");
	}
}
