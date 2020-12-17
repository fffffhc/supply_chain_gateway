package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.vo.pay.PendingRepaymentInformationVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/9/28 11:12
 */
@Mapper
public interface RepayDao {
    /**
     * 待还款融资列表查询
     */
    int countforRepay(Map<String, Object> params);
    List<PendingRepaymentInformationVo> getPendingRepaymentInformation(Map<String, Object> params);

}
