package com.scf.erdos.factoring.service.impl;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.PageParamHandle;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.DocumentsDao;
import com.scf.erdos.factoring.service.IDocumentsService;
import com.scf.erdos.factoring.vo.pay.CreditDecisionVo;
import com.scf.erdos.factoring.vo.pay.ListofDocumentsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/9/28 11:11
 */

@Service
@SuppressWarnings("all")
public class DocumentsServiceImpl implements IDocumentsService {
    @Autowired
    private DocumentsDao documentsDao;

    @Autowired
    private PageParamHandle pageParamHandle;

    @Transactional
    @Override
    public Result getListofDocuments(Map<String, Object> map) throws ServiceException {
        Result result = pageParamHandle.handle(map);
        if (200 == result.getResp_code()) {
            Map<String, Object> param = (Map<String, Object>) result.getData();
            int total = documentsDao.countforListofDocuments(param);
            List<ListofDocumentsVo> list = documentsDao.getListofDocuments(param);
            int page = Integer.parseInt(param.get("page").toString());
            int limit = Integer.parseInt(param.get("limit").toString());
            PageResult pageResult = PageResult.<ListofDocumentsVo>builder().page(page).limit(limit).data(list).count((long) total).build();
            return Result.succeed(pageResult, "成功");
        } else {
            return result;
        }
    }

    @Transactional
    @Override
    public Result getCreditDecision(Map<String, Object> params) throws ServiceException {
        try {
            CreditDecisionVo creditDecisionVo = documentsDao.getCreditDecision(params);
            if(creditDecisionVo != null){
                return Result.succeed(creditDecisionVo,"成功获取授信决策意见书");
            }else{
                return Result.failed("无授信决策意见书信息");
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    @Transactional
    @Override
    public Result getBasicContract(Map<String, Object> params) throws ServiceException {
        try {
            String basicContract = documentsDao.getBasicContract(params);
            if(basicContract != null){
                return Result.succeed(basicContract,"成功获取基础合同");
            }else{
                return Result.failed("无基础合同信息");
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
