package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.Product.ProductContract;
import com.scf.erdos.factoring.model.Product.ProductInfo;
import com.scf.erdos.factoring.service.IProductService;
import com.scf.erdos.factoring.util.ExcelUtil;
import com.scf.erdos.factoring.vo.product.ExportExcelProducts;
import com.scf.erdos.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Description : 融资产品 Controller
 * @author：bao-clm
 * @date: 2020/5/22
 * @version：1.0
 */

@Slf4j
@RestController
@RequestMapping("/product")
@Api(tags = "PRODUCT API")
public class ProductController {

    @Autowired
    private IProductService  iProductService;

    @ApiOperation(value = "保存融资产品信息")
    @PostMapping("/add")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result save(@Validated ProductInfo productInfo){
        return iProductService.add(productInfo);
    }

    @ApiOperation(value = "获取产品详情")
    @GetMapping("/getProductInfo/{id}")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result getProductInfo(@PathVariable Integer id){
        return iProductService.getProductInfo(id);
    }

    @ApiOperation(value = "获取产品列表-分页")
    @GetMapping("/getAllProducts")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result getAllProducts(@RequestParam Map<String, Object> params){
        return iProductService.getAllProducts(params);
    }

    @ApiOperation(value = "产品激活/停用")
    @PutMapping("/handleProduct")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result handleProduct(@Validated ProductInfo productInfo){
        return iProductService.handleProduct(productInfo);
    }

    @ApiOperation(value = "产品删除")
    @DeleteMapping("/delete/{id}")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result delete(@PathVariable Integer id){
        return iProductService.delete(id);
    }

    @ApiOperation(value = "产品信息修改")
    @PutMapping("/update")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result update(@Validated ProductInfo productInfo){
        return iProductService.update(productInfo);
    }

    @ApiOperation(value = "获取产品电子合同列表")
    @GetMapping("/getContracts/{id}")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result getContracts(@PathVariable Integer id){
        return iProductService.getContracts(id);
    }

    @ApiOperation(value = "添加产品合同")
    @PostMapping("/addProductContract")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result addProductContract(@Validated ProductContract productContract){
        return iProductService.addProductContract(productContract);
    }

    @ApiOperation(value = "修改产品合同")
    @PutMapping("/updateProductContract")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result updateProductContract(@Validated ProductContract productContract){
        return iProductService.updateProductContract(productContract);
    }

    @ApiOperation(value = "删除产品合同")
    @DeleteMapping("/deleteProductContract/{id}")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result deleteProductContract(@PathVariable Integer id){
        return iProductService.deleteProductContract(id);
    }

    @ApiOperation(value = "获取占位符")
    @GetMapping("/getPlaceholder")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result getPlaceholder(@RequestParam Map<String, Object> params){
        return iProductService.getPlaceholder(params);
    }

    @ApiOperation(value = "export-excel")
    @GetMapping("/exportExcel")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public void exportExcel(@RequestParam Map<String, Object> params,HttpServletRequest request,HttpServletResponse response) {
       String type = params.get("type").toString();
       List<ExportExcelProducts> result = iProductService.exportExcel(type);

       String fileName = "占位符";
       String sheetName = "占位符";
        try{
            ExcelUtil.writeExcel(response,result,fileName,sheetName,new ExportExcelProducts());
        }catch (Exception e){
            log.error("导出异常" + e.getMessage());
        }
    }

    @ApiOperation(value = "产品电子合同详情")
    @GetMapping("/getContractInfo/{id}")
    @LogAnnotation(module = "product",recordRequestParam = false)
    public Result getContractInfo(@PathVariable Integer id){
        return iProductService.getContractInfo(id);
    }
}
