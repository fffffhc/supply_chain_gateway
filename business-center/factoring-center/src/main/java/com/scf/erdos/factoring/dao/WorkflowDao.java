package com.scf.erdos.factoring.dao;

import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.factoring.model.workflow.CompanyWorkflow;
import com.scf.erdos.factoring.vo.workflow.WorkflowPageVo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface WorkflowDao {

    @Insert("insert company_workflow (name,company_id,flow_type,flow_info,user_id,create_time)" +
            "values(#{name},#{companyId},#{flowType},#{flowInfo},#{userId},now())")
    int add(CompanyWorkflow companyWorkflow);
    int update(CompanyWorkflow companyWorkflow);

    int count(Map<String, Object> params);
    List<WorkflowPageVo> getAllWorkflow(Map<String, Object> params);

    CompanyWorkflow getWorkflowInfo(Map<String, Object> params);

    @Update("update company_workflow set status = #{status} where id = #{id}")
    int handleWork(Map<String, Object> params);

    @Select("select status from company_workflow where id = #{id}")
    String getWorkflowStatus(@Param("id") String id);

    @Delete("delete from company_workflow where id = #{id}")
    int delete(@Param("id") String id);

}
