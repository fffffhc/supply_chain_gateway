package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;

import java.util.Map;

public interface IDictService {

    Result getCompanyDicts(Map<String, Object> params) throws ServiceException;
    Result getProductDicts(Map<String, Object> params) throws ServiceException;
    Result getCompanyByType(Map<String, Object> params) throws ServiceException;
}
