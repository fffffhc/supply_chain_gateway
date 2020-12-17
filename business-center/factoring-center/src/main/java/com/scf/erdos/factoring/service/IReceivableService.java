package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.receivable.ReceivableInfo;

import java.util.Map;

public interface IReceivableService {

    Result getYszks(Map<String, Object> params) throws ServiceException;

    Result getYszkInfo(Integer id) throws ServiceException;

    Result add(ReceivableInfo yszkInfo) throws ServiceException;

    Result update(ReceivableInfo yszkInfo) throws ServiceException;

    Result getExcelTemplate(Map<String, Object> params) throws ServiceException;
}
