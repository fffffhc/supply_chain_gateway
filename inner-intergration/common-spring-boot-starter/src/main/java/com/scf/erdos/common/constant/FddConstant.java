package com.scf.erdos.common.constant;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/11/10 13:46
 */
public class FddConstant {

    public final static String APP_ID = "403943";//接入方ID
    public final static String APP_SECRET = "kjF8sJVh3i3dy9gjrmFhBpXU";//接入方的密钥
    public final static String V = "2.0";//版本号
    public final static String URL = "http://test.api.fabigbig.com:8888/api"; //接口URL
    public final static String REGISTERURL = "/account_register.api"; //客户注册
    public final static String COMPANY_VERIFIED = "/get_company_verify_url.api";//企业实名认证
    public final static String PERSON_VERIFIED = "/get_person_verify_url.api";//个人实名认证
    public final static String SEAL_UPLOAD = "/add_signature.api";//签章上传
    public final static String CONTRACT_UPLOAD = "/uploaddocs.api";//合同上传
    public final static String EXTSIGN = "/extsign.api";//合同手动签署

    public final static String FDD_CA_AUTH_CALLBACK = "http://rongxin.chinaerdos.com/factoring-center/company/fdd/ca/auth/callBack";//法大大ca 认证结果回调地址

    public final static String FDD_CODE_SUCCESS = "1";
    public final static String FDD_CODE_ERROR = "0";

    /**
     * 合同上传返回状态码
     * 1000：操作成功
     * 2001：参数缺失或者不合法
     * 2002：业务异常，失败原因见msg
     * 2003：其他错误，请联系法大大
     */
    public final static String CONTRACT_UPLOAD_1000 = "1000";
    public final static String CONTRACT_UPLOAD_2001 = "2001";
    public final static String CONTRACT_UPLOAD_2002 = "2002";
    public final static String CONTRACT_UPLOAD_2003 = "2003";

    /**
     * 模板上传
     * 1：操作成功
     * 2001：参数缺失或者不合法
     * 2002：业务异常，失败原因见msg
     * 2003：其他错误，请联系法大大
     */
    public final static String TEMPLATE_UPLOAD_1 = "1";
    public final static String TEMPLATE_UPLOAD_2001 = "2001";
    public final static String TEMPLATE_UPLOAD_2002 = "2002";
    public final static String TEMPLATE_UPLOAD_2003 = "2003";

    /**
     * CA认证状态
     * 0，未ca认证；1，在认证中；2，认证失败；3，认证成功
     */
    public final static String CA_STATUS_0 = "0";
    public final static String CA_STATUS_1 = "1";
    public final static String CA_STATUS_2 = "2";
    public final static String CA_STATUS_3 = "3";

    /**
     * 1:个人
     * 2:企业
     */
    public final static String ACCOUNT_TYPE_1 = "1";
    public final static String ACCOUNT_TYPE_2 = "2";


    /**
     * 实名认证套餐类型：
     * 0：标准方案（对公打款 +纸质审核+法人身份+法 人授权）；
     * 1：对公打款；
     * 2：纸质审核；
     * 3：法人身份（授权）认 证
     */
    public final static String C_VERIFIED_WAY_0 = "0";
    public final static String C_VERIFIED_WAY_1 = "1";
    public final static String C_VERIFIED_WAY_2 = "2";
    public final static String C_VERIFIED_WAY_3 = "3";


    /**
     * 管理员认证套餐类型：
     * 0：三要素标准方案；
     * 1：三要素补充方案；
     * 2：四要素标准方案；
     * 3：四要素补充方案；
     * 4：纯三要素方案；
     * 5：纯四要素方案；
     * 9：人脸识别方案
     * <p>
     * 0、2-标准方案 [运营商三要素/银行卡四要素通 过，才能人脸识别认证，人脸认 证通过即认证通过]；
     * 1、3-补充方案 [运营商三要素/银行卡四要素通 过的情况下认证成功，不通过的 情况下进行人脸识别认证]；
     * 4、5-纯要素方案 [运营商三要素/银行卡四要素通 过，则认证通过]；
     * 9-人脸识别方案 [默认二要素+人脸识别，后续只 允许刷脸签署；可配置增加手机 号采集，用户信息关联手机号后 允许使用短信签署]
     */
    public final static String M_VERIFIED_WAY_0 = "0";
    public final static String M_VERIFIED_WAY_1 = "1";
    public final static String M_VERIFIED_WAY_2 = "2";
    public final static String M_VERIFIED_WAY_3 = "3";
    public final static String M_VERIFIED_WAY_4 = "4";
    public final static String M_VERIFIED_WAY_5 = "5";
    public final static String M_VERIFIED_WAY_9 = "9";


