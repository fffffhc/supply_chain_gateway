package com.scf.erdos.common.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.scf.erdos.common.constant.UaaConstant;
import com.scf.erdos.common.model.SysRole;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.scf.erdos.common.auth.details.LoginAppUser;

import cn.hutool.core.bean.BeanUtil;

/**
 * @Description : 获取用户信息
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
@SuppressWarnings("all") 
public class SysUserUtil {

	/**
	 * 获取登陆的 LoginAppUser
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static LoginAppUser getLoginAppUser() {
		
		// 当OAuth2AuthenticationProcessingFilter设置当前登录时，直接返回
		// 强认证时处理
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof OAuth2Authentication) {
			OAuth2Authentication oAuth2Auth = (OAuth2Authentication) authentication;
			authentication = oAuth2Auth.getUserAuthentication();

			if (authentication instanceof UsernamePasswordAuthenticationToken) {
				UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
				
				if(authenticationToken.getPrincipal() instanceof LoginAppUser){
					return (LoginAppUser) authenticationToken.getPrincipal();
				}else if (authenticationToken.getPrincipal() instanceof Map){
					
					LoginAppUser loginAppUser = BeanUtil.mapToBean((Map) authenticationToken.getPrincipal(), LoginAppUser.class, true);
					Set<SysRole> roles = new HashSet<>();
					
					for(Iterator<SysRole> it = loginAppUser.getSysRoles().iterator(); it.hasNext();){
						SysRole role =  BeanUtil.mapToBean((Map) it.next() , SysRole.class, false);
						roles.add(role) ;
					}
					loginAppUser.setSysRoles(roles); 
					return loginAppUser;
				}
				
				
			} else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
				// 刷新token方式
				PreAuthenticatedAuthenticationToken authenticationToken = (PreAuthenticatedAuthenticationToken) authentication;
				return (LoginAppUser) authenticationToken.getPrincipal();

			}
		}

		// 弱认证处理，当内部服务，不带token时，内部服务
		String accessToken = TokenUtil.getToken();
		
		if(accessToken!=null){
			RedisTemplate redisTemplate = SpringUtils.getBean(RedisTemplate.class);
			LoginAppUser  loginAppUser= (LoginAppUser) redisTemplate.opsForValue()
					.get(UaaConstant.TOKEN + ":" + accessToken);
			if (loginAppUser != null) {
				return  loginAppUser ;
			}
		}
		
		

		return null;
	}
}
