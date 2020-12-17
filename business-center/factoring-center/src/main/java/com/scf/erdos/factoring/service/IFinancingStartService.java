package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.financing.FinancingSave;

import java.util.Map;

public interface IFinancingStartService {

    Result getProductCredit(Map<String, Object> params) throws ServiceException;
    Result getProducts(Map<String, Object> params) throws ServiceException;
    Result applyFinancing(Map<String, Object> params) throws ServiceException;
    Result save(FinancingSave financingSave) throws ServiceException;
    Result getBuyerCompanys(Map<String, Object> params) throws ServiceException;
}
