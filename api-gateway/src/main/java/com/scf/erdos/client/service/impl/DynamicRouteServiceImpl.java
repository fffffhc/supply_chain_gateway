package com.scf.erdos.client.service.impl;

import java.net.URI;
import java.util.*;

import javax.annotation.Resource;

import com.scf.erdos.client.dto.GatewayFilterDefinition;
import com.scf.erdos.client.dto.GatewayPredicateDefinition;
import com.scf.erdos.client.dto.GatewayRouteDefinition;
import com.scf.erdos.client.entity.GatewayRoutes;
import com.scf.erdos.client.mapper.GatewayRoutesMapper;
import com.scf.erdos.client.routes.RedisRouteDefinitionRepository;
import com.scf.erdos.client.service.DynamicRouteService;
import com.scf.erdos.client.vo.GatewayRoutesVO;
import com.scf.erdos.common.util.PageUtil;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Service
@SuppressWarnings("all")
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware, DynamicRouteService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    @Autowired
    private GatewayRoutesMapper gatewayRoutesMapper;


    /**
     * 初始化 转化对象
     */
    private static MapperFacade routeDefinitionMapper;
    private static MapperFacade routeVOMapper;

    static {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(GatewayRouteDefinition.class, GatewayRoutes.class)
                .exclude("filters")
                .exclude("predicates")
                .byDefault();
        routeDefinitionMapper = mapperFactory.getMapperFacade();

        //  routeVOMapper
        mapperFactory.classMap(GatewayRoutes.class, GatewayRoutesVO.class)
                .byDefault();
        routeVOMapper = mapperFactory.getMapperFacade();

    }


    /**
     * 给spring注册事件
     * 刷新路由
     */
    private void notifyChanged() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * Set the ApplicationEventPublisher that this object runs in.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method.
     * Invoked before ApplicationContextAware's setApplicationContext.
     *
     * @param applicationEventPublisher event publisher to be used by this object
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

