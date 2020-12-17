package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.vo.signupOnline.SignupStateVo;
import com.scf.erdos.factoring.vo.signupOnline.SignupOnlineVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SignUpOnlineDao {
    int count(Map<String, Object> param);
    List<SignupOnlineVo> getOnlineContracts(Map<String, Object> param);


    List<SignupStateVo> getSignupInfo(@Param("id") Integer id);
}
