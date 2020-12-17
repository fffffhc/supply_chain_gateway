package com.scf.erdos.user.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.constant.CompanyRole;
import com.scf.erdos.common.constant.UserType;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.CompanyInfoVo;
import com.scf.erdos.common.model.SysPermission;
import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.common.model.SysUser;
import com.scf.erdos.common.util.PageUtil;
import com.scf.erdos.common.util.SysUserUtil;
import com.scf.erdos.common.util.ValidatorUtil;
import com.scf.erdos.common.web.CodeEnum;
import com.scf.erdos.user.Utils.Alisms;
import com.scf.erdos.user.Utils.Util;
import com.scf.erdos.user.dao.SysUserDao;
import com.scf.erdos.user.dao.SysUserRoleDao;
import com.scf.erdos.user.feign.CompanyFeignClient;
import com.scf.erdos.user.model.SysUserExcel;
import com.scf.erdos.user.service.ISmsServer;
import com.scf.erdos.user.service.SysPermissionService;
import com.scf.erdos.user.service.SysUserService;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 作者 owen
 * @version 创建时间：2017年11月12日 上午22:57:51
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private SysPermissionService sysPermissionService;
	@Autowired
	private SysUserRoleDao userRoleDao;
	@Autowired(required = false)
	private TokenStore redisTokenStore;
	@Autowired
	private ISmsServer iValidateServer;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private Alisms alisms;
	@Value("${aliyun.sms.template.code2}")
	private String templateCode2;

	@Autowired
	private CompanyFeignClient companyFeignClient;

	@Transactional
	@Override
	public void addSysUser(SysUser sysUser)  throws ServiceException {
		try {
			String username = sysUser.getUsername();
			if (StringUtils.isBlank(username)) {
				throw new IllegalArgumentException("用户名不能为空");
			}

			if (ValidatorUtil.checkPhone(username)) {// 防止用手机号直接当用户名，手机号要发短信验证
				throw new IllegalArgumentException("用户名要包含英文字符");
			}

			if (username.contains("@")) {// 防止用邮箱直接当用户名，邮箱也要发送验证（暂未开发）
				throw new IllegalArgumentException("用户名不能包含@");
			}

			if (username.contains("|")) {
				throw new IllegalArgumentException("用户名不能包含|字符");
			}

			if (StringUtils.isBlank(sysUser.getPassword())) {
				throw new IllegalArgumentException("密码不能为空");
			}

			if (StringUtils.isBlank(sysUser.getNickname())) {
				sysUser.setNickname(username);
			}

			if (StringUtils.isBlank(sysUser.getType())) {
				sysUser.setType(UserType.COMPANY.name());
			}

			SysUser persistenceUser = sysUserDao.findByUsername(sysUser.getUsername());
			if (persistenceUser != null && persistenceUser.getUsername() != null) {
				throw new IllegalArgumentException("用户名已存在");

			}

			sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
			sysUser.setEnabled(Boolean.TRUE);
			sysUser.setCreateTime(new Date());
			sysUser.setUpdateTime(sysUser.getCreateTime());
			sysUserDao.save(sysUser);
			log.info("添加用户：{}", sysUser);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional
	@Override
	public SysUser updateSysUser(SysUser sysUser)  throws ServiceException {
		try {
			sysUser.setUpdateTime(new Date());

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication instanceof OAuth2Authentication) {
				OAuth2Authentication oAuth2Auth = (OAuth2Authentication) authentication;
				authentication = oAuth2Auth.getUserAuthentication();

				OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oAuth2Auth.getDetails();

				LoginAppUser user = SysUserUtil.getLoginAppUser();

				if (user != null) {

					if ( !ObjectUtils.notEqual(user.getId(),sysUser.getId()) ) {

						OAuth2AccessToken token = redisTokenStore.readAccessToken(details.getTokenValue());

						if (token != null) {

							if (!StringUtils.isBlank(sysUser.getHeadImgUrl())) {
								user.setHeadImgUrl(sysUser.getHeadImgUrl());
							}

							if (!StringUtils.isBlank(sysUser.getNewPassword())) {
								user.setPassword(sysUser.getNewPassword());
							}

							if (!StringUtils.isBlank(sysUser.getNewPassword())) {
								user.setPassword(sysUser.getNewPassword());
							}

							if (!StringUtils.isBlank(sysUser.getNickname())) {
								user.setNickname(sysUser.getNickname());
							}

							if (!StringUtils.isBlank(sysUser.getPhone())){
								user.setPhone(sysUser.getPhone());
							}

							if (sysUser.getSex() != null) {
								user.setSex(sysUser.getSex());
							}

							UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(user,
									null, user.getAuthorities());

							OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Auth.getOAuth2Request(), userAuthentication);
							oAuth2Authentication.setAuthenticated(true);
							redisTokenStore.storeAccessToken(token, oAuth2Authentication);

						}

					}

				}
			}

			sysUserDao.updateByOps(sysUser);
			log.info("修改用户：{}", sysUser);
			return sysUser;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional
	@Override
	public LoginAppUser findByUsername(String username)  throws ServiceException{
		try {
			SysUser sysUser = sysUserDao.findUserByUsername(username);
			if (sysUser != null) {
				LoginAppUser loginAppUser = new LoginAppUser();
				BeanUtils.copyProperties(sysUser, loginAppUser);

				Set<SysRole> sysRoles = userRoleDao.findRolesByUserId(sysUser.getId());
				loginAppUser.setSysRoles(sysRoles);// 设置角色

				if (!CollectionUtils.isEmpty(sysRoles)) {
					Set<Long> roleIds = sysRoles.parallelStream().map(r -> r.getId()).collect(Collectors.toSet());
					Set<SysPermission> sysPermissions = sysPermissionService.findByRoleIds(roleIds);
					if (!CollectionUtils.isEmpty(sysPermissions)) {
						Set<String> permissions = sysPermissions.parallelStream().map(p -> p.getPermission())
								.collect(Collectors.toSet());

						loginAppUser.setPermissions(permissions);// 设置权限集合
					}

				}

				return loginAppUser;
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		return null;
	}


	@Transactional
	@Override
	public LoginAppUser findByMobile(String mobile)  throws ServiceException {
		try {
			SysUser sysUser = sysUserDao.findUserByMobile(mobile);
			if (sysUser != null) {
				LoginAppUser loginAppUser = new LoginAppUser();
				BeanUtils.copyProperties(sysUser, loginAppUser);

				Set<SysRole> sysRoles = userRoleDao.findRolesByUserId(sysUser.getId());
				loginAppUser.setSysRoles(sysRoles);// 设置角色

				if (!CollectionUtils.isEmpty(sysRoles)) {
					Set<Long> roleIds = sysRoles.parallelStream().map(r -> r.getId()).collect(Collectors.toSet());
					Set<SysPermission> sysPermissions = sysPermissionService.findByRoleIds(roleIds);
					if (!CollectionUtils.isEmpty(sysPermissions)) {
						Set<String> permissions = sysPermissions.parallelStream().map(p -> p.getPermission())
								.collect(Collectors.toSet());

						loginAppUser.setPermissions(permissions);// 设置权限集合
					}

				}

				return loginAppUser;
			}

			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SysUser> findByUserIds(List<String> userIds) throws ServiceException {
		try {
			List<SysUser> list = sysUserDao.findByUserIds(userIds);
			return list;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public SysUser findByUserId(String userId) throws ServiceException {
		try {
			SysUser sysUser = sysUserDao.findByUserId(userId);
			return sysUser;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public SysUser findById(Long id)  throws ServiceException {
		try {
			return sysUserDao.findById(id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 给用户设置角色
	 */
	@Transactional
	@Override
	public void setRoleToUser(Long id, Set<Long> roleIds)  throws ServiceException {
		try {
			SysUser sysUser = sysUserDao.findById(id);
			if (sysUser == null) {

				throw new IllegalArgumentException("用户不存在");
			}

			userRoleDao.deleteUserRole(id, null);
			if (!CollectionUtils.isEmpty(roleIds)) {
				roleIds.forEach(roleId -> {
					userRoleDao.saveUserRoles(id, roleId);
				});
			}

			log.info("修改用户：{}的角色，{}", sysUser.getUsername(), roleIds);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional
	@Override
	public Result updatePassword(Long id, String oldPassword, String newPassword)  throws ServiceException {
		try {
			SysUser sysUser = sysUserDao.findById(id);
			if (StringUtils.isNoneBlank(oldPassword)) {
				if (!passwordEncoder.matches(oldPassword, sysUser.getPassword())) {
					return Result.failedWith(null, CodeEnum.ERROR.getCode(),"旧密码错误");
				}
			}

			SysUser user = new SysUser();
			user.setId(id);
			user.setPassword(passwordEncoder.encode(newPassword));

			updateSysUser(user);
			log.info("修改密码：{}", user);
			return Result.succeedWith(null, CodeEnum.SUCCESS.getCode(),"修改密码成功");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Result findUsers(Map<String, Object> params)  throws ServiceException {
		try {
			int total = sysUserDao.count(params);
			List<SysUser> list = Collections.emptyList();
			PageUtil pageUtil = new PageUtil();
			Result result = pageUtil.pageParamConver(params, true);
			Integer page = MapUtils.getInteger(params, "page");
			Integer limit = MapUtils.getInteger(params, "limit");
			if(result.getResp_code() == 200){
				params = (Map)result.getData();
			}else{
				return result;
			}

			/**
			 * 平台管理员获取所有用户
			 * 企业管理员只能获取本企业的用户
			 */
			LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
			String companyId = loginUser.getCompanyId();
			if(!StringUtils.isBlank(companyId)) {
				params.put("companyId",companyId);
			}

			list = sysUserDao.findList(params);

			/*List<Long> userIds = list.stream().map(SysUser::getId).collect(Collectors.toList());

				List<SysRoleMenus> sysRoles = userRoleDao.findRolesByUserIds(userIds);

				list.forEach(u -> {
					u.setRoles(sysRoles.stream().filter(r -> !ObjectUtils.notEqual(u.getId(), r.getUserId()))
							.collect(Collectors.toList()));
				});*/

			PageResult pageResult = PageResult.<SysUser>builder().page(page).limit(limit).data(list).count((long)total).build();
			return Result.succeed(pageResult,"成功");

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Set<SysRole> findRolesByUserId(Long userId)  throws ServiceException {
		try {
			return userRoleDao.findRolesByUserId(userId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Result updateEnabled(Map<String, Object> params)  throws ServiceException {
		try {
			Long id = MapUtils.getLong(params, "id");
			Boolean enabled = MapUtils.getBoolean(params, "enabled");
			SysUser appUser = sysUserDao.findById(id);
			if (appUser == null) {
				return Result.failed(null,"用户不存在");
			}
			appUser.setEnabled(enabled);
			appUser.setUpdateTime(new Date());

			int i = sysUserDao.updateByOps(appUser);
			log.info("修改用户：{}", appUser);
			return i > 0 ? Result.succeed(null,"用户状态修改成功"):
					Result.failed(null,"用户状态修改失败");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional
	@Override
	public Result saveOrUpdate(SysUser sysUser)  throws ServiceException {
		try {
			// 身份证号加密
			/*String idNum = sysUser.getIdNum();
			if(ValidatorUtil.checkIDNum(idNum)){
				sysUser.setIdNum(AESUtils.encryption(idNum));
			}*/
			int i = 0;
			if (sysUser.getId() == null) {
				//获取企业id，判断是企业用户或者平台用户
				LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
				String companyId = loginUser.getCompanyId();
				if(StringUtils.isBlank(companyId)){
					sysUser.setType(UserType.PLATFORM.name());
				}else{
					sysUser.setCompanyId(companyId);
					sysUser.setType(UserType.COMPANY.name());
				}
				sysUser.setCreateTime(new Date());
				//密码加密
				sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
				sysUser.setEnabled(Boolean.TRUE);

				SysUser persistenceUser = sysUserDao.findByUsername(sysUser.getUsername());
				if (persistenceUser != null && persistenceUser.getUsername() != null) {
					return Result.failed(null,"用户名已存在");
				}
				sysUser.setUpdateTime(sysUser.getCreateTime());
				i = sysUserDao.save(sysUser);
			} else {
				sysUser.setUpdateTime(new Date());
				i = sysUserDao.updateByOps(sysUser);
			}

			userRoleDao.deleteUserRole(sysUser.getId(), null);
			userRoleDao.saveUserRoles(sysUser.getId(), Long.parseLong(sysUser.getRoleId()));

			return Result.succeed("操作成功");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional
	public Result register(Map<String, Object> params) throws ServiceException {
		try {
			String smsCodeIn = (String)params.get("smsCode");
			String smsCode =   redisTemplate.opsForValue().get("sms:" + params.get("phone"));
			if(!smsCodeIn.equals(smsCode)){
				return Result.failedWith(null,1000,"手机短信验证码错误");
			}

			/**
			 * 企业名称，统一社会信用代码，登陆账号，手机号
			 * 不能重复注册
			 */
			String username = (String)params.get("username");
			String companyName = (String)params.get("companyName");
			String creditNo = (String)params.get("creditNo");
			String phone = (String)params.get("phone");

			SysUser sysUser1 = sysUserDao.findByUsername(username);
			if(sysUser1 != null){
				return Result.failedWith(null,1001,"登陆账号已存在");
			}
			SysUser sysUser2 = sysUserDao.findByCompanyName(companyName);
			if(sysUser2 != null){
				return Result.failedWith(null,1002,"该企业已注册");
			}
			SysUser sysUser3 = sysUserDao.findByCreditNo(creditNo);
			if(sysUser3 != null){
				return Result.failedWith(null,1003,"该统一社会信用代码已注册");
			}
			SysUser sysUser4 = sysUserDao.findUserByMobile(phone);
			if(sysUser4 != null){
				return Result.failedWith(null,1004,"手机号不能重复");
			}

			/**
			 * 密码加密
			 */
			String password = (String)params.get("password");
			SysUser sysUser = new SysUser();
			sysUser.setPassword(passwordEncoder.encode(password));
			sysUser.setEnabled(Boolean.TRUE);
			sysUser.setUsername(username);
			sysUser.setCompanyName(companyName);
			sysUser.setCreditNo(creditNo);
			sysUser.setContacter((String)params.get("contacter"));
			sysUser.setPhone((String)params.get("phone"));
			sysUser.setCreateTime(new Date());
			sysUser.setType(UserType.COMPANYADMIN.name());

			int i = sysUserDao.insertCompanyUser(sysUser);

			/**
			 * 保存成功后发送短信：邀请码短信
			 */
			String templateCode = templateCode2;
			String code = Util.randomCode(4);
			params.put("smsCode",code);
			params.put("templateCode",templateCode);

			//保存邀请码 到 redis
			redisTemplate.opsForValue().set("yqm" + phone,code);
			alisms.sendSms(params);
			return i > 0 ? Result.succeed("成功"):Result.failed("失败");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Result getInvitationCode(Map<String, Object> params) throws ServiceException {
		try {
			String username = params.get("username").toString();
			boolean bl = redisTemplate.hasKey("yqm" + username);
			if(bl){
				return Result.succeed(true,"初次登陆用户");
			}else{
				return Result.succeed(false,"不是初次登陆用户");
			}
		} catch (BeansException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SysUserExcel> findAllUsers(Map<String, Object> params)  throws ServiceException {
		try {
			List<SysUserExcel> sysUserExcels = new ArrayList<>();

			PageUtil pageUtil = new PageUtil();
			Result result = pageUtil.pageParamConver(params, true);

			if(result.getResp_code() == 200){
				params = (Map)result.getData();
			}
			List<SysUser> list = sysUserDao.findList(params);

			for (SysUser sysUser : list){
				SysUserExcel sysUserExcel = new SysUserExcel();
				BeanUtils.copyProperties(sysUser,sysUserExcel);
				sysUserExcels.add(sysUserExcel);
			}
			return sysUserExcels;
		} catch (BeansException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SysRole> getRoles() {
		return sysUserDao.getRoles();
	}

	@Override
	public List<SysRole> getRolesByCode() {

		LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
		Set<SysRole> roleSet = loginUser.getSysRoles();
		Iterator it1 = roleSet.iterator();
		/**
		 * 用户角色只有一个
		 */
		SysRole sr = new SysRole();
		while(it1.hasNext()){
			sr = (SysRole)it1.next();
		}
		/*Map map = new HashMap();
		map.put("code",sr.getCode());
		map.put("length",sr.getCode().length());*/
		return sysUserDao.getRolesByCode(sr.getCode());
	}

	@Override
	public Result getLoginAppUser() throws ServiceException {
		LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
		CompanyInfoVo companyInfoVo = companyFeignClient.findConpanyInfoByUserId(loginUser.getId());
        loginUser.setCompanyInfoVo(companyInfoVo);
		return  Result.succeed(loginUser,"获取成功");
	}

	@Override
	@Transactional
	public boolean setCompanyRole(Map<String, Object> params) {
		sysUserDao.updateCompanyUser(params);

		String roleCode = "";
		String companyRole = params.get("companyLabel1").toString();
		switch(companyRole)
			{
			case CompanyRole.COMPANY_10000:
				roleCode = CompanyRole.COMPANY_10000_01_ROLE;//一般企业（融资/确权） - 一岗通
				break;
			case CompanyRole.COMPANY_10001:
				roleCode = CompanyRole.COMPANY_10001_01_ROLE;//一般企业（融资方） - 一岗通
				break;
			case CompanyRole.COMPANY_10002:
				roleCode = CompanyRole.COMPANY_10002_01_ROLE;//一般企业（确权方）- 一岗通
				break;
			case CompanyRole.COMPANY_10003:
				roleCode = CompanyRole.COMPANY_10003_01_ROLE;//担保机构 - 一岗通
				break;
			case CompanyRole.COMPANY_10004:
				roleCode = CompanyRole.COMPANY_10004_01_ROLE;//资金机构 - 一岗通
				break;
			case CompanyRole.COMPANY_10005:
				roleCode = CompanyRole.COMPANY_10005_01_ROLE;//其它服务机构 - 一岗通
				break;
			default:
				roleCode = "";
				break;
			}
		params.put("roleCode",roleCode);
		sysUserDao.deleteUserRole(params);
		int i = sysUserDao.setRole(params);
		return i > 0 ? true:false;
	}

	@Override
	public List<SysRole> getRoles(String code) {
		List<SysRole> roleList = userRoleDao.getRoles(code);

		return roleList;
	}

}