    /**
     * 是否允许用户页面修改
     * 1允许
     * 2不允许
     */
    public final static String PAGE_MODIFY_1 = "1";
    public final static String PAGE_MODIFY_2 = "2";


    /**
     * 刷脸是否显示结果页面 参数值为“1”：直接跳转 到return_url或法大大指 定页面，
     * 参数值为“2”：需要用户 点击确认后跳转到 return_url或法大大指定 页面
     */
    public final static String RESULT_TYPE_1 = "1";
    public final static String RESULT_TYPE_2 = "2";


    /**
     * 是否认证成功后自动申 请实名证书
     * 参数值为“0”：不申请
     * 参数值为“1”：自动申请
     */
    public final static String CERT_FLAG_0 = "0";
    public final static String CERT_FLAG_1 = "1";


    /**
     * 组织类型
     * 0：企业；
     * 1：政府/事业单位；
     * 2：其他组织；
     * 3：个体工商户
     */
    public final static String ORGANIZATION_TYPEU_0 = "0";
    public final static String ORGANIZATION_TYPEU_1 = "1";
    public final static String ORGANIZATION_TYPEU_2 = "2";
    public final static String ORGANIZATION_TYPEU_3 = "3";


    /**
     * zh:中文，en:英文
     */
    public final static String LANG_ZH = "zh";
    public final static String LANG_EN = "en";


    /**
     * 个人实名认证套餐类型：
     * 0：三要素标准方案；
     * 1：三要素补充方案；
     * 2：四要素标准方案；
     * 3：四要素补充方案；
     * 4：纯三要素方案；
     * 5：纯四要素方案；
     * 9：人脸识别方案
     * 0、2-标准方案 [运营商三要素/银行卡四要素通 过，才能人脸识别认证，人脸认 证通过即认证通过]；
     * 1、3-补充方案 [运营商三要素/银行卡四要素通 过的情况下认证成功，不通过的 情况下进行人脸识别认证]；
     * 4、5-纯要素方案 [运营商三要素/银行卡四要素通 过，则认证通过]；
     * 9-人脸识别方案 [默认二要素+人脸识别，后续只 允许刷脸签署；可配置增加手机 号采集，用户信息关联手机号后 允许使用短信签署]
     */
    public final static String U_VERIFIED_WAY_0 = "0";
    public final static String U_VERIFIED_WAY_1 = "1";
    public final static String U_VERIFIED_WAY_2 = "2";
    public final static String U_VERIFIED_WAY_3 = "3";
    public final static String U_VERIFIED_WAY_4 = "4";
    public final static String U_VERIFIED_WAY_5 = "5";
    public final static String U_VERIFIED_WAY_9 = "9";


    /**
     * 证件类型:
     * 0：身份证；
     * 1：护照；
     * B：港澳居民来往内地通行证,
     * C：台湾居民来往大陆通行证
     * (默认为0，仅当支持其他证件时，证件类型1/B/C接口允许同步传参)
     */
    public final static String CERT_TYPE_0 = "0";
    public final static String CERT_TYPE_1 = "1";
    public final static String CERT_TYPE_B = "B";
    public final static String CERT_TYPE_C = "C";


    /***法大大 -- 获取ca认证地址接口回调接口返回结果 **********************************************************************/

    /**
     * 个人
     * Status：
     * 0：未激活；
     * 1：未认证；
     * 2：审核通过；
     * 3：已提交待审核；
     * 4：审核不通过;
     */
    public final static String P_STATUS_0 = "0";
    public final static String P_STATUS_1 = "1";
    public final static String P_STATUS_2 = "2";
    public final static String P_STATUS_3 = "3";
    public final static String P_STATUS_4 = "4";

    /**
     * 企业
     * status：
     * 0：未认证；
     * 1：管理员资料已提交；
     * 2：企业基本资料(没有申请表) 已提交；
     * 3：已提交待审核；
     * 4：审核通过；
     * 5：审核不通过；
     * 6：人工初审通过
     */
    public final static String C_STATUS_0 = "0";
    public final static String C_STATUS_1 = "1";
    public final static String C_STATUS_2 = "2";
    public final static String C_STATUS_3 = "3";
    public final static String C_STATUS_4 = "4";
    public final static String C_STATUS_5 = "5";
    public final static String C_STATUS_6 = "6";

}
