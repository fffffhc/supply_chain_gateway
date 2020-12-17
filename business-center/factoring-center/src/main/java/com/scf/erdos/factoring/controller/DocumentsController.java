package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.service.IDocumentsService;
import com.scf.erdos.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/9/23 17:11
 */

@Slf4j
@RestController
@RequestMapping("/documents")
@Api(tags = "documents API")
@SuppressWarnings("all")
public class DocumentsController {


    @Autowired
    private IDocumentsService iDocumentsService;

    @ApiOperation(value = "获取文件清单")
    @GetMapping("/getListofDocuments")
    @LogAnnotation(module = "pay",recordRequestParam = false)
    public Result getListofDocuments(@RequestParam Map<String, Object> params){
        return iDocumentsService.getListofDocuments(params);
    }

    @ApiOperation(value = "获取授信决策意见书")
    @GetMapping("/getCreditDecision")
    @LogAnnotation(module = "pay",recordRequestParam = false)
    public Result getCreditDecision(@RequestParam Map<String, Object> params){
        return iDocumentsService.getCreditDecision(params);
    }

    @ApiOperation(value = "获取基础合同")
    @GetMapping("/getBasicContract")
    @LogAnnotation(module = "pay",recordRequestParam = false)
    public Result getBasicContract(@RequestParam Map<String, Object> params){
        return iDocumentsService.getBasicContract(params);
    }

}
