package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.model.financing.Financing;
import com.scf.erdos.factoring.vo.company.CompanyInfoVo;
import com.scf.erdos.factoring.vo.credit.CreditVo;
import com.scf.erdos.factoring.vo.financing.*;
import com.scf.erdos.factoring.vo.pay.PaymentNoteVo;
import com.scf.erdos.factoring.vo.workflow.WorkflowInfoVo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface FinancingDao {

    int count(Map<String, Object> params);
    List<FinancingPageVo> getFinancingPages(Map<String, Object> params);

    @Select("select * from financing_info where id = #{id}")
    FinancingInfoVo getFinancingInfo(@Param("id") Integer id);

    CompanyInfoVo getCompanyInfo(@Param("id") Integer id);

    @Select("select * from financing_info where id = #{id}")
    DecisionSubmissionVo getDecisionSubmission(@Param("id") Integer id);

    @Select("select * from product_credit where customer_company_id = #{companyId} and product_id = #{productId} and status = 3")
    CreditVo getCreditVo(@Param("companyId") Integer companyId,@Param("productId") Integer productId);

    List<DSFinancings> getDsFinancingsList(Map<String, Object> params);
    @Select("select * from financing_other_platfrom where financing_id = #{id}")
    List<DSOtherFinancings> getDsOtherFinancingsList(@Param("id") Integer id);
    @Select("select * from financing_pledge_bill where financing_id = #{id}")
    List<DSPledgeBill> getDsPledgeBillList(@Param("id") Integer id);

    @Select("select * from company_workflow where company_id = #{companyId} and status = '1' limit 1")
    WorkflowInfoVo getWorkflowInfoVo(@Param("companyId") String companyId);

    @Select("select * from financing_audit_log where financing_id = #{id} order by id desc")
    List<FinancingAuditLog> getFinancingAuditLog(@Param("id") Integer id);

    @Select("select status from financing_info where id = #{id}")
    int getFinancingStatus(@Param("id") Integer id);

    @Insert("insert into financing_audit_log (financing_id,audit_opinion,user_id,status,create_time) " +
            "values (#{financingId},#{auditOpinion},#{userId},#{status},now())")
    int addLog(Map<String, Object> params);

    int addAuditInfo(Financing financing);

    @Delete("delete from financing_other_platfrom where financing_id = #{id}")
    int deleteOtherPlatfrom(@Param("id") Integer id);
    @Delete("delete from financing_pledge_bill where financing_id = #{id}")
    int deletePledgeBill(@Param("id") Integer id);

    void saveOtherPlatfrom(Financing financing);
    void savePledgeBill(Financing financing);

    @Select("SELECT user_id FROM financing_audit_log WHERE financing_id = #{financingId} ORDER BY id DESC LIMIT 1")
    String getAdminName(@Param("id") Integer financingId);

    Integer getFinancingTimes(@Param("id") Integer financingId);

    void savePaymentNote(PaymentNoteVo paymentNoteVo);
}
