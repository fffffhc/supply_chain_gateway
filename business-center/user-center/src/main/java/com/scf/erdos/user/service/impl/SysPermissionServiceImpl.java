package com.scf.erdos.user.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysPermission;
import com.scf.erdos.common.model.SysUser;
import com.scf.erdos.common.util.PageUtil;
import com.scf.erdos.common.web.CodeEnum;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.user.dao.SysPermissionDao;
import com.scf.erdos.user.dao.SysRolePermissionDao;
import com.scf.erdos.user.service.SysPermissionService;
import com.scf.erdos.common.web.PageResult;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class SysPermissionServiceImpl implements SysPermissionService {

	@Autowired
	private SysPermissionDao sysPermissionDao;
	@Autowired
	private SysRolePermissionDao rolePermissionDao;

	@Override
	public Set<SysPermission> findByRoleIds(Set<Long> roleIds)  throws ServiceException {
		try {
			return rolePermissionDao.findPermissionsByRoleIds(roleIds);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional
	@Override
	public void save(SysPermission sysPermission)  throws ServiceException {
		try {
			SysPermission permission = sysPermissionDao.findByPermission(sysPermission.getPermission());
			if (permission != null) {
				throw new IllegalArgumentException("权限标识已存在");
			}
			sysPermission.setCreateTime(new Date());
			sysPermission.setUpdateTime(sysPermission.getCreateTime());

			sysPermissionDao.insert(sysPermission);
			log.info("保存权限标识：{}", sysPermission);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional
	@Override
	public void update(SysPermission sysPermission)  throws ServiceException {
		try {
			sysPermission.setUpdateTime(new Date());
			sysPermissionDao.updateByOps(sysPermission);
			log.info("修改权限标识：{}", sysPermission);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional
	@Override
	public void delete(Long id)  throws ServiceException {
		try {
			SysPermission permission = sysPermissionDao.findById(id);
			if (permission == null) {
				throw new IllegalArgumentException("权限标识不存在");
			}

			sysPermissionDao.deleteOps(id);
			rolePermissionDao.deleteRolePermission(null, id);
			log.info("删除权限标识：{}", permission);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Result findPermissions(Map<String, Object> params)  throws ServiceException {
		try {
			int total = sysPermissionDao.count(params);
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
			list = sysPermissionDao.findList(params);
			PageResult pageResult = PageResult.<SysUser>builder().page(page).limit(limit).data(list).count((long)total).build();
			return Result.succeedWith(pageResult, CodeEnum.SUCCESS.getCode(),"成功");
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public void setAuthToRole(Long roleId, Set<Long> authIds)  throws ServiceException {
		try {
			rolePermissionDao.deleteRolePermission(roleId,null);

			if (!CollectionUtils.isEmpty(authIds)) {
				authIds.forEach(authId -> {
					rolePermissionDao.saveRolePermission(roleId, authId);
				});
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public List<Map<String, Object>> findAuthByRoleId(Long roleId) {

		List<Map<String, Object>> authTrees = new ArrayList<>();
		Set<Long> roleIds = new HashSet<Long>();
		//初始化角色
		roleIds.add(roleId);

		Set<SysPermission> roleAuths = rolePermissionDao.findPermissionsByRoleIds(roleIds);//根据roleId获取对应的权限
		List<SysPermission> list = sysPermissionDao.findPermission();
		Map<Long, SysPermission> roleAuthsMap = roleAuths.stream().collect(Collectors.toMap(SysPermission::getId, SysPermission -> SysPermission));

		for (SysPermission sysPermission : list) {
			Map<String, Object> authTree = new HashMap<>();
			authTree.put("id", sysPermission.getId() + "");
			authTree.put("name", sysPermission.getName());
			authTree.put("open", true);
			authTree.put("checked", false);
			if (roleAuthsMap.get(sysPermission.getId()) != null) {
				authTree.put("checked", true);
			}
			authTrees.add(authTree);
		}

		return authTrees;
	}
}
