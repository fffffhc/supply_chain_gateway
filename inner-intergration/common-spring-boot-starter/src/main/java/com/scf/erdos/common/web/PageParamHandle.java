package com.scf.erdos.common.web;


import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.util.SysUserUtil;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Description : 分页查询条件数据处理
 * @author：bao-clm
 * @date: 2020/8/3
 * @version：1.0
 */

@SuppressWarnings("all")
@Repository
public class PageParamHandle {

    public Result handle(Map<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page");
        Integer limit = MapUtils.getInteger(params, "limit");
        if (page == null || limit == null) {
            return Result.failed("page，limit不能为空！");
        }
        if (page < 1) {
            return Result.failed("page不能小于1，首页从1开始！");
        }

        //1，获取当前用户信息
        LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
        if (loginUser == null)
            return Result.failed("获取当前用户登陆信息失败");
        params.put("userId", loginUser.getId());
        params.put("companyId", loginUser.getCompanyId());//用户企业id

        params.put("currentPage", (page - 1) * limit);
        params.put("pageSize", limit);
        return Result.succeed(params, "SUCCESS");
    }
}
