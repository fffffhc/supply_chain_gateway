package com.scf.erdos.factoring.service.impl;

import cn.hutool.json.JSONObject;
import com.scf.erdos.common.auth.details.LoginAppUser;
import com.scf.erdos.common.constant.CompanyRole;
import com.scf.erdos.common.constant.FddConstant;
import com.scf.erdos.common.exception.service.ServiceException;
import com.scf.erdos.common.model.SysRole;
import com.scf.erdos.common.model.SysUser;
import com.scf.erdos.common.util.ApiSign;
import com.scf.erdos.common.util.SysUserUtil;
import com.scf.erdos.common.web.PageResult;
import com.scf.erdos.common.web.Result;
import com.scf.erdos.factoring.dao.CompanyDao;
import com.scf.erdos.factoring.feign.fdd.FDDFeignClient;
import com.scf.erdos.factoring.feign.UserFeignClient;
import com.scf.erdos.factoring.feign.fdd.FDDFeignClientImpl;
import com.scf.erdos.factoring.service.ICompanyService;
import com.scf.erdos.factoring.vo.company.CompanyInfoVo;
import com.scf.erdos.factoring.vo.company.CompanyPageVo;
import com.scf.erdos.factoring.vo.company.CompanyRegisterInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description : 企业管理 service 实现类
 * @author：bao-clm
 * @date: 2020/5/11
 * @version：1.0
 */

