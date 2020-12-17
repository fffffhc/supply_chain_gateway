package com.scf.erdos.user.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysMenu;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.user.dao.SysMenuDao;
import com.scf.erdos.user.dao.SysRoleMenuDao;
import com.scf.erdos.user.model.SysMenuTree;
import com.scf.erdos.user.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@SuppressWarnings("all")
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao menuDao;
 	@Autowired
	private SysRoleMenuDao roleMenuDao;

	@Transactional
	@Override
	public void save(SysMenu menu)  throws ServiceException {
		try {
			menu.setCreateTime(new Date());
			menu.setUpdateTime(menu.getCreateTime());

			menuDao.save(menu);
			log.info("新增菜单：{}", menu);
		} catch (Exception e) {
			throw new ServiceException(e) ;
		}
	}

	@Transactional
	@Override
	public void update(SysMenu menu)  throws ServiceException {
		try {
			menu.setUpdateTime(new Date());

			menuDao.updateByOps(menu);
			log.info("修改菜单：{}", menu);
		} catch (Exception e) {
			throw new ServiceException(e) ;
		}
	}

	@Transactional
	@Override
	public void delete(Long id)  throws ServiceException{
		try {
			SysMenu menu = menuDao.findById(id);

			menuDao.deleteByParentId(menu.getId());
			menuDao.delete(id);

			log.info("删除菜单：{}", menu);
		} catch (Exception e) {
			throw new ServiceException(e) ;
		}
	}

	@Transactional
	@Override
	public void setMenuToRole(Long roleId, Set<Long> menuIds)  throws ServiceException {
		try {
			roleMenuDao.delete(roleId, null);

			if (!CollectionUtils.isEmpty(menuIds)) {
				menuIds.forEach(menuId -> {
					roleMenuDao.save(roleId, menuId);
				});
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SysMenu> findByRoles(Set<Long> roleIds)  throws ServiceException{
		try {
			return roleMenuDao.findMenusByRoleIds(roleIds);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SysMenu> findAll()  throws ServiceException{
		try {
			return menuDao.findAll();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public SysMenu findById(Long id)  throws ServiceException{
		try {
			return menuDao.findById(id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Set<Long> findMenuIdsByRoleId(Long roleId)  throws ServiceException{
		try {
			return roleMenuDao.findMenuIdsByRoleId(roleId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SysMenu> findOnes()  throws ServiceException{
		try {
			return menuDao.findOnes();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Result findMenusByRoleId(Long roleId) {

		Set<Long> roleIds = new HashSet<Long>();
		//初始化角色
		roleIds.add(roleId);
		List<SysMenu> list = new ArrayList<SysMenu>();
		List<SysMenu> roleMenus = roleMenuDao.findMenusByRoleIds(roleIds); // 获取该角色绑定的菜单
		List<SysMenu> allMenus = menuDao.findAll(); // 全部的菜单列表

		//封装应用绑定的服务
		for(SysMenu sysMenu : allMenus){
			for(SysMenu menu : roleMenus){
				if (sysMenu.getId() == menu.getId()){
					sysMenu.setChecked(true);
				}else{
					sysMenu.setChecked(false);
				}
			}
			list.add(sysMenu);
		}

		List<SysMenuTree> treeDataList = getTreeList(list, -1L);
		return Result.succeed(treeDataList,"获取成功");
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
