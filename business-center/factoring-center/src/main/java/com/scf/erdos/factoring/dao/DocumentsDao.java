package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.vo.pay.CreditDecisionVo;
import com.scf.erdos.factoring.vo.pay.ListofDocumentsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/9/28 11:13
 */

@Mapper
public interface DocumentsDao {
    /**
     * 获取文件清单
     */
    int countforListofDocuments(Map<String, Object> params);
    List<ListofDocumentsVo> getListofDocuments(Map<String, Object> params);

    /**
     * 获取授信决策意见书
     */
    CreditDecisionVo getCreditDecision(Map<String, Object> params);

    /**
     * 获取基础合同
     */
    String getBasicContract(Map<String, Object> params);
}
