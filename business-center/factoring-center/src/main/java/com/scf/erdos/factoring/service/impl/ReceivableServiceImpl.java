package com.scf.erdos.factoring.service.impl;

import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysUser;
import com.scf.erdos.common.util.SysUserUtil;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.ReceivableDao;
import com.scf.erdos.factoring.feign.UserFeignClient;
import com.scf.erdos.factoring.model.receivable.ReceivableContract;
import com.scf.erdos.factoring.model.receivable.ReceivableFapiao;
import com.scf.erdos.factoring.model.receivable.ReceivableInfo;
import com.scf.erdos.factoring.model.receivable.ReceivableOtherBill;
import com.scf.erdos.factoring.service.IReceivableService;
import com.scf.erdos.factoring.vo.yszk.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description : 应收账款上传 接口实现类
 * @author：bao-clm
 * @date: 2020/5/12
 * @version：1.0
 */

@Slf4j
@Service
@SuppressWarnings("all")
public class ReceivableServiceImpl implements IReceivableService {

    @Autowired
    private ReceivableDao receivableDao;

    @Autowired
    private UserFeignClient userFeignClient;

    @Transactional
    @Override
    public Result getYszks(Map<String, Object> params) throws ServiceException {
        try {
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            params.put("companyId",loginUser.getCompanyId());

            Integer page = MapUtils.getInteger(params, "page");
            Integer limit = MapUtils.getInteger(params, "limit");

            if (page == null || limit == null) {
                return Result.failed("page，limit不能为空！");
            }

            if(page < 1){
                return Result.failed("page不能小于1，首页从1开始！");
            }

            params.put("currentPage",(page - 1)*limit);
            params.put("pageSize",limit);

            int total = receivableDao.count(params);
            List<ReceivablePageVo> list = receivableDao.getAllYszks(params);

            //通过用户id 获取资产录入id账号
            List<String> userIds = list.stream().map(ReceivablePageVo::getUserId).collect(Collectors.toList());
            List<ReceivablePageVo> newList = new ArrayList<ReceivablePageVo>();
            if(userIds.size() > 0){
                //获取用户信息
                List<SysUser> userList = userFeignClient.findByUserIds(userIds);
                //用户信息组装
                newList = list.stream()
                        .map(receivablePageVo -> userList.stream()
                                .filter(loginAppUser -> receivablePageVo.getUserId().equals(String.valueOf(loginAppUser.getId())))
                                .findFirst()
                                .map(loginAppUser -> {
                                    receivablePageVo.setUsername(loginAppUser.getUsername());
                                    return receivablePageVo;
                                }).orElse(null))
                        .collect(Collectors.toList());
            }

            PageResult pageResult = PageResult.<ReceivablePageVo>builder().page(page).limit(limit).data(newList).count((long)total).build();
            return Result.succeed(pageResult,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public Result getYszkInfo(Integer id) throws ServiceException {
        try {
           ReceivableInfoVo yszkInfoVo = receivableDao.getYszkInfo(id);
           if(yszkInfoVo != null){
               yszkInfoVo.setContractList(receivableDao.getContractsByYszkId(id));
               yszkInfoVo.setFapiaoList(receivableDao.getFapiaoByYszkId(id));
               yszkInfoVo.setOtherBillList(receivableDao.getOtherBillByYszkId(id));
               return Result.succeed(yszkInfoVo,"获取成功");
           }else{
               return Result.failed("获取失败");
           }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public Result add(ReceivableInfo yszkInfo) throws ServiceException {
        try {
            //1，首先判断 发票和其它票据是否已使用
            Result result1 = existFapiaoAndBills(yszkInfo);
            if(result1 != null){
                return result1;
            }
            //2，判断买方企业是否相同
            Result result2 = buyerCompapny(yszkInfo);
            if(result2 != null){
                return result2;
            }

            /**
             * 2020-08-21
             * 从基础合同获取买方企业名称
             * 存在应收账款主表
             */
            String buyerCompany = getBuyerCompapny(yszkInfo);
            yszkInfo.setBuyerCompany(buyerCompany);

            //3，获取当前用户登陆信息
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            yszkInfo.setUserId(loginUser.getId());
            yszkInfo.setCompanyId(loginUser.getCompanyId());

            //4，生成资产编号 RX + 年月日 + 7随机数
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String date = now.format(formatter);
            String code = "";
            String reCode = "";
            do{
                code = "RX" + date + ((int)(new Random().nextDouble()*(9999999-1000000 + 1))+ 1000000);
                reCode = receivableDao.getCode(code);
            }while(!StringUtils.isBlank(reCode));
            yszkInfo.setCode(code);

            //5，获取本次应收账款总金额，发票总金额，其它单据总金额
            yszkInfo = getsSumAmount(yszkInfo);

            //6，保存应收账款主表信息并返回id
            receivableDao.add(yszkInfo);

            //7,保存合同信息
            receivableDao.addContracts(yszkInfo);

            //8,保存发票信息
            List<ReceivableFapiao> fapiaoList = yszkInfo.getFapiaoList();
            if(fapiaoList.size() > 0){
                receivableDao.addFapiaos(yszkInfo);
            }

            //9,保存其它单据信息
            List<ReceivableOtherBill> billList = yszkInfo.getOtherBillList();
            if(billList.size() > 0){
                receivableDao.addOtherBills(yszkInfo);
            }
            return Result.succeed("保存成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public Result update(ReceivableInfo yszkInfo) throws ServiceException {
        try{
            //1,应收账款id 不能为空
            int yszkId =  yszkInfo.getId();
            if(yszkId < 0 || yszkId == 0){
                return Result.failed("应收账款id不能为空");
            }

            /**
             * 2020-08-21
             *
             * 1，
             * 从基础合同获取买方企业名称
             * 存在应收账款主表
             *
             * 2，已使用的应收账款不能修改
             */
            ReceivableInfoVo yszkInfoVo = receivableDao.getYszkInfo(yszkInfo.getId());
            if(yszkInfoVo.getStatus()){
                return Result.failed(-1008,"已使用应收账款不能修改");
            }

            String buyerCompany = getBuyerCompapny(yszkInfo);
            yszkInfo.setBuyerCompany(buyerCompany);

            //2，首先判断 发票和其它票据是否已使用
            Result result = existFapiaoAndBills(yszkInfo);
            if(result != null){
                return result;
            }
            Result result2 = buyerCompapny(yszkInfo);
            if(result2 != null){
                return result2;
            }

            //3，获取当前用户登陆信息
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            yszkInfo.setUpdateUserId(loginUser.getId());

            //4，获取本次应收账款总金额，发票总金额，其它单据总金额
            yszkInfo = getsSumAmount(yszkInfo);

            //5，更新主表信息
            receivableDao.updateYszkInfo(yszkInfo);

            //6，先删除，然后添加
            receivableDao.deleteContractsByYszkId(yszkId);
            receivableDao.deleteFapiaoByYszkId(yszkId);
            receivableDao.deleteOtherBillsByYszkId(yszkId);

            List<ReceivableContract> contractList = yszkInfo.getContractList();
            if(contractList.size() > 0){
                receivableDao.addContracts(yszkInfo);
            }
            List<ReceivableFapiao> fapiaoList = yszkInfo.getFapiaoList();
            if(fapiaoList.size() > 0){
                receivableDao.addFapiaos(yszkInfo);
            }
            List<ReceivableOtherBill> otherBillList = yszkInfo.getOtherBillList();
            if(otherBillList.size() > 0){
                receivableDao.addOtherBills(yszkInfo);
            }
            return Result.succeed("修改成功");
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getExcelTemplate(Map<String, Object> params) throws ServiceException {
        String url = receivableDao.getExcelTemplate(params);
        return Result.succeed(url,"获取成功");
    }

    /**
     * 判断保存的应收账款发票和其它票据是否使用过
     */
    public Result existFapiaoAndBills(ReceivableInfo yszkInfo){
        List<ReceivableFapiao> fapiaoList = yszkInfo.getFapiaoList();
        if(fapiaoList.size() > 0){
            //List<ReceivableFapiaoVo> flist = receivableDao.getFapiaoByFpNo(fapiaoList);
            /**
             * 2020-08-25
             * 校验发票是否重复使用
             */
            for(ReceivableFapiao receivableFapiao : fapiaoList){
                ReceivableFapiaoVo receivableFapiaoVo = receivableDao.getFapiao(receivableFapiao);
                if(receivableFapiaoVo != null){
                    return Result.failed(-1001,receivableFapiaoVo.getFpNo() +"已使用，请添加别的发票");
                }
            }
        }

        List<ReceivableOtherBill> otherBillList = yszkInfo.getOtherBillList();
        if(otherBillList.size() > 0){
            //List<ReceivableOtherBillVo> blist = receivableDao.getOtherBillByBillCode(otherBillList);
            for(ReceivableOtherBill receivableOtherBill : otherBillList){
                ReceivableOtherBillVo receivableOtherBillVo = receivableDao.getOtherBill(receivableOtherBill);
                if(receivableOtherBillVo != null){
                    return Result.failed(-1002,receivableOtherBillVo.getBillCode() +"已使用，请添加别的票据");
                }
            }
        }
        return null;
    }
    /**
     * 通过基础合同 获取买方企业
     */
    public String getBuyerCompapny(ReceivableInfo yszkInfo){
        List<ReceivableContract> contractList = yszkInfo.getContractList();
        ReceivableContract r = contractList.get(0);
        return r.getBuyerCompany();
    }

    /**
     * 基础合同中的“买方企业名称”
     * 发票信息列表“买方企业名称”
     * 其它票据买方“买方企业名称”
     *  -- 基础合同买方企业和发票信息列表买方企业/ 其它票据买方企业 必须一致
     */
    public Result buyerCompapny(ReceivableInfo yszkInfo){
        String contractBuyerCompany = "";
        String fapiaoBuyerCompany = "";
        String billBuyerCompany = "";

        /**
         * 判断发基础合同中的“买方企业名称”是否相同
         */
        List<ReceivableContract> contractList = yszkInfo.getContractList();
        if(contractList.size() > 0){
            List<String> buyerList = new ArrayList<String>();
            for(int i=0;i < contractList.size();i++){
                ReceivableContract receivableContract = contractList.get(i);
                String name = receivableContract.getBuyerCompany();
                buyerList.add(name);
            }
            //去重
            List<String> distinctNumbers = buyerList.stream()
                    .distinct()
                    .collect(Collectors.toList());
            if(distinctNumbers.size() > 1){
                return Result.failedWith(null,-1003,"基础合同列表买方企业不同");
            }else{
                contractBuyerCompany = distinctNumbers.get(0);
            }
        }

        /**
         * 判断发票信息列表“买方企业名称”是否相同
         */
        List<ReceivableFapiao> fapiaoList = yszkInfo.getFapiaoList();
        if(fapiaoList.size() > 0){
            List<String> buyerList = new ArrayList<String>();
            for(int i=0;i < fapiaoList.size();i++){
                ReceivableFapiao receivableFapiao = fapiaoList.get(i);
                String name = receivableFapiao.getBuyerCompany();
                buyerList.add(name);
            }
            //去重
            List<String> distinctNumbers = buyerList.stream()
                    .distinct()
                    .collect(Collectors.toList());

            if(distinctNumbers.size() > 1){
                return Result.failedWith(null,-1004,"发票列表买方企业不同");
            }else{
                fapiaoBuyerCompany = distinctNumbers.get(0);
            }
        }

        /**
         * 判断其它票据买方“买方企业名称”是否相同
         */
        List<ReceivableOtherBill> otherBillList = yszkInfo.getOtherBillList();
        if(otherBillList.size() > 0){
            List<String> buyerList = new ArrayList<String>();
            for(int i=0;i < otherBillList.size();i++){
                ReceivableOtherBill receivableOtherBill = otherBillList.get(i);
                String name = receivableOtherBill.getBuyerCompany();
                buyerList.add(name);
            }

            //其它票据买方企业不是必填项
            if(buyerList.size() > 0){
                //去重
                List<String> distinctNumbers = buyerList.stream()
                        .distinct()
                        .collect(Collectors.toList());

                if(distinctNumbers.size() > 1){
                    return Result.failedWith(null,-1005,"其它票据买方企业不同");
                }else{
                    billBuyerCompany = distinctNumbers.get(0);
                }
            }
        }

        /**
         * 发票信息和其它票据信息，有可能同时存在，有可能存在两者其一
         */

        if(!"".equals(fapiaoBuyerCompany) && !contractBuyerCompany.equals(fapiaoBuyerCompany)){
            return Result.failedWith(null,-1006,"基础合同买方企业和发票列表买方企业不同");
        }

        if(!"".equals(billBuyerCompany) && !contractBuyerCompany.equals(billBuyerCompany)){
            return Result.failedWith(null,-1007,"基础合同买方企业和其它票据列表买方企业不同");
        }

        return null;
    }

    /**
     * 计算发票和其它票据总额
     */
    public ReceivableInfo getsSumAmount(ReceivableInfo yszkInfo){
        DecimalFormat df = new DecimalFormat("#.00");
        double yszkAmount = 0.00;
        double fapiaoAmount = 0.00;
        double billAmount = 0.00;

        List<ReceivableFapiao> fapiaoList = yszkInfo.getFapiaoList();
        if(fapiaoList.size() > 0){
            BigDecimal totalAmount = fapiaoList.stream().filter(o -> o.getFpAmount() != null)
                    .map(o -> o.getFpAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        List<ReceivableOtherBill> otherBillList = yszkInfo.getOtherBillList();
        if(otherBillList.size() > 0){
            BigDecimal totalAmount = otherBillList.stream().filter(o -> o.getAmount() != null)
                    .map(o -> o.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

           /* DoubleSummaryStatistics doubleSummaryStatistics = otherBillList.stream().collect(Collectors.summarizingDouble(ReceivableOtherBill::getAmount));
            billAmount = doubleSummaryStatistics.getSum();*/
        }

        yszkAmount = fapiaoAmount + billAmount;

        yszkInfo.setYszkAmount(Double.parseDouble(df.format(yszkAmount)));
        yszkInfo.setFapiaoAmount(Double.parseDouble(df.format(fapiaoAmount)));
        yszkInfo.setBillAmount(Double.parseDouble(df.format(billAmount)));

        return yszkInfo;
    }

}
