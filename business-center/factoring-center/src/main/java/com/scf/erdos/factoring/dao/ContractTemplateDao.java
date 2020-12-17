package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.model.contract.ContractTemplate;
import com.scf.erdos.factoring.vo.contract.ContractTemplateVo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContractTemplateDao {

    int count(Map<String, Object> params);
    List<ContractTemplateVo> getContractTemplates(Map<String, Object> params);

    String getContractNameByName(ContractTemplate contractTemplate);
    void add(ContractTemplate contractTemplate);

    @Select("select * from product_contract_template where id = #{id}")
    ContractTemplateVo getInfo(@Param("id") Integer id);

    void update(ContractTemplate contractTemplate);

    @Update("update product_contract_template set is_delete = '1' where id = #{id}")
    void delete(@Param("id") Integer id);
}
