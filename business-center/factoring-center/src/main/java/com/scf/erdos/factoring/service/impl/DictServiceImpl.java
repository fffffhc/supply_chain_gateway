package com.scf.erdos.factoring.service.impl;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.DictDao;
import com.scf.erdos.factoring.service.IDictService;
import com.scf.erdos.factoring.vo.dict.CompanyVo;
import com.scf.erdos.factoring.vo.dict.DictListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description : 字典 接口实现类
 * @author：bao-clm
 * @date: 2020/5/11
 * @version：1.0
 */

@Service
@SuppressWarnings("all")
public class DictServiceImpl implements IDictService {
    @Autowired
    private DictDao dictDao;

    @Override
    public Result getCompanyDicts(Map<String, Object> params) throws ServiceException {
        try{
            List<DictListVo> list = dictDao.getCompanyDicts(params);

            Map map = list.stream().collect(Collectors.groupingBy(DictListVo::getType));

            return Result.succeed(map,"获取成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getProductDicts(Map<String, Object> params) throws ServiceException {
        try{
            List<DictListVo> list = dictDao.getProductDicts(params);

            Map map = list.stream().collect(Collectors.groupingBy(DictListVo::getType));

            return Result.succeed(map,"获取成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result getCompanyByType(Map<String, Object> params) throws ServiceException {
        try{
            List<CompanyVo> list = dictDao.getCompanyByType(params);
            return Result.succeed(list,"获取成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}
