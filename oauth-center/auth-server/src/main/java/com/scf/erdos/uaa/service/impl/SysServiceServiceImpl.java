package com.scf.erdos.uaa.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.uaa.dao.SysClientServiceDao;
import com.scf.erdos.uaa.dao.SysServiceDao;
import com.scf.erdos.uaa.model.SysService;
import com.scf.erdos.uaa.model.SysServiceTree;
import com.scf.erdos.uaa.service.SysServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2020/2/1
 * @version：1.0
 */

@Slf4j
@Service
@Transactional
@SuppressWarnings("all")
public class SysServiceServiceImpl implements SysServiceService {

    @Autowired
    private SysServiceDao sysServiceDao;

    @Autowired
    private SysClientServiceDao sysClientServiceDao;

    /**
     * 添加服务
     *
     * @param service
     */
    @Override
    public void save(SysService service) {
        service.setCreateTime(new Date());
        service.setUpdateTime(new Date());

        sysServiceDao.save(service);
        log.info("添加服务：{}", service);
    }

    /**
     * 更新服务
     *
     * @param service
     */
    @Override
    public void update(SysService service) {
        service.setUpdateTime(new Date());

        sysServiceDao.update(service);

        log.info("更新服务：{}", service);
    }

    /**
     * 删除服务
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        SysService sysService = sysServiceDao.findById(id);

        /**
         * 如果删除父级服务，则删除所对应的所有下级服务。
         */
        sysServiceDao.deleteByParentId(sysService.getId());
        sysServiceDao.delete(id);
        log.info("删除服务:{}",sysService);
    }

    /**
     * 客户端分配服务
     *
     * @param clientId
     * @param serviceIds
     */
    @Override
    public void setMenuToClient(Long clientId, Set<Long> serviceIds) {
        sysClientServiceDao.delete(clientId,null);

        if (!CollectionUtils.isEmpty(serviceIds)){
            serviceIds.forEach(serviceId -> {
                sysClientServiceDao.save(clientId,serviceId);
            });

        }

    }

    /**
     * 客户端服务列表
     *
     * @param clientIds
     * @return
     */
    @Override
    public List<SysService> findByClient(Set<Long> clientIds) {
        return sysClientServiceDao.findServicesBySlientIds(clientIds);
    }

    /**
     * 服务列表
     *
     * @return
     */
    @Override
    public List<SysService> findAll() {
        return sysServiceDao.findAll();
    }

    /**
     * ID获取服务
     *
     * @param id
     * @return
     */
    @Override
    public SysService findById(Long id) {
        return sysServiceDao.findById(id);
    }

    /**
     * 角色ID获取服务
     *
     * @param clientId
     * @return
     */
    @Override
    public Set<Long> findServiceIdsByClientId(Long clientId) {
        return sysClientServiceDao.findServiceIdsByClientId(clientId);
    }

    /**
     * 一级服务
     *
     * @return
     */
    @Override
    public List<SysService> findOnes() {
        return sysServiceDao.findOnes();
    }

    /**
     * @Description : 获取全部服务及 当前应用绑定的服务封装
     * @author：bao-clm
     * @date: 2020/4/8
     * @version：1.0
     */
    @Override
    public Result findServicesByclientId(Long clientId) {
        Set<Long> clientIds = new HashSet<Long>();
        clientIds.add(clientId);
        List<SysService> list = new ArrayList<SysService>();
        List<SysService> allService = sysServiceDao.findAll();//获取全部服务
        List<SysService> clientService = sysClientServiceDao.findServicesBySlientIds(clientIds);//获取应用绑定的服务

        //封装应用绑定的服务
        for(SysService sysService : allService){
            for(SysService service : clientService){
                if (sysService.getId() == service.getId()){
                    sysService.setChecked(true);
                }else{
                    sysService.setChecked(false);
                }
            }
            list.add(sysService);
        }

        //顶级服务父级id 为 -1
        List<SysServiceTree> treeDataList = getTreeList(list, -1L);
        //System.out.println("获取某个父级的树形结构数据：" + gson.toJson(treeDataList));

        return Result.succeed(treeDataList,"获取成功");
       /* Set<Long> clientIds = new HashSet<Long>();
        //初始化应用
        clientIds.add(clientId);

        List<SysService> clientService = sysServiceService.findByClient(clientIds);
        List<SysService> allService = sysServiceService.findAll();
        List<Map<String, Object>> authTrees = new ArrayList<>();

        Map<Long,SysService> clientServiceMap = clientService.stream().collect(Collectors.toMap(SysService::getId,SysService->SysService));

        for (SysService sysService: allService) {
            Map<String, Object> authTree = new HashMap<>();
            authTree.put("id",sysService.getId());
            authTree.put("name",sysService.getName());
            authTree.put("pId",sysService.getParentId());
            authTree.put("open",true);
            authTree.put("checked", false);
            if (clientServiceMap.get(sysService.getId())!=null){
                authTree.put("checked", true);
            }
            authTrees.add(authTree);
        }*/
    }

    //获取某个父级的树形结构数据
    public static List<SysServiceTree> getTreeList(List<SysService> list, Long parentId) {
        List<SysServiceTree> treeList = new ArrayList<>();
        List<SysService> childList = list.stream().filter(i -> i.getParentId().equals(parentId)).sorted(Comparator.comparing(SysService::getSort)).collect(Collectors.toList());
        for (SysService item : childList) {
            SysServiceTree m = new SysServiceTree();
            m.setId(item.getId());
            m.setName(item.getName());
            m.setParentId(item.getParentId());
            m.setChecked(item.getChecked());
            m.setChildren(getTreeList(list, item.getId()));
            treeList.add(m);
        }
        return treeList;
    }
    //获取某个父级的所有子级ids(不包括当前父级)
    /*public static List<String> getChildIds(List<SysService> list, Long parentId) {
        List<String> stringList = new ArrayList<>();
        List<SysService> childList = list.stream().filter(i -> i.getParentId().equals(parentId)).sorted(Comparator.comparing(SysService::getSort)).collect(Collectors.toList());
        for (SysService item : childList) {
            stringList.add(item.getId().toString());
            stringList.addAll(getChildIds(list, item.getId()));
        }
        return stringList;
    }

    //获取某个父级的所有子级数据
    public static List<SysService> getChildList(List<SysService> list, Long parentId) {
        List<SysService> resultList = new ArrayList<>();
        List<SysService> childList = list.stream().filter(i -> i.getParentId().equals(parentId)).sorted(Comparator.comparing(SysService::getSort)).collect(Collectors.toList());
        for (SysService item : childList) {
            resultList.add(item);
            resultList.addAll(getChildList(list, item.getId()));
        }
        return resultList;
    }*/
}
