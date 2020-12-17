package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.model.credit.ProductCredit;
import com.scf.erdos.factoring.vo.credit.CreditPageVo;
import com.scf.erdos.factoring.vo.credit.CreditVo;
import com.scf.erdos.factoring.vo.product.ProductContractsVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CredictDao {

    @Select("select is_credit from product_info where id = #{productId}")
    boolean getIsCreditByProductId(@Param("productId") String productId);

    @Select("select code from product_credit where product_id = #{productId} and customer_company_id = #{companyId} and status not in ('3','4')")
    String getCreditCodeById(Map<String, Object> params);

    @Select("select code from product_credit where code = #{code}")
    String getCode(@Param("code") String code);

    @Insert("insert into product_credit (code,product_id,customer_company_id,apply_time)values(#{code},#{productId},#{companyId},now())")
    int apply(Map<String, Object> params);

    int count(Map<String, Object> params);
    List<CreditPageVo> getCredicts(Map<String, Object> params);

    CreditVo getCreditVoById(@Param("id") Integer id);

    int update(ProductCredit productCredit);

    @Insert("insert into product_credit_log(product_credit_id,credit_opinion,status,user_id,create_time)values(#{id},#{creditOpinion},#{status},#{userId},now())")
    int saveAuditLog(ProductCredit productCredit);

    @Select("select * from product_contract where product_id = #{id} and is_delete = '0' and signing_stage = 1 order by id desc")
    List<ProductContractsVo> getContracts(@Param("productId") Integer productId);
}
