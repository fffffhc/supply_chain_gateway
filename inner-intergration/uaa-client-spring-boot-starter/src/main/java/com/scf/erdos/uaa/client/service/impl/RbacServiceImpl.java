package com.scf.erdos.uaa.client.service.impl;
/**
 * 
 */

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.scf.erdos.uaa.client.dao.SysClientDao;
import com.scf.erdos.uaa.client.dao.SysServiceDao;
import com.scf.erdos.uaa.client.service.RbacService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

/**
 * @Description : API 级别权限认证
 * @author：bao-clm
 * @date: 2020/3/23
 * @version：1.0
 */

@Service("rbacService")
@SuppressWarnings("all")
public class RbacServiceImpl implements RbacService {

    @Resource
    private SysServiceDao sysServiceDao;
    
    @Resource
    private SysClientDao sysClientDao;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    /**
     * @param request        HttpServletRequest
     * @param authentication 认证信息
     * @return 是否有权限
     */
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Authentication user = SecurityContextHolder.getContext()
                .getAuthentication();
        
        //TODO 目前都是true
        boolean hasPermission = false;

        if (user != null) {

            if (user instanceof OAuth2Authentication) {

                OAuth2Authentication athentication = (OAuth2Authentication) user;

                String clientId = athentication.getOAuth2Request().getClientId();

                Map map = sysClientDao.getClient(clientId);

                if (map == null) {
                    return false;
                } else {
                    List<Map> list = sysServiceDao.listByClientId(Long.valueOf(String.valueOf(map.get("id"))));
                    for (Iterator<Map> it = list.iterator(); it.hasNext(); ) {
                        Map temp = it.next();

                        if (antPathMatcher.match( String.valueOf(temp.get("path")),request.getRequestURI())) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }

		return hasPermission;
	}

}
