package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.financing.Financing;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface IFinancingService {

    Result getFinancings(Map<String, Object> params) throws ServiceException;
    Result getFinancingInfo(@Param("id") Integer id) throws ServiceException;
    Result getCustomerInfo(@Param("id") Integer id) throws ServiceException;
    Result financeingReview(@Param("id") Integer id) throws ServiceException;
    Result audit(Financing financing) throws ServiceException;
    Result reject(Financing financing) throws ServiceException;
    Result ht() throws ServiceException;
}
