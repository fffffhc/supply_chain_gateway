package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.exception.controller.ControllerException;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.util.ApiSign;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.service.ICompanyService;
import com.scf.erdos.factoring.vo.company.CompanyInfoVo;
import com.scf.erdos.factoring.vo.company.CompanyRegisterInfoVo;
import com.scf.erdos.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 企业管理 Controller
 * @author：bao-clm
 * @date: 2020/5/11
 * @version：1.0
 */

@Slf4j
@RestController
    @RequestMapping("/company")
@Api(tags = "FACOTRING COMPANY API")
public class CompanyController {

    @Autowired
    private ICompanyService iCompanyService;

    @ApiOperation(value = "获取企业列表-分页")
    @GetMapping("/get")
    @LogAnnotation(module = "Company",recordRequestParam = false)
    public Result getAllCompany(@RequestParam Map<String, Object> params){
        return iCompanyService.getAllCompany(params);
    }

    @ApiOperation(value = "获取企业详情")
    @GetMapping("/getCompanyInfo")
    @LogAnnotation(module = "Company",recordRequestParam = false)
    public Result getCompanyInfo(@RequestParam Map<String, Object> params){
        return iCompanyService.getCompanyInfo(params);
    }

    @ApiOperation(value = "添加企业详情")
    @PostMapping("/add")
    @LogAnnotation(module = "Company",recordRequestParam = false)
    public Result add(@RequestParam Map<String, Object> params){
        return iCompanyService.add(params);
    }

    @ApiOperation(value = "编辑企业详情")
    @PutMapping("/update")
    @LogAnnotation(module = "Company",recordRequestParam = true)
    public Result update(@RequestParam Map<String, Object> params){
        return iCompanyService.update(params);
    }

    @ApiOperation(value = "审核")
    @PutMapping("/audit")
    @LogAnnotation(module = "Company",recordRequestParam = true)
    public Result audit(@RequestParam Map<String, Object> params){
        return iCompanyService.audit(params);
    }

    /**
     * 根据用户id
     *
     * @return
     * @throws ControllerException
     */
    @GetMapping(value = "/getCompanyInfo/userId", params = "userId")
    @ApiOperation(value = "根据用户id获取企业信息")
    @LogAnnotation(module = "getCompanyInfo", recordRequestParam = false)
    public CompanyRegisterInfoVo findConpanyInfoByUserId(@RequestParam(value = "userId") Long userId) throws ControllerException {
        try {
            return iCompanyService.findConpanyInfoByUserId(userId);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 获取公司工商数据
     *
     * @return
     * @throws ControllerException
     */
    @GetMapping(value = "/getTianyancha")
    @ApiOperation(value = "根据用户id获取企业信息")
    @LogAnnotation(module = "getTianyancha", recordRequestParam = false)
    public Result getTianyancha(@RequestParam(value = "keyword") String keyword) throws ControllerException {
        try {
            return iCompanyService.getTianyancha(keyword);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 企业 ca 认证
     *
     * @param params
     * @return
     * @throws ControllerException
     */
    @PostMapping(value = "/authCA")
    @ApiOperation(value = "用户CA认证")
    @LogAnnotation(module = "authCA", recordRequestParam = false)
    public Result authCA(@RequestParam Map<String, Object> params) throws ControllerException {
        try {
            return iCompanyService.authCA(params);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 企业 ca 认证回调接口
     *
     * @param params
     * @return
     * @throws ControllerException
     */
    @PostMapping(value = "/fdd/ca/auth/callBack")
    @ApiOperation(value = "企业ca认证回调接口")
    @LogAnnotation(module = "/fdd/ca/auth/callBack", recordRequestParam = false)
    public Result fddCaAuthCallBack(@RequestParam Map<String, Object> params) {
        try {
            return iCompanyService.fddCaAuthCallBack(params);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 签章上传
     */
    @PostMapping(value = "/sealUpload")
    @ApiOperation(value = "签章上传")
    @LogAnnotation(module = "/签章上传", recordRequestParam = false)
    public Result sealUpload(@RequestParam Map<String, Object> params) {
        try {
            return iCompanyService.sealUpload(params);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 合同上传
     */
    @PostMapping(value = "/contractUpload")
    @ApiOperation(value = "合同上传")
    @LogAnnotation(module = "/合同上传", recordRequestParam = false)
    public Result contractUpload(@RequestParam Map<String, Object> params) {
        try {
            return iCompanyService.contractUpload(params);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 合同签署
     */
    @PostMapping(value = "/extsign")
    @ApiOperation(value = "合同签署")
    @LogAnnotation(module = "/合同签署", recordRequestParam = false)
    public Result extsign(@RequestParam Map<String,Object> params){
        try {
            return iCompanyService.extsign(params);
        }catch (ServiceException e) {
            throw new ControllerException(e);
        }

    }




}
