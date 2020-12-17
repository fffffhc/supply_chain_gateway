package com.scf.erdos.factoring.dao;

import com.scf.erdos.factoring.model.Product.ProductContract;
import com.scf.erdos.factoring.model.Product.ProductInfo;
import com.scf.erdos.factoring.vo.product.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductDao {
    @Select("select name from product_info where name = #{name}")
    String getProductNameByName(@Param("name") String name);

    @Select("select name from product_info where name = #{name} and id != #{id}")
    String getProductNameByNameId(@Param("name") String name,@Param("id") Integer id);

    @Select("select code from product_info where code = #{code}")
    String getCode(@Param("code") String code);

    int add(ProductInfo productInfo);

    ProductInfoVo getProductInfo(@Param("id") Integer id);

    int count(Map<String, Object> params);
    List<ProductPageVo> getAllProducts(Map<String, Object> params);

    int addProductContract(ProductContract productContract);

    @Select("SELECT COUNT(*) FROM financing_info where product_id = #{id} and status != '6'")
    int getBeUsed(@Param("id") Integer id);

    @Update("update product_info set status = #{status} where id = #{id}")
    int handleProduct(ProductInfo productInfo);

    @Select("select id from product_contract where product_id = #{id} limit 1")
    String getContract(@Param("id") Integer id);

    @Delete("update product_info set is_delete = true where id = #{id}")
    int delete(@Param("id") Integer id);

    int update(ProductInfo productInfo);

    List<ProductContractsVo> getContracts(@Param("id") Integer id);

    @Select("select code from product_contract where product_id = #{productId} and code = #{code} and is_delete = '0'")
    String getProductContractCode(ProductContract productContract);

    int updateProductContract(ProductContract productContract);

    @Update("update product_contract set is_delete = true where id = #{id}")
    int deleteProductContract(@Param("id") Integer id);

    List<ProductContractPlaceholder> getPlaceholder(Map<String, Object> params);

    @Select("select * from product_contract where id = #{id}")
    ProductContractInfoVo getContractInfo(@Param("id") Integer id);

    List<ExportExcelProducts> exportExcel(@Param("type") String type);

    @Select("select id,contract_name from product_contract_template where company_id = #{companyId} and is_delete = false")
    List<ContractTemplateVo> getContractTemplates(@Param("companyId") String companyId);

}
