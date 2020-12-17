package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.contract.ContractTemplate;
import com.scf.erdos.factoring.service.IContractTemplateService;
import com.scf.erdos.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.Map;
import java.util.UUID;

/**
 * @Description : 合同模板维护
 * @author：bao-clm
 * @date: 2020/9/1
 * @version：1.0
 */

@Slf4j
@RestController
@RequestMapping("/contractTemplate")
@Api(tags = "FACOTRING COMPANY API")
public class ContractTemplateController {


    @Autowired
    private IContractTemplateService iContractTemplateService;

    @ApiOperation(value = "获取合同模板-分页")
    @GetMapping("/get")
    @LogAnnotation(module = "contractTemplate",recordRequestParam = false)
    public Result getContractTemplates(@RequestParam Map<String, Object> params){
        return iContractTemplateService.getContractTemplates(params);
    }

    @ApiOperation(value = "新增/编辑合同模板")
    @PostMapping("/saveOrUpdate")
    @LogAnnotation(module = "contractTemplate",recordRequestParam = false)
    public Result saveOrUpdate(ContractTemplate contractTemplate, MultipartFile file) throws IOException, DocumentException {
        return iContractTemplateService.saveOrUpdate(contractTemplate,file);
    }

    @ApiOperation(value = "获取合同详情")
    @GetMapping("/getInfo/id")
    @LogAnnotation(module = "contractTemplate",recordRequestParam = false)
    public Result getInfo(@RequestParam(value = "id") Integer id){
        return iContractTemplateService.getInfo(id);
    }

    @ApiOperation(value = "删除合同模板")
    @GetMapping("/delete/id")
    @LogAnnotation(module = "contractTemplate",recordRequestParam = false)
    public Result delete(@RequestParam(value = "id") Integer id){
        return iContractTemplateService.delete(id);
    }

}
