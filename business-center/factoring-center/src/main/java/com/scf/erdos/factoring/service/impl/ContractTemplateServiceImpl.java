package com.scf.erdos.factoring.service.impl;

import com.alibaba.fastjson.JSON;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.util.StringUtil;
import com.scf.erdos.common.web.PageParamHandle;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.ContractTemplateDao;
import com.scf.erdos.factoring.model.contract.ContractTemplate;
import com.scf.erdos.factoring.service.IContractTemplateService;
import com.scf.erdos.factoring.vo.contract.ContractTemplateVo;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scf.erdos.factoring.contract.util.OSSTool.uploadFileByOSS;

/**
 * @Description : 合同模板
 * @author：bao-clm
 * @date: 2020/9/1
 * @version：1.0
 */

@Service
@SuppressWarnings("all")
public class ContractTemplateServiceImpl implements IContractTemplateService {
    @Autowired
    private PageParamHandle pageParamHandle;
    @Autowired
    private ContractTemplateDao contractTemplateDao;

    @Override
    public Result getContractTemplates(Map<String, Object> map) throws ServiceException {

        Result result = pageParamHandle.handle(map);
        if(200 == result.getResp_code()) {
            Map<String, Object> param = (Map<String, Object>) result.getData();
            int total = contractTemplateDao.count(param);
            List<ContractTemplateVo> list = contractTemplateDao.getContractTemplates(param);

            int page = Integer.parseInt(param.get("page").toString());
            int limit = Integer.parseInt(param.get("limit").toString());
            PageResult pageResult = PageResult.<ContractTemplateVo>builder().page(page).limit(limit).data(list).count((long)total).build();
            return Result.succeed(pageResult,"成功");
        }else{
            return result;
        }
    }

    @Override
    @Transactional
    public Result saveOrUpdate(ContractTemplate contractTemplate, MultipartFile file) throws ServiceException, IOException, DocumentException {
        //1，获取当前用户信息
        /*LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
        if (loginUser == null)
            return Result.failed("获取当前用户登陆信息失败");
        contractTemplate.setUserId(loginUser.getId());
        contractTemplate.setCompanyId(loginUser.getCompanyId());*/

        contractTemplate.setUserId(1L);
        contractTemplate.setCompanyId("1");
        contractTemplate.setType(1);//合同类型（1，融资；2，置换）

        //2，判断合同名称是否重复
        String contractName = contractTemplateDao.getContractNameByName(contractTemplate);
        if(StringUtil.isNotEmpty(contractName)){
            return Result.failedWith(null,-1001,"合同名称重复");
        }

        //3，xml文档处理
        if(file.isEmpty()){
            return Result.failedWith(null,-1001,"文件不存在，请重新上传！");
        }
        String tmpFileName = UUID.randomUUID().toString().replace("_","");
        File filePath = new File("tmp/source/");
        File tmpFile = new File(filePath,tmpFileName);//创建一个File
        //file.transferTo(tmpFile);
        FileUtils.copyInputStreamToFile(file.getInputStream(), tmpFile);//MultipartFile 转 File

        SAXReader reader = new SAXReader();
        Document document = reader.read(tmpFile.getAbsoluteFile());
        String xmlStr = document.asXML();//xml文件 转 String
        contractTemplate.setSourceHtml(xmlStr);

        String sourseHtml = contractTemplate.getSourceHtml();
        String itemStr = extractVarByRegular(sourseHtml);
        contractTemplate.setVarGroupListStr(itemStr);//xml 获取所有的占位符参数包装成json字符串

        //4，OSS 保存合同模板xml 文件
        String ossUrl = uploadFileByOSS("contract/template/" + file.getOriginalFilename(),tmpFile);
        contractTemplate.setContractTemplateUrl(ossUrl);

        //5，修改合同
        if(contractTemplate.getId() != null && contractTemplate.getId() > 0){
            contractTemplateDao.update(contractTemplate);
        }else{
            contractTemplateDao.add(contractTemplate);
        }
        return Result.succeed("成功");
    }

    @Override
    public Result getInfo(Integer id) throws ServiceException {
        ContractTemplateVo contractTemplateVo = contractTemplateDao.getInfo(id);
        return Result.succeed(contractTemplateVo,"成功");
    }

    @Override
    public Result delete(Integer id) throws ServiceException {
        contractTemplateDao.delete(id);
        return Result.succeed("成功");
    }


    private String extractVarByRegular(String str){
        ContractVo contractVo = new ContractVo();
        List<VarItems> list = new ArrayList<VarItems>();
        if(StringUtils.isBlank(str)) {
            return null;
        }

        String regex = "\\$\\{([^}]*)\\}";//匹配${}
        Matcher matcher = Pattern.compile(regex).matcher(str);
        while(matcher.find()){
            String group = matcher.group();
            VarItems varItems = new VarItems();
            varItems.setKey(group.substring(2, group.length()-1));
            varItems.setValue(group);
            list.add(varItems);
        }
        contractVo.setVarItems(list);
        JSON json = (JSON) JSON.toJSON(contractVo);
        return json.toString();
    }

    @Data
    public static class ContractVo{
        public String name = "默认组";
        public List<VarItems> varItems;

    }

    @Data
    public static class VarItems{
        public String key;
        public String value;
    }

}
