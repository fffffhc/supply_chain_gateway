package com.scf.erdos.oss.model;

/**
 * @Description : OSS存储服务；支持阿里云oss、七牛oss、本地，fastdfs四种配置
 *
 * @author：bao-clm
 * @date: 2020/3/23
 * @version：1.0
 */
public enum FileType {
	// 七牛
	QINIU ,
	// 阿里云
	ALIYUN,
	// 本地存储
	LOCAL, 
	// fastdfs存储
	FASTDFS
}
