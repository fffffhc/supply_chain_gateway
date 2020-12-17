/**
 * 
 */
package com.scf.erdos.uaa.client.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2020/3/23
 * @version：1.0
 */
public interface RbacService {
	
	boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
