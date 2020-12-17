package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;

import java.util.Map;

public interface ISignUpOnlineService {

    Result getOnlineContracts(Map<String, Object> params) throws ServiceException;
    Result getSignupInfo(Integer id) throws ServiceException;
}
