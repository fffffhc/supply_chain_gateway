package com.scf.erdos.oss.dao;

import java.util.List;
import java.util.Map;

import com.scf.erdos.oss.model.FileInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description : oss上传存储db
 * @author：bao-clm
 * @date: 2020/3/23
 * @version：1.0
 */
@Mapper
public interface FileDao {

    @Select("select * from file_info t where t.id = #{id}")
    FileInfo getById(String id);

    @Insert("insert into file_info(id, name, isImg, contentType, size, path, url, source, createTime) "
            + "values(#{id}, #{name}, #{isImg}, #{contentType}, #{size}, #{path}, #{url}, #{source}, #{createTime})")
    int save(FileInfo fileInfo);

    @Delete("delete from file_info where id = #{id}")
    int delete(String id);

    int count(Map<String, Object> params);

    List<FileInfo> findList(Map<String, Object> params);
}