//    @Override
//    public String add(RouteDefinition definition) {
//        redisTemplate.opsForValue().set(GATEWAY_ROUTES_PREFIX + definition.getId(), JSONObject.toJSONString(definition));
//        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
//        notifyChanged();
//        return "success";
//    }
//
//    @Override
//    public String update(RouteDefinition definition) {
//        redisTemplate.delete(GATEWAY_ROUTES_PREFIX + definition.getId());
//        redisTemplate.opsForValue().set(GATEWAY_ROUTES_PREFIX + definition.getId(), JSONObject.toJSONString(definition));
//        return "success";
//        try {
//            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
//        } catch (Exception e) {
//            return "update fail,not find route  routeId: " + definition.getId();
//        }
//        try {
//            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
//            notifyChanged();
//            return "success";
//        } catch (Exception e) {
//            return "update route  fail";
//        }
//    }


    /**
     * 新增路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public String add(GatewayRouteDefinition gatewayRouteDefinition) {
        GatewayRoutes gatewayRoutes = transformToGatewayRoutes(gatewayRouteDefinition);
        gatewayRoutes.setDelFlag(0);
        gatewayRoutes.setCreateTime(new Date());
        gatewayRoutes.setUpdateTime(new Date());
        gatewayRoutesMapper.insertSelective(gatewayRoutes);
        gatewayRouteDefinition.setId(gatewayRoutes.getId());
        redisTemplate.boundHashOps(RedisRouteDefinitionRepository.GATEWAY_ROUTES_PREFIX).put(gatewayRouteDefinition.getId(), JSONObject.toJSONString(gatewayRouteDefinition));
        return gatewayRoutes.getId();
    }

    /**
     * 修改路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public String update(GatewayRouteDefinition gatewayRouteDefinition) {
        GatewayRoutes gatewayRoutes = transformToGatewayRoutes(gatewayRouteDefinition);
        gatewayRoutes.setCreateTime(new Date());
        gatewayRoutes.setUpdateTime(new Date());
        gatewayRoutesMapper.updateByPrimaryKeySelective(gatewayRoutes);
        redisTemplate.boundHashOps(RedisRouteDefinitionRepository.GATEWAY_ROUTES_PREFIX).delete(gatewayRouteDefinition.getId());
        redisTemplate.boundHashOps(RedisRouteDefinitionRepository.GATEWAY_ROUTES_PREFIX).put(gatewayRouteDefinition.getId(), JSONObject.toJSONString(gatewayRouteDefinition));
        return gatewayRouteDefinition.getId();
    }


    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    @Override
    public String delete(String id) {
        gatewayRoutesMapper.deleteByPrimaryKey(id);
        redisTemplate.boundHashOps(RedisRouteDefinitionRepository.GATEWAY_ROUTES_PREFIX).delete(id);

        return "success";
//        try {
//            this.routeDefinitionWriter.delete(Mono.just(id));
//            notifyChanged();
//            return "delete success";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "delete fail";
//        }
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @Override
    public Result findAll(Map<String, Object> params) {

        int total = gatewayRoutesMapper.count(params);
        List list = Collections.emptyList();
        PageUtil pageUtil = new PageUtil();
        Result result = pageUtil.pageParamConver(params, true);
        Integer page = MapUtils.getInteger(params, "page");
        Integer limit = MapUtils.getInteger(params, "limit");
        if(result.getResp_code() == 200){
            params = (Map)result.getData();
        }else{
            return result;
        }
        list = gatewayRoutesMapper.findAll(new HashMap());

        PageResult pageResult = PageResult.<GatewayRoutesVO>builder().page(page).limit(limit).data(list).count((long)total).build();
        return Result.succeed(pageResult,"成功");
    }

    /**
     * @return
     */
    @Override
    public String synchronization() {
        HashMap map = new HashMap();
        map.put("delFlag", 0);
        List<GatewayRoutes> alls = gatewayRoutesMapper.findAll(map);

        for (GatewayRoutes route : alls) {
            GatewayRouteDefinition gatewayRouteDefinition = GatewayRouteDefinition.builder()
                    .description(route.getDescription())
                    .id(route.getId())
                    .order(route.getOrder())
                    .uri(route.getUri())
                    .build();

            List<GatewayFilterDefinition> gatewayFilterDefinitions = JSONArray.parseArray(route.getFilters(), GatewayFilterDefinition.class);
            List<GatewayPredicateDefinition> gatewayPredicateDefinitions = JSONArray.parseArray(route.getPredicates(), GatewayPredicateDefinition.class);
            gatewayRouteDefinition.setPredicates(gatewayPredicateDefinitions);
            gatewayRouteDefinition.setFilters(gatewayFilterDefinitions);

            redisTemplate.boundHashOps(RedisRouteDefinitionRepository.GATEWAY_ROUTES_PREFIX).put(route.getId(), JSONObject.toJSONString(gatewayRouteDefinition));


        }

        return "success";
    }

    /**
     * 更改路由状态
     *
     * @param params
     * @return
     */
    @Override
    public Result updateFlag(Map<String, Object> params) {
        String id = MapUtils.getString(params, "id");
        Integer flag = MapUtils.getInteger(params, "flag");

        GatewayRoutes gatewayRoutes = gatewayRoutesMapper.selectByPrimaryKey(id);
        if (gatewayRoutes == null) {
            return Result.failed("路由不存在");
        }

        if (flag == 1) {
            redisTemplate.boundHashOps(RedisRouteDefinitionRepository.GATEWAY_ROUTES_PREFIX).delete(id);

        } else {
            GatewayRouteDefinition gatewayRouteDefinition = GatewayRouteDefinition.builder()
                    .description(gatewayRoutes.getDescription())
                    .id(gatewayRoutes.getId())
                    .order(gatewayRoutes.getOrder())
                    .uri(gatewayRoutes.getUri())
                    .build();

            List<GatewayFilterDefinition> gatewayFilterDefinitions = JSONArray.parseArray(gatewayRoutes.getFilters(), GatewayFilterDefinition.class);
            List<GatewayPredicateDefinition> gatewayPredicateDefinitions = JSONArray.parseArray(gatewayRoutes.getPredicates(), GatewayPredicateDefinition.class);
            gatewayRouteDefinition.setPredicates(gatewayPredicateDefinitions);
            gatewayRouteDefinition.setFilters(gatewayFilterDefinitions);

            redisTemplate.boundHashOps(RedisRouteDefinitionRepository.GATEWAY_ROUTES_PREFIX).put(gatewayRoutes.getId(), JSONObject.toJSONString(gatewayRouteDefinition));

        }

        gatewayRoutes.setDelFlag(flag);
        gatewayRoutes.setUpdateTime(new Date());
        int i = gatewayRoutesMapper.updateByPrimaryKeySelective(gatewayRoutes);
        return i > 0 ? Result.succeed("更新成功") : Result.failed("更新失败");
    }

    /**
     * 转化路由对象  GatewayRoutes
     *
     * @param gatewayRouteDefinition
     * @return
     */
    private GatewayRoutes transformToGatewayRoutes(GatewayRouteDefinition gatewayRouteDefinition) {
        GatewayRoutes definition = new GatewayRoutes();
        routeDefinitionMapper.map(gatewayRouteDefinition, definition);
        //设置路由id
        if (!StringUtils.isNotBlank(definition.getId())) {
            definition.setId(java.util.UUID.randomUUID().toString().toUpperCase().replace("-", ""));
        }

        String filters = JSONArray.toJSONString(gatewayRouteDefinition.getFilters());
        String predicates = JSONArray.toJSONString(gatewayRouteDefinition.getPredicates());

        definition.setFilters(filters);
        definition.setPredicates(predicates);

        return definition;
    }

    /**
     * 测试方法 新建 一个路由
     */
//    @PostConstruct
    public void main() {
        RouteDefinition definition = new RouteDefinition();
        definition.setId("jd");
        URI uri = UriComponentsBuilder.fromUriString("lb://user-center").build().toUri();
//         URI uri = UriComponentsBuilder.fromHttpUrl("http://baidu.com").build().toUri();
        definition.setUri(uri);
        definition.setOrder(11111);

        //定义第一个断言
        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Path");

        Map<String, String> predicateParams = new HashMap<>(8);
        predicateParams.put("pattern", "/jd/**");
        predicate.setArgs(predicateParams);
        //定义Filter
        FilterDefinition filter = new FilterDefinition();
        filter.setName("StripPrefix");
        Map<String, String> filterParams = new HashMap<>(8);
        //该_genkey_前缀是固定的，见org.springframework.cloud.gateway.support.NameUtils类
        filterParams.put("_genkey_0", "1");
        filter.setArgs(filterParams);

        definition.setFilters(Arrays.asList(filter));
        definition.setPredicates(Arrays.asList(predicate));

        System.out.println("definition:" + JSON.toJSONString(definition));
        redisTemplate.opsForHash().put(RedisRouteDefinitionRepository.GATEWAY_ROUTES_PREFIX, "key", JSON.toJSONString(definition));
    }
}
