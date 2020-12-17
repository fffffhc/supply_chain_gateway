package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;

import java.util.Map;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/9/28 11:08
 */
public interface IRepayService {
    Result getPendingRepaymentInformation(Map<String, Object> params) throws ServiceException;
}
