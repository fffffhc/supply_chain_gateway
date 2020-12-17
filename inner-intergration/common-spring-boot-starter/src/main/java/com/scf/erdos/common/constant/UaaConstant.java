package com.scf.erdos.common.constant;

/**
 * @Description : 认证授权常用 KEY
 * @author：bao-clm
 * @date: 2019/1/12
 * @version：1.0
 */
public class UaaConstant {

	 /**
     * 缓存client的redis key，这里是hash结构存储
     */
    public static final String CACHE_CLIENT_KEY = "oauth_client_details";
    
    public static final String TOKEN_PARAM = "access_token" ;
    
    public static final String TOKEN_HEADER = "accessToken" ;
    
    public static final String AUTH = "auth" ;
    
    public static final String TOKEN = "token" ;
    
    public static final String Authorization = "Authorization" ;
    
}
