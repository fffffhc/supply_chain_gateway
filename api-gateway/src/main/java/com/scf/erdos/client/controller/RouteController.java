package com.scf.erdos.client.controller;

import java.util.HashMap;
import java.util.Map;
import com.scf.erdos.client.dto.GatewayRouteDefinition;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.client.service.DynamicRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/route")
@SuppressWarnings("all")
public class RouteController {

    @Autowired
    private DynamicRouteService dynamicRouteService;

    //增加路由
	@PostMapping("/add")
    public Mono<Result>   add(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
	    String routeId = dynamicRouteService.add(gatewayRouteDefinition);
        return Mono.just(Result.succeed(routeId,"添加成功"));
    }

    //更新路由
    @PostMapping("/update")
    public Mono<Result> update(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        String routeId = dynamicRouteService.update(gatewayRouteDefinition);
        return Mono.just(Result.succeed(routeId,"修改成功"));
    }

    //删除路由
    @DeleteMapping("/{id}")
    public Mono<Result> delete(@PathVariable String id) {
        dynamicRouteService.delete(id);
        return Mono.just(Result.succeed(id,"删除成功"));
    }

    //获取全部数据
    @GetMapping("/findAll")
    public Mono<Result>  findAll(@RequestBody Map<String, Object> params){
        return Mono.just(dynamicRouteService.findAll(params));
    }

    //同步redis数据 从mysql中同步过去
    @GetMapping("/synchronization")
    public Mono<Result>  synchronization() {
        dynamicRouteService.synchronization();
        return Mono.just(Result.succeed("同步成功"));
    }


    //修改路由状态
    @PutMapping("/updateFlag")
    public Mono<Result>  updateFlag(@RequestBody Map<String, Object> params) {
        return Mono.just(dynamicRouteService.updateFlag(params));
    }

    public static void main(String[] args){
	    int DEFAULT_INITIAL_CAPACITY = 1 << 4;
        Map<String,Object> map = new HashMap<>(80);

    }
}
