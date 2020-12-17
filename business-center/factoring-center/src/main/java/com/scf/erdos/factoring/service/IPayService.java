package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;

import java.util.Map;

public interface IPayService {

    Result getPendingLondInformation(Map<String, Object> params) throws ServiceException;

    Result getPaymentNote(Map<String, Object> params) throws ServiceException;

    Result pay(Map<String, Object> params) throws ServiceException;



}
