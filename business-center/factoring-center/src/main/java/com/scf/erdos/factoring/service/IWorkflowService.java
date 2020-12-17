package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.workflow.CompanyWorkflow;

import java.util.Map;

public interface IWorkflowService {

    Result add(CompanyWorkflow companyWorkflow) throws ServiceException;
    Result update(CompanyWorkflow companyWorkflow) throws ServiceException;
    Result delete(String id) throws ServiceException;
    Result getWorkflows(Map<String, Object> params);
    Result getWorkflowInfo(Map<String, Object> params);
    Result handleWork(Map<String, Object> params);
    Result getRoles(Map<String, Object> params);
}
