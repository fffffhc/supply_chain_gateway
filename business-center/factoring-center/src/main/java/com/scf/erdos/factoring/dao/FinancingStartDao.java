package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.model.financing.FinancingSave;
import com.scf.erdos.factoring.vo.dict.CompanyVo;
import com.scf.erdos.factoring.vo.financing.AppliFinancingVo;
import com.scf.erdos.factoring.vo.financing.ProductCredits;
import com.scf.erdos.factoring.vo.financing.ProductsVo;
import com.scf.erdos.factoring.vo.financing.Receivable;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface FinancingStartDao {

    List<ProductCredits> getProductCredits(Map<String, Object> params);

    int count(Map<String, Object> params);
    List<ProductsVo> getFinancingProducts(Map<String, Object> params);

    ProductCredits getCreditStatus(Map<String, Object> params);

    AppliFinancingVo getAppliFinancingVo(@Param("productId") Integer productId);

    List<Receivable> getReceivable(@Param("productId") String productId);

    int saveFinancingInfo(FinancingSave financingSave);

    @Update("update receivable_info set status = true where id = #{receivableId}")
    int updateReceivable(FinancingSave financingSave);

    @Select("select code from financing_info where code = #{code}")
    String getCode(@Param("code") String code);

    @Select("select id,company_name from company_info where company_label_1 in ('10000','10002')")
    List<CompanyVo> getBuyerCompanys();
}
