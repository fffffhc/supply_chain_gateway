package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.credit.ProductCredit;

import java.util.Map;

public interface ICredictService {

    Result apply(Map<String, Object> params) throws ServiceException;
    Result getCredicts(Map<String, Object> params) throws ServiceException;
    Result getCreditInfo(Map<String, Object> params) throws ServiceException;
    Result audit(ProductCredit productCredit) throws ServiceException;
}
