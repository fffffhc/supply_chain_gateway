package com.scf.erdos.uaa.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.scf.erdos.uaa.model.SysService;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2019/1/12
 * @version：1.0
 */
@Mapper
public interface SysServiceDao {

    @Insert("insert into sys_service(parentId, name, url, path, css, sort, createTime, updateTime,isMenu) "
            + "values (#{parentId}, #{name}, #{url} , #{path} , #{css}, #{sort}, #{createTime}, #{updateTime},#{isMenu})")
    int save(SysService service);

    int update(SysService service);

    @Select("select * from sys_service t where t.id = #{id}")
    SysService findById(Long id);

    @Delete("delete from sys_service where id = #{id}")
    int delete(Long id);

    @Delete("delete from sys_service where parentId = #{id}")
    int deleteByParentId(Long id);

    @Select("select * from sys_service t order by t.sort desc")
    List<SysService> findAll();

    @Select("SELECT * FROM sys_service t WHERE t.isMenu = 1 AND t.parentId = -1 ORDER BY t.sort")
    List<SysService> findOnes();

}