@Slf4j
@Service
@SuppressWarnings("all")
public class CompanyServiceImpl implements ICompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private FDDFeignClient fddFeignClient;

    @Autowired
    private FDDFeignClientImpl fDDFeignClientImpl;

    @Transactional
    @Override
    public Result getAllCompany(Map<String, Object> params) throws ServiceException{
        try {
            Integer page = MapUtils.getInteger(params, "page");
            Integer limit = MapUtils.getInteger(params, "limit");

            if (page == null || limit == null) {
                return Result.failed("page，limit不能为空！");
            }

            if(page < 1){
                return Result.failed("page不能小于1，首页从1开始！");
            }

            params.put("currentPage",(page - 1)*limit);
            params.put("pageSize",limit);

            int total = companyDao.count(params);
            List<CompanyPageVo> list = companyDao.getAllCompany(params);
            List<String> userIds = list.stream().map(CompanyPageVo::getUserId).collect(Collectors.toList());

            List<CompanyPageVo> newList = new ArrayList<CompanyPageVo>();
            if(userIds.size() > 0){
                //获取用户信息
                List<SysUser> userList = userFeignClient.findByUserIds(userIds);
                //用户信息组装
                newList = list.stream()
                        .map(companyPageVo -> userList.stream()
                                .filter(loginAppUser -> companyPageVo.getUserId().equals(String.valueOf(loginAppUser.getId())))
                                .findFirst()
                                .map(loginAppUser -> {
                                    companyPageVo.setAccount(loginAppUser.getUsername());
                                    companyPageVo.setContacterName(loginAppUser.getNickname());
                                    companyPageVo.setContacterMobile(loginAppUser.getPhone());
                                    return companyPageVo;
                                }).orElse(null))
                        .collect(Collectors.toList());
            }

            PageResult pageResult = PageResult.<CompanyPageVo>builder().page(page).limit(limit).data(newList).count((long)total).build();
            return Result.succeed(pageResult,"成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public Result getCompanyInfo(Map<String, Object> params) throws ServiceException{
        try {
            CompanyInfoVo companyInfoVo = companyDao.getCompanyInfo(params);
            if(companyInfoVo != null){
                return Result.succeed(companyInfoVo,"成功获取企业详情信息");
            }else{
                return Result.failed("无企业信息");
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public Result add(Map<String, Object> params) throws ServiceException{
        try {
            CompanyInfoVo companyInfoVo = companyDao.getCompanyByCreditNoAndName(params);
            if(companyInfoVo != null){
                return Result.failed("改企业信息已录入，您不能重复录入");
            }else{
                /**
                 * 获取当前用户登陆信息
                 */
                LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
                if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");

                params.put("userId",loginUser.getId());
                params.put("sourceType","1000");//企业注册渠道  1000 平台客户
                int i = companyDao.add(params);
                return i > 0 ? Result.succeed("保存企业信息成功"): Result.failed("保存企业信息失败");
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public Result update(Map<String, Object> params) throws ServiceException{
        try {
            CompanyInfoVo companyInfoVo = companyDao.getCompanyByCreditName(params);
            if(companyInfoVo != null){
                return Result.failed("改企业信息已录入，您不能重复录入");
            }else{
                /**
                 * 获取当前用户登陆信息
                 */
                LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
                if(loginUser == null)
                    return Result.failed("获取当前用户登陆信息失败");

                params.put("userId",loginUser.getId());
                params.put("status","1");//修改完后，变待审批
                int i = companyDao.update(params);
                return i > 0 ? Result.succeed("编辑企业信息成功"): Result.failed("编辑企业信息失败");
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public Result audit(Map<String, Object> params) throws ServiceException{
        try {
            /**
             * 获取当前用户登陆信息
             */
            LoginAppUser loginUser = SysUserUtil.getLoginAppUser();
            if(loginUser == null)
                return Result.failed("获取当前用户登陆信息失败");
            params.put("auditUserId",loginUser.getId());

            /**
             * 1，更新企业主表信息状态 company_info
             * 2，企业审核日志表审核信息保存 company_audit_info
             * 3，如果企业审核通过的话，更新企业联系人信息表企业id 填充及赋予角色
             *    审核通过 ：status = 3
             */
            int i = companyDao.audit(params);
            int j = companyDao.update(params);

            if("3".equals(params.get("status"))){
                CompanyInfoVo companyInfoVo = companyDao.getCompanyInfo(params);
                params.put("userId",companyInfoVo.getUserId());
                boolean bl = userFeignClient.updateCompanyUser(params);
            }

            return Result.succeed("审核成功");
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result settleIn(Map<String, Object> params) throws ServiceException {
        try {
            int i = companyDao.settleIn(params);
            return i > 0 ? Result.succeed("企业入驻申请成功"): Result.failed("企业入驻申请失败");
        }catch (Exception e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public CompanyRegisterInfoVo findConpanyInfoByUserId(Long userId) {
        CompanyRegisterInfoVo companyRegisterInfoVo = new CompanyRegisterInfoVo();

        CompanyRegisterInfoVo cv = companyDao.findConpanyInfoByUserId(userId);
        if(cv == null){
            companyRegisterInfoVo.setCaStatus("0");
            companyRegisterInfoVo.setCompanyAccount("0");
            companyRegisterInfoVo.setStatus("0");
        }else{
            companyRegisterInfoVo = cv;
        }

        return companyRegisterInfoVo;
    }

    @Override
    public Result getTianyancha(String keyword) {

        String token = "d36a4844-58d6-4376-bd49-856bba35aaf6";
        String url = "http://open.api.tianyancha.com/services/open/ic/baseinfo/2.0?id=11684584&name=中航重机股份有限公司&keyword=中航重机股份有限公司";
        System.out.println(executeGet(url, token));
        return Result.failed("获取当前用户登陆信息失败");
    }

    protected static String executeGet(String url1, String token) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://open.api.tianyancha.com/services/open/ic/baseinfo/2.0?id=11684584&name=中航重机股份有限公司&keyword=中航重机股份有限公司";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization",token);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        //看得懂吧
        String strbody = restTemplate.exchange(url, HttpMethod.GET, entity,String.class).getBody();

        return strbody;
    }

    @Transactional
    @Override
    public Result authCA(Map<String, Object> params) {
        /**
         * 企业ca 认证：
         * 一个企业可以多个ca 认证，默认第一个企业认证用户为企业管理者，有电子签章使用权限；
         * 如果想移交到下一个人，请企业管理者主动移交使用电子签章权限。
         *
         * 一般企业担保人角色只能 个人身份认证。
         *
         */

        LoginAppUser loginUser = SysUserUtil.getLoginAppUser(); //获取当前用户登陆信息
        if (loginUser == null)
            return Result.failed("获取当前用户登陆信息失败");
        String userId = loginUser.getId().toString();
        String caStatus = loginUser.getCaStatus(); //0，未ca认证；1，在认证中；2，认证失败；3，认证成功

        Set<SysRole> roles = loginUser.getSysRoles();
        List<SysRole> roleList = new ArrayList(roles);
        String roleCode = roleList.get(0).getCode();//用户角色code，系统用户只有一个角色


        /**
         * 1，认证中
         */
        if (FddConstant.CA_STATUS_1.equals(caStatus)) {
            return Result.succeed("CA正在认证中");
        }

        String account_type = FddConstant.ACCOUNT_TYPE_2;//1:个人；2:企业
        if (CompanyRole.COMPANY_10000_04_ROLE.equals(roleCode) || CompanyRole.COMPANY_10001_02_ROLE.equals(roleCode)) {
            account_type = FddConstant.ACCOUNT_TYPE_1;//一般企业担保人角色只能 个人身份认证。
        }

        /**
         * 2，未认证
         */

        if (FddConstant.CA_STATUS_0.equals(caStatus)) {
            String registerResult = fDDFeignClientImpl.fddRegister(userId, account_type);//用户注册返回结果
            JSONObject registerResultObj = new JSONObject(registerResult);
            String code = registerResultObj.get("code").toString();
            String msg = registerResultObj.get("msg").toString();
            if (FddConstant.FDD_CODE_SUCCESS.equals(code)) {
                String customerId = registerResultObj.get("data").toString();
                return getVerifiedUrl(account_type, customerId, loginUser,true);
            } else {
                return Result.failed("用户法大大注册失败",msg);
            }
        }

        /**
         * 3，重新认证
         */
        if (FddConstant.CA_STATUS_2.equals(caStatus) || FddConstant.CA_STATUS_3.equals(caStatus)) {
            Boolean flag = (Boolean) params.get("flag");//是否重新认证
            if (flag) {
                String registerResult = fDDFeignClientImpl.fddRegister(userId, account_type);//用户注册返回结果
                JSONObject registerResultObj = new JSONObject(registerResult);
                String code = registerResultObj.get("code").toString();
                if (FddConstant.FDD_CODE_SUCCESS.equals(code)) {
                    String customerId = registerResultObj.get("customer_id").toString();
                    return getVerifiedUrl(account_type, customerId, loginUser,true);
                } else {
                    return Result.failed("用户法大大注册失败");
                }
            }

        }

        return null;
    }

    /**
     * 用户ca 获取法大大实名认证地址
     *
     * @param account_type
     * @param customerId
     * @param loginUser
     * @return
     */
    public Result getVerifiedUrl(String account_type,String customerId,LoginAppUser loginUser,Boolean isinsertOrupdate) {
        Map<String,Object> map = new HashMap<>();
        /**
         * 1，企业用户ca 实名认证获取地址
         */
        String result = "";

        if(FddConstant.ACCOUNT_TYPE_1.equals(account_type)) {
            result = fDDFeignClientImpl.fddPersonCa(customerId);// 个人用户ca 实名认证获取地址
        }else if(FddConstant.ACCOUNT_TYPE_2.equals(account_type)){
            result = fDDFeignClientImpl.fddCompanyCa(customerId);// 企业用户ca 实名认证获取地址
        }


        JSONObject fddCaResultObj = new JSONObject(result);
        String code = fddCaResultObj.get("code").toString();
        if(FddConstant.FDD_CODE_SUCCESS.equals(code)){
            JSONObject data = (JSONObject)fddCaResultObj.get("data");
            String transactionNo = data.get("transactionNo").toString();//fdd返回交易号
            String url = new String(Base64.getDecoder().decode(data.get("url").toString()), StandardCharsets.UTF_8);
                map.put("requestParamJson",customerId);//请求参数json
                map.put("responseParamJson",result);//返回参数json
                map.put("userId",loginUser.getId());//用户id
                map.put("companyId",loginUser.getCompanyId());//企业id
            companyDao.insertCompanyCaAuthLog(map);

                map.put("companyId",loginUser.getCompanyId());
                map.put("userId",loginUser.getId());
                map.put("platformCustomerId",customerId);
                map.put("apiVersion",FddConstant.V);
                map.put("transactionNo",transactionNo);
                map.put("url",url);
                map.put("status",1);//认证中
                map.put("platformType","fdd”“");//法大大
                map.put("accountType",account_type);
            if (isinsertOrupdate){
                companyDao.insertCompanyCaAuth(map);
            }else {companyDao.updateCompanyCaAuth(map);}

            return Result.succeed("获取企业实名认证地址成功!",url);

        }else{
            return Result.failed("获取企业实名认证地址失败!");
        }

    }


    /**
     * fdd回调接口
     * @param params
     * @return
     */
    @Override
    public Result fddCaAuthCallBack(Map<String, Object> params) {
        String app_id = FddConstant.APP_ID;//接入方的ID
        String app_secret = FddConstant.APP_SECRET;//接入方的密钥

        Map<String,Object> map = new HashMap<>();
            map.put("authenticationType",params.get("authenticationType").toString());
            map.put("certStatus",params.get("certStatus").toString());
            map.put("customerId",params.get("customerId").toString());
            map.put("serialNo",params.get("serialNo").toString());
            map.put("status",params.get("status").toString());
            map.put("statusDesc",params.get("statusDesc").toString());
        String mySign = ApiSign.fddParam(app_id, params.get("timestamp").toString(), app_secret, map);//签名
        String sign = params.get("sign").toString(); // 法大大-调接口时的签名

        if(sign == null || !mySign.equals(sign)){
            return Result.failed("签名有误！");
        }

        /**
         * 个人
         * Status：
         * 0：未激活；
         * 1：未认证；
         * 2：审核通过；
         * 3：已提交待审核；
         * 4：审核不通过;
         *
         * 企业
         * status：
         * 0：未认证；
         * 1：管理员资料已提交；
         * 2：企业基本资料(没有申请表) 已提交；
         * 3：已提交待审核；
         * 4：审核通过；
         * 5：审核不通过；
         * 6：人工初审通过
         *
         * 个人： 2，3，4
         * 企业： 3，4，5
         * 以上状态发生变化就会调用异步通知接口，地址能访问http状态码是200就
         * 只回调一次，如果不是就会再次发起回调，连续异步通知3次，间隔10分
         * 钟，如果3次之后通知还是失败，建议主动查询个人/企业实名认证信息
         */
        String myStatus = "";
        String status = params.get("status").toString();
        String authenticationType = params.get("authenticationType").toString();//1：个人,2：企业

        if(authenticationType.equals(FddConstant.ACCOUNT_TYPE_1)){
            if(status.equals(FddConstant.P_STATUS_2)){
                myStatus = FddConstant.CA_STATUS_3;
            }else if(status.equals(FddConstant.P_STATUS_3)){
                myStatus = FddConstant.CA_STATUS_1;
            }else if(status.equals(FddConstant.P_STATUS_4)){
                myStatus = FddConstant.CA_STATUS_2;
            }
        }

        if(authenticationType.equals(FddConstant.ACCOUNT_TYPE_2)){
            if(status.equals(FddConstant.C_STATUS_3)){
                myStatus = FddConstant.CA_STATUS_1;
            }else if(status.equals(FddConstant.C_STATUS_4)){
                myStatus = FddConstant.CA_STATUS_3;
            }else if(status.equals(FddConstant.C_STATUS_5)){
                myStatus = FddConstant.CA_STATUS_2;
            }
        }

        params.put("myStatus",myStatus);
        params.put("requestParamJson",params.toString());//请求参数json
        params.put("responseParamJson","");//返回参数json
        //companyDao.saveCallBackLog(params);
        int count = companyDao.updateCompanyCaStatus(params);
        return Result.succeed("SUCCESS");

    }

    @Override
    public Result sealUpload(Map<String, Object> params) {
        /*LoginAppUser loginUser = SysUserUtil.getLoginAppUser(); //获取当前用户登陆信息
        if (loginUser == null)
            return Result.failed("获取当前用户登陆信息失败");
        String customerId = loginUser.getFddCustomerId().toString();
        String signatureImgBase64 = params.get("signatureImgBase64").toString();*/

        String customerId = "D47C10659158A5D667CD5BC33F9A5DF9";
        String userId = "2135";
        /*byte[] data2 = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream("D:\\timg.jpg");
            data2 = new byte[in.available()];
            in.read(data2);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组进行Base64编码，得到Base64编码的字符串
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Str = encoder.encode(data2);*/

        String base64Str = ",iVBORw0KGgoAAAANSUhEUgAAAHgAAABRCAIAAACWpfV7AAACtUlEQVR4Ae2cPW+DMBRFaVW5i1nwhDIw8f9/EBNDxGSWOEM99ZmvNI2rpJW5hfYyNImNfcnx4WFl6NP5fM54rE/gef0IJgQCBA3ygKAJGkQAFEOjCRpEABRDowkaRAAUQ6MJGkQAFEOjCRpEABRDowkaRAAUQ6MJGkQAFEOjCRpEABRDowkaRAAUQ6MJGkQAFEOjCRpEABRDowkaRAAUQ6MJGkQAFEOjCRpEABRDowkaRAAUQ6MJGkQAFEOjCRpEABRDowkaRAAUQ6MJGkQAFEOjQaBfQDnfjzkej98flGzE4XBINtcw0UaN/l3KQib5BWwUdFqbtjAbQYNWgaBBoLf7MPwCgC7rUvVta/14gjJVVajpZNc1nYsNvB51GeM/zBQbl65tR6BHPM5dk1RK3aWljNFZNq1Mpsuq8F3TyjyBvzkti5YO6+1MOyod3rZN03Snmy/h32aGN12hQZlSu35ZHp1r39vxo7N9pvP5foiOTtW4I9DRr6xeVfByOErRNhyifl2ZCZ9gLry1b2NX5K/cEpHW5E07Kh3R7x40t0NPqCyVuRTvoVHKhHatVAmdz8O996ow2oZiro2U98X1+YxVXvcO+gJFtHWFyZW1C3xdhudmd11YpLdTtdwEMtT3vfNL8b7MtcK7vwP6Bs7wCFRZVRdzV1XrYZchm5NmbJOqo07d3L/m695Bh02HH5QVfeUp18r7UERCwViqigAUosZ/qivSGIxn6XhIsLysp2303V3eNN+4TQwfHh3x0IXcOelpm//3LvlvOncwxLrT/oC39+1djNAm2wgatCwE/b9Bp62PP2CZ/AI2+jD8AZqND2HpAC0QQRM0iAAohkYTNIgAKIZGEzSIACiGRhM0iAAohkYTNIgAKIZGEzSIACiGRhM0iAAohkYTNIgAKIZGEzSIACjmHRRasLsWgrvJAAAAAElFTkSuQmCC";

        String result = fDDFeignClientImpl.sealUpload(customerId,base64Str);

        JSONObject sealUploadobj = new JSONObject(result);
        String code = sealUploadobj.get("code").toString();
        if(FddConstant.FDD_CODE_SUCCESS.equals(code)){
            JSONObject data = (JSONObject)sealUploadobj.get("data");
            String signatureId = data.get("signature_id").toString();
            String signatureSubInfo = data.get("signature_sub_info").toString();

            Map<String,Object> map = new HashMap<>();
            map.put("userId",userId);//loginUser.getId().toString()
            map.put("signatureId",signatureId);
            map.put("signatureSubInfo",signatureSubInfo);
            companyDao.getSignatureId(map);
            return Result.succeed("上传印章成功");

        }else {
            return Result.failed("上传印章失败");
        }

    }

    @Override
    public Result contractUpload(Map<String, Object> params) {
        /*String contractId = params.get("contractId").toString();
        String docTitle = params.get("docIitle").toString();
        String docUrl = params.get("docUrl").toString();*/

        String contractId = "1";
        String docTitle = "fuhaocheng";
        String docUrl = "http://erdos-scf-platform-test.oss-cn-zhangjiakou.aliyuncs.com/2020/12/01/xx/bd0a6f29-2bd3-421c-b9ce-f886d57e1276.pdf";
        String docType = ".pdf";

        String result = fDDFeignClientImpl.contractUpload(contractId,docTitle,docUrl,docType);

        JSONObject contractUploadObj = new JSONObject(result);
        String code = contractUploadObj.get("code").toString();
        String msg = contractUploadObj.get("msg").toString();
        if(FddConstant.CONTRACT_UPLOAD_1000.equals(code)){
            return Result.succeed("操作成功",msg);
        }else if (FddConstant.CONTRACT_UPLOAD_2001.equals(code)){
            return Result.failed("参数缺失或者不合法",msg);

        }else if (FddConstant.CONTRACT_UPLOAD_2002.equals(code)){
            return Result.failed("业务异常",msg);

        }else if (FddConstant.CONTRACT_UPLOAD_2003.equals(code)){
            return Result.failed("其他错误，请联系法大大",msg);
        }else {
            return Result.failed("获取返回值code失败");
        }
    }

    @Override
    public Result extsign(Map<String, Object> params) {
        String transactionId = params.get("transactionId").toString();
        String contractId = params.get("contractId").toString();
        String customerId = params.get("customerId").toString();
        String docTitle = params.get("docTitle").toString();
        String signKeyword = params.get("signKeyword").toString();
        String returnUrl = params.get("returnUrl").toString();
        String result = fDDFeignClientImpl.extsign(transactionId,contractId,customerId,docTitle,signKeyword,returnUrl);

        return Result.succeed(result);
    }


}


