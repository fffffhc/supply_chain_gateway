package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.vo.company.CompanyRegisterInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.Map;

public interface ICompanyService {
    Result getAllCompany(Map<String, Object> params) throws ServiceException;

    Result getCompanyInfo(Map<String, Object> params) throws ServiceException;

    Result add(Map<String, Object> params) throws ServiceException;

    Result update(Map<String, Object> params) throws ServiceException;

    Result audit(Map<String, Object> params) throws ServiceException;

    Result settleIn(Map<String, Object> params) throws ServiceException;

    CompanyRegisterInfoVo findConpanyInfoByUserId(Long userId) throws ServiceException;

    Result getTianyancha(@Param("keyword") String keyword);

    Result authCA(@RequestParam Map<String, Object> params);

    Result fddCaAuthCallBack(@RequestParam Map<String, Object> params);

    Result sealUpload(@RequestParam Map<String, Object> params);

    Result contractUpload(@RequestParam Map<String, Object> params);

    Result extsign(@RequestParam Map<String,Object> params);



}
