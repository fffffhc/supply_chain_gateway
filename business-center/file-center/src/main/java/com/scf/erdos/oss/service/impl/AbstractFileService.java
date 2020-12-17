package com.scf.erdos.oss.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.scf.erdos.common.model.SysUser;
import com.scf.erdos.common.util.PageUtil;
import com.scf.erdos.common.web.CodeEnum;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.oss.model.FileInfo;
import com.scf.erdos.oss.model.FileType;
import com.scf.erdos.oss.utils.FileUtil;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.oss.dao.FileDao;
import com.scf.erdos.oss.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description : AbstractFileService 抽取类
 * 根据filetype 实例化具体oss对象
 * @author：bao-clm
 * @date: 2020/3/23
 * @version：1.0
 */
@Slf4j
@SuppressWarnings("all")
public abstract class AbstractFileService implements FileService {

    protected abstract FileDao getFileDao();

    protected static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public FileInfo upload(MultipartFile file) throws Exception {
        FileInfo fileInfo = FileUtil.getFileInfo(file);
        FileInfo oldFileInfo = getFileDao().getById(fileInfo.getId());
        if (oldFileInfo != null) {
            return oldFileInfo;
        }

        if (!fileInfo.getName().contains(".")) {
            throw new IllegalArgumentException("缺少后缀名");
        }

        uploadFile(file, fileInfo);

        fileInfo.setSource(fileType().name());// 设置文件来源
        getFileDao().save(fileInfo);// 将文件信息保存到数据库
//		// 本地保存文件
//		FileUtil.saveFile(file,fileInfo.getPath());
        log.info("上传文件：{}", fileInfo);

        return fileInfo;
    }

    /**
     * 文件来源
     *
     * @return
     */
    protected abstract FileType fileType();

    /**
     * 上传文件
     *
     * @param file
     * @param fileInfo
     */
    protected abstract void uploadFile(MultipartFile file, FileInfo fileInfo) throws Exception;

    @Override
    public void delete(FileInfo fileInfo) {
        deleteFile(fileInfo);
        getFileDao().delete(fileInfo.getId());
        log.info("删除文件：{}", fileInfo);
    }

    /**
     * 删除文件资源
     *
     * @param fileInfo
     * @return
     */
    protected abstract boolean deleteFile(FileInfo fileInfo);

    @Override
    public FileInfo getById(String id) {
        return getFileDao().getById(id);
    }

    public Result findList(Map<String, Object> params) {
        int total = getFileDao().count(params);
        List list = Collections.emptyList();
        PageUtil pageUtil = new PageUtil();
        Result result = pageUtil.pageParamConver(params, true);
        Integer page = MapUtils.getInteger(params, "page");
        Integer limit = MapUtils.getInteger(params, "limit");
        if(result.getResp_code() == 200){
            params = (Map)result.getData();
        }else{
            return result;
        }
        list = getFileDao().findList(params);
        PageResult pageResult = PageResult.<SysUser>builder().page(page).limit(limit).data(list).count((long)total).build();
        return Result.succeed(pageResult,"成功");
    }

    @Override
    public void unZip(String filePath, String descDir) throws RuntimeException {

    }
}
