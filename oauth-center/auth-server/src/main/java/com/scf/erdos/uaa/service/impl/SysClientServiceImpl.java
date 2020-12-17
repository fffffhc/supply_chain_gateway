package com.scf.erdos.uaa.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.scf.erdos.common.constant.UaaConstant;
import com.scf.erdos.common.model.SysUser;
import com.scf.erdos.common.util.PageUtil;
import com.scf.erdos.common.web.CodeEnum;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.uaa.dao.SysClientDao;
import com.scf.erdos.uaa.dao.SysClientServiceDao;
import com.scf.erdos.uaa.dto.SysClientDto;
import com.scf.erdos.uaa.model.SysClient;
import com.scf.erdos.uaa.service.SysClientService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@SuppressWarnings("all")
public class SysClientServiceImpl implements SysClientService {


    @Autowired
    private SysClientDao sysClientDao;

    @Autowired
    private SysClientServiceDao sysClientServiceDao;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
  
    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService ;



     
    @Override
    public Result saveOrUpdate(SysClientDto clientDto) {
        clientDto.setClientSecret(passwordEncoder.encode(clientDto.getClientSecretStr()));

        if (clientDto.getId() != null) {// 修改
            sysClientDao.update(clientDto);
        } else {// 新增
        	SysClient r = sysClientDao.getClient(clientDto.getClientId());
            if (r != null) {
                Result.failedWith(null,CodeEnum.ERROR.getCode(),clientDto.getClientId()+"已存在");
            }
            sysClientDao.save(clientDto);
        }
        return Result.succeedWith(null, CodeEnum.SUCCESS.getCode(),"操作成功");
    }

     

    @Override
    @Transactional
    public void deleteClient(Long id) {
        sysClientDao.delete(id);

        sysClientServiceDao.delete(id,null);

        redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).delete(id) ;
        log.debug("删除应用id:{}", id);
    }

	@Override
	public Result listRoles(Map<String, Object> params) {
        int total = sysClientDao.count(params);
        List list = Collections.emptyList();
        PageUtil pageUtil = new PageUtil();
        Result result = pageUtil.pageParamConver(params, true);
        Integer page = MapUtils.getInteger(params, "page");
        Integer limit = MapUtils.getInteger(params, "limit");
        if(result.getResp_code() == 200){
            params = (Map)result.getData();
        }else{
            return result;
        }
        list = sysClientDao.findList(params);
        PageResult pageResult = PageResult.<SysUser>builder().page(page).limit(limit).data(list).count((long)total).build();
        return Result.succeedWith(pageResult,CodeEnum.SUCCESS.getCode(),"成功");

    }
	public  SysClient getById(Long id) {
		return sysClientDao.getById(id);
	}

	@Override
	public List<SysClient> findList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return sysClientDao.findList(params);
	}

	@Override
	public Result updateEnabled(Map<String, Object> params) {
		Long id = MapUtils.getLong(params, "id");
		Boolean enabled = MapUtils.getBoolean(params, "status");

		SysClient client = sysClientDao.getById(id);
		if (client == null) {
            return Result.failedWith(null, CodeEnum.ERROR.getCode(),"应用不存在");
		}
		client.setStatus(enabled);

		int i = sysClientDao.update(client) ;
		
		ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(client.getClientId()); 
		
		if(enabled){
			redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).put(client.getClientId(), JSONObject.toJSONString(clientDetails));
		}else{
			redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).delete(client.getClientId()) ;
		}

		log.info("应用状态修改：{}", client);
		return i > 0 ? Result.succeedWith(client,CodeEnum.SUCCESS.getCode(),"更新成功") :
                Result.failedWith(null, CodeEnum.ERROR.getCode(),"更新失败");
	}

}
