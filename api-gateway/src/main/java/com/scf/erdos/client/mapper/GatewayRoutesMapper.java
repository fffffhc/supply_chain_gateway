package com.scf.erdos.client.mapper;

import com.scf.erdos.client.entity.GatewayRoutes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
@SuppressWarnings("all")
public interface GatewayRoutesMapper {
    int deleteByPrimaryKey(String id);

    int insert(GatewayRoutes record);

    int insertSelective(GatewayRoutes record);

    GatewayRoutes selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(GatewayRoutes record);

    int updateByPrimaryKey(GatewayRoutes record);

    int count(Map map);

    List<GatewayRoutes> findAll(Map map);
}