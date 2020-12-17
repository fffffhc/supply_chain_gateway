package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.service.IDictService;
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
 * @Description : 字典controller
 * @author：bao-clm
 * @date: 2020/5/11
 * @version：1.0
 */

@Slf4j
@RestController
@RequestMapping("/dict")
@Api(tags = "DICT API")
public class DictController {

    @Autowired
    private IDictService iDictService;

    @ApiOperation(value = "企业--【获取字典信息】")
    @GetMapping("/company/get")
    public Result getCompanyDicts(@RequestParam Map<String, Object> params){
        return iDictService.getCompanyDicts(params);
    }

    @ApiOperation(value = "产品--【获取字典信息】")
    @GetMapping("/product/get")
    public Result getProductDicts(@RequestParam Map<String, Object> params){
        return iDictService.getProductDicts(params);
    }

    @ApiOperation(value = "企业分类--【获取字典信息】")
    @GetMapping("/company/getCompanyByType")
    public Result getCompanyByType(@RequestParam Map<String, Object> params){
        return iDictService.getCompanyByType(params);
    }

}
