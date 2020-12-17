package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.contract.ContractTemplate;
import org.dom4j.DocumentException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface IContractTemplateService {

    Result getContractTemplates(Map<String, Object> params) throws ServiceException;
    Result saveOrUpdate(ContractTemplate contractTemplate, MultipartFile file) throws ServiceException, IOException, DocumentException;
    Result getInfo(Integer id) throws ServiceException;
    Result delete(Integer id) throws ServiceException;

}
