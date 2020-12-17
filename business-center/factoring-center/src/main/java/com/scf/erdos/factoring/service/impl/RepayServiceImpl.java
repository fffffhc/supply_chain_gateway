package com.scf.erdos.factoring.service.impl;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.PageParamHandle;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.RepayDao;
import com.scf.erdos.factoring.service.IRepayService;
import com.scf.erdos.factoring.vo.pay.PendingRepaymentInformationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/9/28 11:09
 */

@Service
@SuppressWarnings("all")
public class RepayServiceImpl implements IRepayService {
    @Autowired
    private RepayDao repayDao;

    @Autowired
    private PageParamHandle pageParamHandle;


    @Transactional
    @Override
    public Result getPendingRepaymentInformation(Map<String, Object> map) throws ServiceException {
        Result result = pageParamHandle.handle(map);
        if (200 == result.getResp_code()) {
            Map<String, Object> param = (Map<String, Object>) result.getData();
            int total = repayDao.countforRepay(param);
            List<PendingRepaymentInformationVo> list = repayDao.getPendingRepaymentInformation(param);
            int page = Integer.parseInt(param.get("page").toString());
            int limit = Integer.parseInt(param.get("limit").toString());
            PageResult pageResult = PageResult.<PendingRepaymentInformationVo>builder().page(page).limit(limit).data(list).count((long) total).build();
            return Result.succeed(pageResult, "成功");
        } else {
            return result;
        }
    }
}
