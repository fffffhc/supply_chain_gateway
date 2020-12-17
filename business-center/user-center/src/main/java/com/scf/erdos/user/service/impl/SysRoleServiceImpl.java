package com.scf.erdos.user.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.scf.erdos.common.constant.CompanyRole;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysMenu;
import com.scf.erdos.common.model.SysPermission;
import com.scf.erdos.common.model.SysRole;

import com.scf.erdos.common.web.CodeEnum;
import com.scf.erdos.user.dao.*;
import com.scf.erdos.user.model.SysMenuTree;
import com.scf.erdos.user.model.SysRoleMenus;
import com.scf.erdos.user.service.SysRoleService;
import com.scf.erdos.common.web.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@SuppressWarnings("all")
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysUserRoleDao userRoleDao;
	@Autowired
	private SysRolePermissionDao rolePermissionDao;
	@Autowired
	private SysMenuDao menuDao;

	@Autowired
	private SysRoleMenuDao roleMenuDao;


	@Transactional
	@Override
	public void save(SysRole sysRole)  throws ServiceException {
		try {
			SysRole role = sysRoleDao.findByCode(sysRole.getCode());
			if (role != null) {
				throw new IllegalArgumentException("角色code已存在");
			}

			sysRole.setCreateTime(new Date());
			sysRole.setUpdateTime(sysRole.getCreateTime());

			sysRoleDao.save(sysRole);
			log.info("保存角色：{}", sysRole);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional
	@Override
	public void update(SysRole sysRole) throws ServiceException {
		try {
			sysRole.setUpdateTime(new Date());

			sysRoleDao.updateByOps(sysRole);
			log.info("修改角色：{}", sysRole);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional
	@Override
	public void deleteRole(Long id)  throws ServiceException {
		try {
			SysRole sysRole = sysRoleDao.findById(id);

			sysRoleDao.delete(id);
			rolePermissionDao.deleteRolePermission(id, null);
			roleMenuDao.delete(id, null) ;
			userRoleDao.deleteUserRole(null, id);



			log.info("删除角色：{}", sysRole);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Transactional
	@Override
	public void setPermissionToRole(Long roleId, Set<Long> permissionIds)  throws ServiceException {
		try {
			SysRole sysRole = sysRoleDao.findById(roleId);
			if (sysRole == null) {
				throw new IllegalArgumentException("角色不存在");
			}

			// 查出角色对应的old权限
			Set<Long> oldPermissionIds = rolePermissionDao.findPermissionsByRoleIds(Sets.newHashSet(roleId)).stream()
					.map(p -> p.getId()).collect(Collectors.toSet());

			// 需要添加的权限
			Collection<Long> addPermissionIds = org.apache.commons.collections4.CollectionUtils.subtract(permissionIds,
					oldPermissionIds);
			if (!CollectionUtils.isEmpty(addPermissionIds)) {
				addPermissionIds.forEach(permissionId -> {
					rolePermissionDao.saveRolePermission(roleId, permissionId);
				});
			}
			// 需要移除的权限
			Collection<Long> deletePermissionIds = org.apache.commons.collections4.CollectionUtils
					.subtract(oldPermissionIds, permissionIds);
			if (!CollectionUtils.isEmpty(deletePermissionIds)) {
				deletePermissionIds.forEach(permissionId -> {
					rolePermissionDao.deleteRolePermission(roleId, permissionId);
				});
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public SysRole findById(Long id)  throws ServiceException{
		try {
			return sysRoleDao.findById(id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Result findRoles(Map<String, Object> params)  throws ServiceException {
		try {
			/*Integer page = MapUtils.getInteger(params, "page");
			Integer limit = MapUtils.getInteger(params, "limit");

			if (page == null || limit == null) {
				return Result.failed("page，limit不能为空！");
			}

			if(page < 1){
				return Result.failed("page不能小于1，首页从1开始！");
			}

			params.put("currentPage",(page - 1)*limit);
			params.put("pageSize",limit);

			int total = sysRoleDao.count(params);*/
			List<SysRole> list = sysRoleDao.findList(params);
			/**
			 * 企业标签 - 企业角色设置 start
			 */
			List<SysRole> newList = new ArrayList<>();
			for(SysRole sysRole : list){
				String companyLabel = sysRole.getCompanyLabel();
				String companyLabelName = "";
				if(StringUtils.isNotEmpty(companyLabel)){
					switch (sysRole.getCompanyLabel()) {
						case CompanyRole.COMPANY_10000:
							companyLabelName = "一般企业（融资/确权）";
							break;
						case CompanyRole.COMPANY_10001:
							companyLabelName = "一般企业（融资/确权）";
							break;
						case CompanyRole.COMPANY_10002:
							companyLabelName = "一般企业（确权方）";
							break;
						case CompanyRole.COMPANY_10003:
							companyLabelName = "担保机构";
							break;
						case CompanyRole.COMPANY_10004:
							companyLabelName = "资金机构";
							break;
						case CompanyRole.COMPANY_10005:
							companyLabelName = "其它服务机构";
							break;
					}
				}else{
					companyLabelName = "系统管理员";
				}

				sysRole.setCompanyLabelName(companyLabelName);
				newList.add(sysRole);
			}
			/**
			 * 企业标签 - 企业角色设置 end
			 */

			//PageResult pageResult = PageResult.<SysRoleMenus>builder().page(page).limit(limit).data(newList).count((long)total).build();
			return Result.succeedWith(newList,CodeEnum.SUCCESS.getCode(),"成功");

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Set<SysPermission> findPermissionsByRoleId(Long roleId)  throws ServiceException {
		try {
			return rolePermissionDao.findPermissionsByRoleIds(Sets.newHashSet(roleId));
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Result saveOrUpdate(SysRole sysRole)  throws ServiceException {
		try {
			int i = 0;
			if (sysRole.getId()==null){
				SysRole role = sysRoleDao.findByCode(sysRole.getCode());
				if (role != null) {
					return Result.failed("角色code已存在");
				}
				sysRole.setCreateTime(new Date());
				sysRole.setUpdateTime(sysRole.getCreateTime());
				i = sysRoleDao.save(sysRole);
			}else {
				sysRole.setUpdateTime(new Date());
				i = sysRoleDao.updateByOps(sysRole);
			}
			return i>0?Result.succeed("操作成功"):Result.failed("操作失败");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional
	public Result saveOrUpdateV1_1(SysRole sysRole) throws ServiceException {
		try {
			if (sysRole.getId()==null){
				SysRole role = sysRoleDao.findBySysRole(sysRole);
				if (role != null) {
					return Result.failed("角色已存在");
				}

				SysRole r = sysRoleDao.findCodeSysRole(sysRole);
				if(r != null){
					String code = r.getCode();
					sysRole.setCode(Integer.parseInt(code) + 1 + "");
				}else{
					sysRole.setCode(Integer.parseInt(sysRole.getCompanyLabel()) + 1 + "");
				}

				sysRole.setCreateTime(new Date());
				sysRoleDao.save(sysRole);
				sysRoleDao.saveRoleMenus(sysRole);
			}else {
				sysRole.setUpdateTime(new Date());
				sysRoleDao.deleteMenus(sysRole.getId());//删除角色跟菜单关联关系
				sysRoleDao.saveRoleMenus(sysRole);


				SysRole r = sysRoleDao.findCodeSysRole(sysRole);
				if(r != null){
					String code = r.getCode();
					sysRole.setCode(Integer.parseInt(code) + 1 + "");
				}else{
					sysRole.setCode(Integer.parseInt(sysRole.getCompanyLabel()) + 1 + "");
				}
				sysRoleDao.updateByOps(sysRole);
			}
			return Result.succeed("操作成功");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Result getRoleInfo(SysRole sysRole) throws ServiceException {
		try {
			SysRoleMenus sysRoleMenus = sysRoleDao.getRoleInfo(sysRole);

			Set<Long> roleIds = new HashSet<Long>();
			//初始化角色
			roleIds.add(sysRole.getId());
			List<SysMenu> list = new ArrayList<SysMenu>();
			List<SysMenu> roleMenus = roleMenuDao.findMenusByRoleIds(roleIds); // 获取该角色绑定的菜单
			List<SysMenu> allMenus = menuDao.findAll(); // 全部的菜单列表

			//封装应用绑定的服务
			for(SysMenu sysMenu : allMenus){
				boolean bl = false;
				for(SysMenu menu : roleMenus){
					if (sysMenu.getId() == menu.getId()){
						bl = true;
						break;
					}
				}
				sysMenu.setChecked(bl);
				list.add(sysMenu);
			}

			List<SysMenuTree> treeDataList = getTreeList(list, -1L);
			sysRoleMenus.setTreeDataList(treeDataList);
			return Result.succeed(sysRoleMenus,"操作成功");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	//获取某个父级的树形结构数据
	public static List<SysMenuTree> getTreeList(List<SysMenu> list, Long parentId) {
		List<SysMenuTree> treeList = new ArrayList<>();
		List<SysMenu> childList = list.stream().filter(i -> i.getParentId().equals(parentId)).sorted(Comparator.comparing(SysMenu::getSort)).collect(Collectors.toList());
		for (SysMenu item : childList) {
			SysMenuTree m = new SysMenuTree();
			m.setId(item.getId());
			m.setName(item.getName());
			m.setParentId(item.getParentId());
			m.setSort(item.getSort());
			m.setChecked(item.getChecked());
			m.setChildren(getTreeList(list, item.getId()));
			treeList.add(m);
		}
		return treeList;
	}

}
