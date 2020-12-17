package com.scf.erdos.client.service;

import java.util.Map;

import com.scf.erdos.client.dto.GatewayRouteDefinition;
import com.scf.erdos.client.vo.GatewayRoutesVO;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;

/**
 * @Description : 路由 Service
 * @author：bao-clm
 * @date: 2019/1/24
 * @version：1.0
 */

public interface DynamicRouteService {

    /**
     * 新增路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    String add(GatewayRouteDefinition gatewayRouteDefinition);

    /**
     * 修改路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    String update(GatewayRouteDefinition gatewayRouteDefinition);


    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    String delete(String id);


    /**
     * 查询全部数据
     *
     * @return
     */
    Result findAll(Map<String, Object> params);

    /**
     * 同步redis数据 从mysql中同步过去
     *
     * @return
     */
    String synchronization();


    /**
     * 更改路由状态
     *
     * @param params
     * @return
     */
    Result updateFlag(Map<String, Object> params);

}
