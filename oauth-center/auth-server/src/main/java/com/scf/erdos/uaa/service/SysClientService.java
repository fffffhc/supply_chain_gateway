package com.scf.erdos.uaa.service;

import java.util.List;
import java.util.Map;

import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.uaa.dto.SysClientDto;
import com.scf.erdos.uaa.model.SysClient;

@SuppressWarnings("all")
public interface SysClientService {

	
	SysClient getById(Long id) ;
	 

    Result saveOrUpdate(SysClientDto clientDto);

    void deleteClient(Long id);
    
    public Result listRoles(Map<String, Object> params);
    
    List<SysClient> findList(Map<String, Object> params) ;
    

	Result updateEnabled(Map<String, Object> params);
    
}
