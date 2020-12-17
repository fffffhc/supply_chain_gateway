package com.scf.erdos.factoring.service;

import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.Product.ProductContract;
import com.scf.erdos.factoring.model.Product.ProductInfo;
import com.scf.erdos.factoring.vo.product.ExportExcelProducts;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IProductService {
    Result add(ProductInfo productInfo) throws ServiceException;
    Result getProductInfo(Integer id)throws ServiceException;
    Result getAllProducts(Map<String, Object> params) throws ServiceException;
    Result addProductContract(ProductContract productContract) throws ServiceException;
    Result handleProduct(ProductInfo productInfo) throws ServiceException;
    Result delete(Integer id) throws ServiceException;
    Result update(ProductInfo productInfo) throws ServiceException;
    Result getContracts(Integer id) throws ServiceException;
    Result updateProductContract(ProductContract productContract);
    Result deleteProductContract(Integer id) throws ServiceException;
    Result getPlaceholder(Map<String, Object> params);
    Result getContractInfo(Integer id);
    List<ExportExcelProducts> exportExcel(String type);
}
