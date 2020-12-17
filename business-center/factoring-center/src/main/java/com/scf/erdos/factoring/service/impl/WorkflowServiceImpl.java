package com.scf.erdos.factoring.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.constant.CompanyRole;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.common.model.SysUser;
import com.scf.erdos.common.util.SysUserUtil;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.WorkflowDao;
import com.scf.erdos.factoring.feign.UserFeignClient;
import com.scf.erdos.factoring.model.workflow.CompanyWorkflowInfo;
import com.scf.erdos.factoring.service.IWorkflowService;
import com.scf.erdos.factoring.vo.workflow.CompanyWorkflow;
import com.scf.erdos.factoring.vo.workflow.WorkflowInfoVo;
import com.scf.erdos.factoring.vo.workflow.WorkflowPageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description : 自定义工作流接口实现类
 * @author：bao-clm
 * @date: 2020/5/19
 * @version：1.0
 */
@Service
@SuppressWarnings("all")
public class WorkflowServiceImpl  implements IWorkflowService {

    @Autowired
    private WorkflowDao workflowDao;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public Result add(com.scf.erdos.factoring.model.workflow.CompanyWorkflow companyWorkflow) throws ServiceException {
        try{
            if(companyWorkflow == null){
                return Result.failed("不能提交空对象");
            }
            String companyId = companyWorkflow.getCompanyId();
            //String flowType = companyWorkflow.getFlowType();
            List<CompanyWorkflowInfo> flowInfoList = companyWorkflow.getFlowInfoList();
            if(companyId == null || "".equals(companyId)){
                return Result.failed("companyId 不能为空");
            }
            /*if(flowType == null || "".equals(flowType)){
                return Result.failed("flowType 不能为空");
            }*/
            if(flowInfoList == null || flowInfoList.size() == 0){
                return Result.failed("工作流流程不能为空");
            }

            /**
             * 获取当前用户登陆信息
             */
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            companyWorkflow.setUserId(loginUser.getId());
            String flowInfo = JSON.toJSON(flowInfoList).toString();
            companyWorkflow.setFlowInfo(flowInfo);

            int i = workflowDao.add(companyWorkflow);
            if(i > 0){
                return Result.succeed("保存成功");
            }else{
                return Result.failed("保存失败");
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result update(com.scf.erdos.factoring.model.workflow.CompanyWorkflow companyWorkflow) throws ServiceException {
        try{
            String id = companyWorkflow.getId();
            if(id == null || "".equals(id)){
                return Result.failed("id 不能为空");
            }
            //正在使用的工作流程不能修改 - >>>>后期加


            /**
             * 获取当前用户登陆信息
             */
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            companyWorkflow.setUpdateUserId(loginUser.getId());

            List<CompanyWorkflowInfo> flowInfoList = companyWorkflow.getFlowInfoList();
            if(flowInfoList == null || flowInfoList.size() != 0){
                String flowInfo = JSON.toJSON(flowInfoList).toString();
                companyWorkflow.setFlowInfo(flowInfo);
            }

            int i = workflowDao.update(companyWorkflow);
            if(i > 0){
                return Result.succeed("修改成功");
            }else{
                return Result.failed("修改失败");
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result delete(String id) throws ServiceException {
        try{
            String status = workflowDao.getWorkflowStatus(id);
            if("1".equals(status)){
                return Result.failed("该工作流正在使用中，不能删除");
            }
            workflowDao.delete(id);
            return Result.succeed("成功");
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getWorkflows(Map<String, Object> params) {
        try {
            Integer page = MapUtils.getInteger(params, "page");
            Integer limit = MapUtils.getInteger(params, "limit");

            if (page == null || limit == null) {
                return Result.failed("page，limit不能为空！");
            }

            if(page < 1){
                return Result.failed("page不能小于1，首页从1开始！");
            }

            params.put("currentPage",(page - 1)*limit);
            params.put("pageSize",limit);

            int total = workflowDao.count(params);
            List<WorkflowPageVo> list = workflowDao.getAllWorkflow(params);
            List<String> userIds = list.stream().map(WorkflowPageVo::getUserId).collect(Collectors.toList());

            List<WorkflowPageVo> newList = new ArrayList<WorkflowPageVo>();
            if(userIds.size() > 0){
                //获取用户信息
                List<SysUser> userList = userFeignClient.findByUserIds(userIds);
                //用户信息组装
                newList = list.stream()
                        .map(workflowPageVo -> userList.stream()
                                .filter(loginAppUser -> workflowPageVo.getUserId().equals(String.valueOf(loginAppUser.getId())))
                                .findFirst()
                                .map(loginAppUser -> {
                                    workflowPageVo.setUserName(loginAppUser.getRealname());
                                    return workflowPageVo;
                                }).orElse(null))
                        .collect(Collectors.toList());
            }

            PageResult pageResult = PageResult.<WorkflowPageVo>builder().page(page).limit(limit).data(newList).count((long)total).build();
            return Result.succeed(pageResult,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getWorkflowInfo(Map<String, Object> params) {
        try {
            WorkflowInfoVo workflowInfoVo = new WorkflowInfoVo();
            com.scf.erdos.factoring.model.workflow.CompanyWorkflow companyWorkflow = workflowDao.getWorkflowInfo(params);
            BeanUtils.copyProperties(companyWorkflow,workflowInfoVo);
            List<CompanyWorkflow> list = JSONObject.parseArray(companyWorkflow.getFlowInfo(), CompanyWorkflow.class);
            workflowInfoVo.setFlowInfoList(list);
            return Result.succeed(workflowInfoVo,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result handleWork(Map<String, Object> params) {
        try {
            workflowDao.handleWork(params);
            return Result.succeed("成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getRoles(Map<String, Object> params) {
        try {
            List<SysRole> roleList = userFeignClient.getRoles(CompanyRole.COMPANY_10004);
            return Result.succeed(roleList,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
