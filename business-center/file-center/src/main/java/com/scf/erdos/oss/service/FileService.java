package com.scf.erdos.oss.service;

import java.util.Map;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.oss.model.FileInfo;
import com.scf.erdos.common.web.PageResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description : 文件service 目前仅支持阿里云oss,七牛云
 * @author：bao-clm
 * @date: 2020/3/23
 * @version：1.0
 */
public interface FileService {

	FileInfo upload(MultipartFile file ) throws Exception;

	void delete(FileInfo fileInfo);
	
	FileInfo getById(String id);
	
	Result findList(Map<String, Object> params);

	void unZip(String filePath, String descDir) throws RuntimeException ;
}
