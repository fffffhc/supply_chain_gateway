package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.contract.model.businessData.UserContract;
import com.scf.erdos.factoring.contract.model.businessData.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContractVarDao {

    Financing getFinancing(@Param("id") Integer id);

    @Select("select * from financing_pledge_bill where financing_id = #{id}")
    List<DSPledgeBill> getDsPledgeBillList(@Param("id") Integer id);

    @Select("select * from receivable_info where id = #{id}")
    Receivable getReceivable(@Param("id") Integer id);

    Product getProduct(@Param("id") Integer id);

    @Select("select * from receivable_contract where yszk_id = #{id}")
    List<BaseContract> getContractList(@Param("id") Integer id);
    @Select("select * from receivable_fapiao where yszk_id = #{id}")
    List<Fapiao> getFapiaoList(@Param("id") Integer id);
    @Select("select * from receivable_other_bill where yszk_id = #{id}")
    List<OtherBill> getOtherBillList(@Param("id") Integer id);

    @Select("select * from company_info where id = #{id}")
    Company getCompany(@Param("id") Integer id);

    @Select("select * from product_credit where id = #{id}")
    Credit getCreditInfo(@Param("id") Integer id);

    List<ContractTemplate> getContractTemplates(@Param("id") Integer id);

    int saveContract(UserContract userContract);
}
