package com.scf.erdos.factoring.service.impl;

import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.constant.UserType;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.util.StringUtil;
import com.scf.erdos.common.util.SysUserUtil;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.ProductDao;
import com.scf.erdos.factoring.model.Product.ProductContract;
import com.scf.erdos.factoring.model.Product.ProductInfo;
import com.scf.erdos.factoring.service.IProductService;
import com.scf.erdos.factoring.vo.product.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Description : 融资产品 service 实现类
 * @author：bao-clm
 * @date: 2020/5/22
 * @version：1.0
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Result add(ProductInfo productInfo) throws ServiceException {
        try{
            /**
             * 获取当前用户登陆信息
             */
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            productInfo.setUserId(loginUser.getId());

            String productName = productDao.getProductNameByName(productInfo.getName());
            if(StringUtil.isNotBlank(productName)){
                return Result.failed(-1001,"产品名不能重复");
            }

            //4，产品编号 RX + 年月日 + 7随机数
           LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String date = now.format(formatter);
            String code = "";
            String reCode = "";
            do{
                code = "RX" + date + ((int)(new Random().nextDouble()*(9999999-1000000 + 1))+ 1000000);
                reCode = productDao.getCode(code);
            }while(!StringUtils.isBlank(reCode));

            productInfo.setCode(code);
            int i = productDao.add(productInfo);
            return i > 0 ? Result.succeed("保存成功") : Result.failed("保存失败");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getProductInfo(Integer id) throws ServiceException {
        try{
            ProductInfoVo productInfoVo = productDao.getProductInfo(id);
            return Result.succeed(productInfoVo,"获取成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getAllProducts(Map<String, Object> params) throws ServiceException {
        try {
            Integer page = MapUtils.getInteger(params, "page");
            Integer limit = MapUtils.getInteger(params, "limit");

            if (page == null || limit == null) {
                return Result.failed("page，limit不能为空！");
            }

            if(page < 1){
                return Result.failed("page不能小于1，首页从1开始！");
            }

            /**
             * 获取当前用户登陆信息
             */
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");

            /**
             * 平台管理员获取所有的产品
             * 公司管理员用户之获取自个儿维护的产品
             */
            String type = loginUser.getType();
            if(type.equals(UserType.COMPANY) || type.equals(UserType.COMPANYADMIN)){
                params.put("userId",loginUser.getId());
            }
            params.put("currentPage",(page - 1)*limit);
            params.put("pageSize",limit);

            int total = productDao.count(params);
            List<ProductPageVo> list = productDao.getAllProducts(params);

            PageResult pageResult = PageResult.<ProductPageVo>builder().page(page).limit(limit).data(list).count((long)total).build();
            return Result.succeed(pageResult,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result addProductContract(ProductContract productContract) throws ServiceException {
        try{
            /**
             * 获取当前用户登陆信息
             */
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            productContract.setUserId(loginUser.getId());

            String code = productDao.getProductContractCode(productContract);
            if(StringUtil.isNotBlank(code)){
                return Result.failed(-1001,"电子合同编号已存在，不能重复");
            }

            int i = productDao.addProductContract(productContract);
            return i > 0 ? Result.succeed("保存成功") : Result.failed("保存失败");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result handleProduct(ProductInfo productInfo) throws ServiceException {
        try{
            Boolean status = productInfo.getStatus();
            //1,激活
            if(status){
                String contractId = productDao.getContract(productInfo.getId());
                if(StringUtil.isBlank(contractId)){
                    return Result.failedWith(null,-1001,"本产品电子合同为空，不能激活");
                }
            }

            //2，停用
            if(!status){
                int count = productDao.getBeUsed(productInfo.getId());
                if(count > 0){
                    return Result.failedWith(null,-1002,"产品在使用中，不能停用");
                }
            }
            productDao.handleProduct(productInfo);
            return Result.succeed("操作成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result delete(Integer id) throws ServiceException {
        try{
            ProductInfoVo productInfoVo = productDao.getProductInfo(id);
            Boolean status = productInfoVo.getStatus();
            //已激活
            if(status){
                return Result.failedWith(null,-1001,"本产品已激活，不能删除");
            }
            productDao.delete(id);
            return Result.succeed("删除成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result update(ProductInfo productInfo) throws ServiceException {
        try{

            String productName = productDao.getProductNameByNameId(productInfo.getName(),productInfo.getId());
            if(StringUtil.isNotBlank(productName)){
                return Result.failed(-1002,"产品名不能重复");
            }

            ProductInfoVo productInfoVo = productDao.getProductInfo(productInfo.getId());
            Boolean status = productInfoVo.getStatus();
            //已激活
            if(status){
                return Result.failedWith(null,-1001,"已激活的产品不能修改");
            }
            /**
             * 获取当前用户登陆信息
             */
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            productInfo.setUpdateUserId(loginUser.getId());

            productDao.update(productInfo);
            return Result.succeed("修改成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getContracts(Integer id) throws ServiceException{
        try{
            List<ProductContractsVo> list = productDao.getContracts(id);
            return Result.succeed(list,"获取产品合同成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result updateProductContract(ProductContract productContract) {
        try{
            productDao.updateProductContract(productContract);
            return Result.succeed("修改成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result deleteProductContract(Integer id) throws ServiceException {
        try{
            productDao.deleteProductContract(id);
            return Result.succeed("删除成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getPlaceholder(Map<String, Object> params) {
        try{
            List<ProductContractPlaceholder> list = productDao.getPlaceholder(params);
            return Result.succeed(list,"获取成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getContractInfo(Integer id) {
        try{
            ProductContractInfoVo pcv = productDao.getContractInfo(id);
            return Result.succeed(pcv,"获取成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ExportExcelProducts> exportExcel(String type) {
        try{
            List<ExportExcelProducts> result = productDao.exportExcel(type);
            return result;
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
