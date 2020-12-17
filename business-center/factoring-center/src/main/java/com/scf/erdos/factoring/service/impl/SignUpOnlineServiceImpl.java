package com.scf.erdos.factoring.service.impl;

import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.util.SysUserUtil;
import com.scf.erdos.common.web.PageParamHandle;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.SignUpOnlineDao;
import com.scf.erdos.factoring.service.ISignUpOnlineService;
import com.scf.erdos.factoring.vo.signupOnline.SignupStateVo;
import com.scf.erdos.factoring.vo.signupOnline.SignupOnlineVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description : 在线签约
 * @author：bao-clm
 * @date: 2020/8/6
 * @version：1.0
 */

@Slf4j
@Service
@SuppressWarnings("all")
public class SignUpOnlineServiceImpl implements ISignUpOnlineService {

    @Autowired
    private SignUpOnlineDao signUpOnlineDao;
    @Autowired
    private PageParamHandle pageParamHandle;

    @Override
    public Result getOnlineContracts(Map<String, Object> map) throws ServiceException {
        try {
            Result result = pageParamHandle.handle(map);
            if(200 == result.getResp_code()){
                Map<String, Object> param = (Map<String, Object>)result.getData();
                //1，获取当前用户信息
                LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
                if(loginUser == null)
                    return Result.failed("获取当前用户登陆信息失败");
                param.put("companyId",loginUser.getCompanyId());//用户企业id

                int total = signUpOnlineDao.count(param);
                List<SignupOnlineVo> list = signUpOnlineDao.getOnlineContracts(param);

                int page = Integer.parseInt(param.get("page").toString());
                int limit = Integer.parseInt(param.get("limit").toString());
                PageResult pageResult = PageResult.<SignupOnlineVo>builder().page(page).limit(limit).data(list).count((long)total).build();
                return Result.succeed(pageResult,"成功");
            }else{
                return result;
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getSignupInfo(Integer id) throws ServiceException {
        try {
            List<SignupStateVo> signupInfos = signUpOnlineDao.getSignupInfo(id);
            return Result.succeed(signupInfos,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
