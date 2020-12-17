package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.vo.company.CompanyInfoVo;
import com.scf.erdos.factoring.vo.company.CompanyPageVo;
import com.scf.erdos.factoring.vo.company.CompanyRegisterInfoVo;
import com.scf.erdos.factoring.vo.credit.ProductCreditLog;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface CompanyDao {

    /**
     * 企业分页列表查询
     */
    int count(Map<String, Object> params);
    List<CompanyPageVo> getAllCompany(Map<String, Object> params);

    /**
     * 获取企业详情
     */
    CompanyInfoVo getCompanyInfo(Map<String, Object> params);

    /**
     * 通过企业名或统一社会编号 获取企业详情
     */
    CompanyInfoVo getCompanyByCreditNoAndName(Map<String, Object> params);
    @Select("select * from company_info where id != #{id} and (company_name = #{companyName} or credit_no = #{creditNo})")
    CompanyInfoVo getCompanyByCreditName(Map<String, Object> params);


    /**
     * 添加企业信息
     */
    int add(Map<String, Object> params);

    /**
     * 编辑企业信息
     */
    int update(Map<String, Object> params);

    /**
     * 审核
     */
    int audit(Map<String, Object> params);

    /**
     * 企业入驻申请
     */
    int settleIn(Map<String, Object> params);

    CompanyRegisterInfoVo findConpanyInfoByUserId(@Param("userId") Long userId);

    @Select("select * from product_credit_log where product_credit_id = #{id}")
    List<ProductCreditLog> getAuditLogs(@Param("id") Integer id);

    @Insert("insert into company_ca_auth(company_id,user_id,platform_customer_id,platform_type,api_version,transaction_no,url,log_id,status,create_time,account_type)" +
            "values(#{companyId},#{userId},#{platformCustomerId},#{platformType},#{apiVersion},#{transactionNo},#{url},#{logId},#{status},now(),#{account_type})")
    int insertCompanyCaAuth(Map<String, Object> params);

    @Update("update company_ca_auth set platform_customer_id = #{platformCustomerId},platform_type = #{platformType},api_version = #{apiVersion},transaction_no = #{transactionNo},url"+
            " = #{url},log_id = #{logId},status = #{status},create_time = now(),account_type = #{account_type} where user_id = #{useId}")
    int updateCompanyCaAuth(Map<String, Object> params);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into company_ca_auth_log(company_id,user_id,request_param_json,response_param_json,get_url_time)"+
            "values(#{companyId},#{userId},#{requestParamJson},#{responseParamJson},now())")
    int insertCompanyCaAuthLog(Map<String, Object> params);

    @Update("update company_ca_auth set status = #{status} where transaction_no = #{transaction_no}")
    int updateCompanyCaStatus(Map<String, Object> params);

    @Update("update company_ca_auth set signature_id = #{signatureId},signatureSubInfo = #{signatureSubInfo},signature_upload_time = now() where user_id = #{userId}")
    int getSignatureId(Map<String, Object> params);

    @Insert("insert into online_contract(credit_code,financing_code,customer_company_id,funding_company_id,signup_company_id) values (creditCode,financingCode,customerCompanyId,fundingCompanyId,signupCompanyId)")
    int insertContract(Map<String, Object> params);


}
