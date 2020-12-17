package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.vo.dict.CompanyVo;
import com.scf.erdos.factoring.vo.dict.DictListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DictDao {

    List<DictListVo> getCompanyDicts(Map<String, Object> params);
    List<DictListVo> getProductDicts(Map<String, Object> params);

    @Select("select t.id,t.company_name from company_info t where t.company_label_1 = #{type} and t.status = '3' and t.ca_status = '4'")
    List<CompanyVo> getCompanyByType(Map<String, Object> params);
}
