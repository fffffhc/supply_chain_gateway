package com.scf.erdos.factoring.service.impl;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.PageParamHandle;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.PayDao;
import com.scf.erdos.factoring.model.repay.PayPlan;
import com.scf.erdos.factoring.service.IPayService;
import com.scf.erdos.factoring.service.impl.repaymentPlan.generateRepaymentPlan;
import com.scf.erdos.factoring.vo.financing.FinancingInfoVo;
import com.scf.erdos.factoring.vo.pay.*;
import com.scf.erdos.factoring.vo.product.ProductInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@SuppressWarnings("all")
public class PayServiceImpl implements IPayService {

    @Autowired
    private PayDao payDao;

    @Autowired
    private PageParamHandle pageParamHandle;

    @Transactional
    @Override
    public Result getPendingLondInformation(Map<String, Object> map) throws ServiceException {
        Result result = pageParamHandle.handle(map);
        if (200 == result.getResp_code()) {
            Map<String, Object> param = (Map<String, Object>) result.getData();
            int total = payDao.countforPay(param);
            List<PendingLondInformationVo> list = payDao.getPendingLondInformation(param);
            int page = Integer.parseInt(param.get("page").toString());
            int limit = Integer.parseInt(param.get("limit").toString());
            PageResult pageResult = PageResult.<PendingLondInformationVo>builder().page(page).limit(limit).data(list).count((long) total).build();
            return Result.succeed(pageResult, "成功");
        } else {
            return result;
        }
    }

    @Transactional
    @Override
    public Result getPaymentNote(Map<String, Object> params) throws ServiceException {
        try {
            PaymentNoteVo paymentNoteVo = payDao.getPaymentNote(params);
            if (paymentNoteVo != null) {
                return Result.succeed(paymentNoteVo, "成功获取放款单");
            } else {
                return Result.failed("无放款单信息");
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public Result pay(Map<String, Object> params) throws ServiceException {
        try {
            //1，更新融资主表financing_info  融资状态 - > 待还款
            payDao.updateFinancingStatus(params);

            //2，生成还款计划
            ProductInfoVo productInfoVo = payDao.getProductInfoVo(params);
            FinancingInfoVo financingInfoVo = payDao.getFinancingInfo(params);
            List<PayPlan> list = generateRepaymentPlan.generateMethod(productInfoVo, financingInfoVo);

            payDao.saveRepaymentPlan(list);
            return Result.succeed("支付成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}



