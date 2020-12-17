package com.scf.erdos.factoring.controller;

import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.model.receivable.ReceivableInfo;
import com.scf.erdos.factoring.service.IReceivableService;
import com.scf.erdos.factoring.util.ExcelUtil;
import com.scf.erdos.factoring.vo.yszk.FapiaoExcelVo;
import com.scf.erdos.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;


/**
 * @Description : 应收账款管理 Controller
 * @author：bao-clm
 * @date: 2020/5/12
 * @version：1.0
 */

@Slf4j
@RestController
@RequestMapping("/receivable")
@Api(tags = "YSZK API")
public class ReceivableController {

    @Autowired
    private IReceivableService iYszkService;

    @ApiOperation(value = "获取应收账款列表-分页")
    @GetMapping("/get")
    @LogAnnotation(module = "receivable",recordRequestParam = false)
    public Result getYszks(@RequestParam Map<String, Object> params){
        return iYszkService.getYszks(params);
    }

    @ApiOperation(value = "获取应收账款详情")
    @GetMapping("/getYszkInfo/{id}")
    @LogAnnotation(module = "receivable",recordRequestParam = false)
    public Result getYszkInfo(@PathVariable Integer id){
        return iYszkService.getYszkInfo(id);
    }

    @ApiOperation(value = "保存应收账款")
    @PostMapping("/add")
    @LogAnnotation(module = "receivable",recordRequestParam = false)
    public Result add(@RequestBody ReceivableInfo yszkInfo){
        return iYszkService.add(yszkInfo);
    }

    @ApiOperation(value = "编辑应收账款")
    @PutMapping("/update")
    @LogAnnotation(module = "receivable",recordRequestParam = false)
    public Result update(@RequestBody ReceivableInfo yszkInfo){
        return iYszkService.update(yszkInfo);
    }

    @ApiOperation(value = "获取发票上传Excel模板")
    @GetMapping("/getExcelTemplate")
    @LogAnnotation(module = "receivable",recordRequestParam = false)
    public Result getExcelTemplate(@RequestParam Map<String, Object> params){
        return iYszkService.getExcelTemplate(params);
    }

    @ApiOperation(value = "解析 Excel数据")
    @PostMapping("/analysisExcel")
    public Result analysisExcel(HttpServletRequest request,@RequestParam("file") MultipartFile file) throws IOException {
        Object objList = ExcelUtil.readExcel(file, new FapiaoExcelVo(), 1, 4);
        return Result.succeed(objList,"获取成功");
    }

}
