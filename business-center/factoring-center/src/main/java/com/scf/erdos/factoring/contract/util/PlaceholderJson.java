package com.scf.erdos.factoring.contract.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scf.erdos.factoring.contract.model.varItems.VarItems;
import com.scf.erdos.factoring.contract.model.varItems.VarItemsGroup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.*;

/**
 * @Description : 占位符JOSN 解析
 * @author：bao-clm
 * @date: 2020/9/7
 * @version：1.0
 */

@Slf4j
@SuppressWarnings("all")
public class PlaceholderJson {
    /**
     * 消息系统变量容器
     */
    public static Map<String, VarItemsGroup> varItemsGroupMap = null;

    /**
     * 通过 合同key 获取相应的占位符
     */
    public static List<VarItems> findVarItemsByKey(String groupKey) {
        getPlaceholder();
        VarItemsGroup vo = varItemsGroupMap.get(groupKey);
        if (vo == null) {
            return null;
        }
        return vo.getVars();
    }

    /**
     * 获取所有相应的占位符
     */
    public static List<VarItems> findAllVarItems() {
        getPlaceholder();
        List<VarItems> varItemsList = new ArrayList<>();

        Iterator it = varItemsGroupMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            VarItemsGroup vo = (VarItemsGroup)entry.getValue();
            varItemsList.addAll(vo.getVars());
        }

        return varItemsList;
    }

    public static Map<String, VarItemsGroup> getPlaceholder() {
        JSONObject all = new JSONObject();
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:contractTemplate/*.json");
            for (Resource r : resources) {
                JSONObject obj = JSON.parseObject(r.getInputStream(), JSONObject.class);
                for (String key : obj.keySet()) {
                        JSONObject varGroup = obj.getJSONObject(key);
                        JSONArray vars = varGroup.getJSONArray("vars");
                        if (vars != null && vars.size() > 0) {
                            for (int i = 0; i < vars.size(); i++) {
                                JSONObject var = vars.getJSONObject(i);
                                if (StringUtils.isNotBlank(var.getString("refSn"))) {
                                    continue; // 这个说明是引用分组的
                                }
                                var.put("sn", key + "__" + var.getString("code"));
                            }
                        }
                        all.put(key, varGroup);
                }
            }
        } catch (IOException e) {
            log.error("执行消息系统变量初始化失败", e);
        }

        // 然后去解析这个文件
        varItemsGroupMap = new HashMap<String, VarItemsGroup>();
        for (String key : all.keySet()) {
            JSONArray vars = depthFirstSearch(key, all);
            List<VarItems> varItemsList = vars.toJavaList(VarItems.class);
            varItemsGroupMap.put(key, new VarItemsGroup(varItemsList));
        }
        return varItemsGroupMap;
    }

    /**
     * 深度遍历查询
     */
    public static JSONArray depthFirstSearch(String key, JSONObject all) {

        JSONObject obj = all.getJSONObject(key);
        JSONArray vars = new JSONArray();

        JSONArray array = obj.getJSONArray("vars");
        JSONArray currentVars = JSON.parseArray(array.toJSONString()); // 避免对象引用，造个新的
        if (currentVars != null && currentVars.size() > 0) {
            for (int i = 0; i < currentVars.size(); i++) {
                JSONObject var = currentVars.getJSONObject(i);
                vars.add(var);
            }
        }
        return vars;
    }
}
