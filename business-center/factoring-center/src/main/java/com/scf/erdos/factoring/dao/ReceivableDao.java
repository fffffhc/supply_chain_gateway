package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.model.receivable.ReceivableFapiao;
import com.scf.erdos.factoring.model.receivable.ReceivableInfo;
import com.scf.erdos.factoring.model.receivable.ReceivableOtherBill;
import com.scf.erdos.factoring.vo.yszk.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReceivableDao {

    /**
     * 应收账款分页列表查询
     */
    int count(Map<String, Object> params);
    List<ReceivablePageVo> getAllYszks(Map<String, Object> params);

    /**
     * 应收账款详情
     */
    @Select("select t.id,t.code,t.tag,t.buyer_company buyerCompany,t.status,t.yszk_amount,t.fapiao_amount,t.bill_amount from receivable_info t where t.id = #{id}")
    ReceivableInfoVo getYszkInfo(@Param("id") Integer id);

    /**
     * 应收账款合同列表
     */
    @Select("select t.id,t.yszk_id,t.contract_name contractName,t.contract_code contractCode,t.contract_date contractDate " +
            ",t.goods,t.unit_price unitPrice,t.buyer_company,t.contract_file from receivable_contract t where t.yszk_id = #{id}")
    List<ReceivableContractVo> getContractsByYszkId(@Param("id") Integer id);

    /**
     * 应收账款发票列表
     */
    @Select("select t.id,t.yszk_id,t.fp_no,t.fp_code,t.fp_date,t.fp_amount,t.buyer_company from receivable_fapiao t where t.yszk_id = #{id}")
    List<ReceivableFapiaoVo> getFapiaoByYszkId(@Param("id") Integer id);

    /**
     * 应收账款其它票据
     */
    @Select("select t.id,t.yszk_id,t.bill_code,t.bill_type,t.bill_date,t.unit_price,t.buyer_company,t.net_weight,t.amount " +
            "from receivable_other_bill t where t.yszk_id = #{id}")
    List<ReceivableOtherBillVo> getOtherBillByYszkId(@Param("id") Integer id);

    /**
     * 保存应收账款
     */
    int add(ReceivableInfo yszkInfo);
    /**
     * 保存应收账款-合同列表
     */
    int addContracts(ReceivableInfo yszkInfo);
    /**
     * 保存应收账款-发票列表
     */
    List<ReceivableFapiaoVo> getFapiaoByFpNo(List<ReceivableFapiao> list);
    ReceivableFapiaoVo getFapiao(ReceivableFapiao receivableFapiao);
    int addFapiaos(ReceivableInfo yszkInfo);
    /**
     * 保存应收账款-其它票据
     */
    List<ReceivableOtherBillVo> getOtherBillByBillCode(List<ReceivableOtherBill> list);
    ReceivableOtherBillVo getOtherBill(ReceivableOtherBill receivableOtherBill);
    int addOtherBills(ReceivableInfo yszkInfo);

    /**
     * 修改应收账款
     */
    int updateYszkInfo(ReceivableInfo yszkInfo);
    @Delete("delete from receivable_contract where yszk_id = #{yszkId}")
    int deleteContractsByYszkId(@Param("yszkId") int yszkId);
    @Delete("delete from receivable_fapiao where yszk_id = #{yszkId}")
    int deleteFapiaoByYszkId(@Param("yszkId") int yszkId);
    @Delete("delete from receivable_other_bill where yszk_id = #{yszkId}")
    int deleteOtherBillsByYszkId(@Param("yszkId") int yszkId);

    @Select("select code from receivable_info where code = #{code}")
    String getCode(@Param("code") String code);

    @Select("select file_url from excel_template")
    String getExcelTemplate(Map<String, Object> params);

    /**
     * 修改应收账款-合同列表
     *//*
    int updateContracts(Map<String, Object> params);
    *//**
     * 修改应收账款-发票列表
     *//*
    int updateFapiaos(Map<String, Object> params);
    *//**
     * 修改应收账款-其它票据
     *//*
    int updateOtherBills(Map<String, Object> params);*/

}
