package com.scf.erdos.oss.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.scf.erdos.oss.model.FileType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.scf.erdos.oss.service.FileService;


/**
 * @Description : 将各个实现类放入map
 * @author：bao-clm
 * @date: 2020/3/23
 * @version：1.0
 */
@Configuration
public class OssServiceFactory {

	private Map<FileType, FileService> map = new HashMap<>();

	@Autowired
	private FileService aliyunOssServiceImpl;
	
	@Autowired
	private FileService qiniuOssServiceImpl;

	@Autowired
	private FileService fastDfsOssServiceImpl;

	@Autowired
	private FileService localOssServiceImpl;

	@PostConstruct
	public void init() {
		map.put(FileType.ALIYUN,  aliyunOssServiceImpl);
		map.put(FileType.QINIU ,  qiniuOssServiceImpl);
		map.put(FileType.LOCAL ,  localOssServiceImpl);
		map.put(FileType.FASTDFS ,  fastDfsOssServiceImpl);
	}

	public FileService getFileService(String fileType) {
	   if (StringUtils.isBlank(fileType)) {
			return localOssServiceImpl;
		}

		return map.get(FileType.valueOf(fileType));
	}
}
