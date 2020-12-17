package com.scf.erdos.factoring.dao;


import com.scf.erdos.factoring.model.repay.PayPlan;
import com.scf.erdos.factoring.vo.financing.FinancingInfoVo;
import com.scf.erdos.factoring.vo.pay.*;
import com.scf.erdos.factoring.vo.product.ProductInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface PayDao {
    /**
     * 待放款融资列表查询
     */
    int countforPay(Map<String, Object> params);
    List<PendingLondInformationVo> getPendingLondInformation(Map<String, Object> params);

    /**
     * 获取付款单
     */
    PaymentNoteVo getPaymentNote(Map<String, Object> params);

    /**
     * 修改financing_code status 状态改为待还款
     */
    @Update("update financing_info set status = '5' where code = #{financingCode}")
    int updateFinancingStatus(Map<String, Object> params);

    /**
     * 保存还款计划
     */
    void saveRepaymentPlan(List<PayPlan> list);

    /**
     * 获取融资详情
     */
    @Select("select * from financing_info where code = #{financingCode}")
    FinancingInfoVo getFinancingInfo(Map<String, Object> params);

    /**
     * 获取产品信息
     */
    @Select("select * from product_info where id = (select product_id from financing_info where code = #{financingCode})")
    ProductInfoVo getProductInfoVo(Map<String, Object> params);
}
