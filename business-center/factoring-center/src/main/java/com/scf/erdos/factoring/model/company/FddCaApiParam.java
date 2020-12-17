package com.scf.erdos.factoring.model.company;

import lombok.Data;

import java.io.Serializable;

/**
 * @author FUHAOCHENG
 * @version 1.0
 * @date 2020/11/12 16:50
 */

@Data
public class FddCaApiParam implements Serializable {

    private String app_id;// 接入方ID
    private String timestamp;//请求时间 yyyyMMddHHmmss
    private String v;//版本号
    /**
     * 示例： Base64( SHA1( app_id +MD5(timestamp) +SHA1(app_secret +agent_info+ bank_info+cert_flag+
     * company_info+
     * company_principal_type+
     * customer_id+legal_info+ notify_url+page_modify+ result_type+return_url+ verified_way+...) ))标记红色部分为接口业务参数，
     * 拼接规则： 对所有业务参数的key(参数名)按 照ASCII码顺序排序后拼接
     */
    private String msg_digest;//签名
    private String customer_id;// 客户编号
    private String page_modify;//是否允许用户页面修改 1允许 2不允许
    private String notify_url;//回调地址
    private String return_url;//同步通知url
    private String result_type;//刷脸是否显示结果页面 参数值为“1”：直接跳转 到return_url或法大大指 定页面， 参数值为“2”：需要用户 点击确认后跳转到 return_url或法大大指定 页面
    private String cert_flag;//是否认证成功后自动申 请实名证书 参数值为“0”：不申请 参数值为“1”：自动申请
    private String option;//add（新增） modify（修改）  不传默认add
    private String lang;//zh:中文，en:英文

    @Data
    public static class Company {
        private String verified_way;//实名认证套餐类型： 0：标准方案（对公打款 +纸质审核+法人身份+法 人授权）； 1：对公打款； 2：纸质审核； 3：法人身份（授权）认 证
        private String m_verified_way;//管理员认证套餐类型： 0：三要素标准方案； 1：三要素补充方案； 2：四要素标准方案； 3：四要素补充方案； 4：纯三要素方案； 5：纯四要素方案； 9：人脸识别方案
        private String company_principal_type;//企业负责人身份: 1. 法人 2. 代理人  默认是1法人
        private String verified_serialno;//管理员认证流水号
        private String organization_type;//0：企业； 1：政府/事业单位； 2：其他组织； 3：个体工商户
        private String authorization_file;//企业注册申请表
        private String legal_name;//法人姓名（代理人认证 想要传法人姓名可用此 参数）

        @Data
        public static class AgentInfo {
            private String agent_name;//代理人姓名
            private String agent_id;//代理人证件号
            private String agent_mobile;//代理人手机号（仅支持国内运营商）
            private String agent_id_front_path;//代理人证件正面照下载地址
            private String bank_card_no;//代理人银行卡号

        }
        @Data
        public static class Companyinfo {
            private String company_name;//企业名称
            private String credit_no;//统一社会信用代码
            private String credit_image_path;//统一社会信用代码证件照路径

        }
        @Data
        public static class Bankinfo {
            private String bank_name;//银行名称
            private String bank_id;//银行帐号
            private String subbranch_name;//开户支行名称

        }
        @Data
        public static class Legalinfo {
            private String legal_name;//法人姓名
            private String legal_id;//法人证件号
            private String legal_mobile;//法人手机号（仅支持国内运营商）
            private String legal_id_front_path;//法人证件正面照下载地址
            private String bank_card_no;//法人银行卡号


        }

    }

    @Data
    public static class Person {
        /**
         * 实名认证套餐类型： 0：三要素标准方案； 1：三要素补充方案； 2：四要素标准方案； 3：四要素补充方案； 4：纯三要素方案； 5：纯四要素方案； 9：人脸识别方案
         * 0、2-标准方案 [运营商三要素/银行卡四要素通 过，才能人脸识别认证，人脸认 证通过即认证通过]；
         * 1、3-补充方案 [运营商三要素/银行卡四要素通 过的情况下认证成功，不通过的 情况下进行人脸识别认证]；
         * 4、5-纯要素方案 [运营商三要素/银行卡四要素通 过，则认证通过]； 9-人脸识别方案 [默认二要素+人脸识别，后续只 允许刷脸签署；可配置增加手机 号采集，用户信息关联手机号后 允许使用短信签署]
         */
        private String verified_way;
        private String customer_name;//姓名
        private String customer_ident_type;//是否支持其他证件类型 身份证-0 其他1
        private String customer_ident_no;//证件号码：cert_type=0身份证号cert_type=1护照号cert_type=B港澳居民来 往内地通行证号
        private String mobile;// 手机号码
        private String ident_front_path;//证件正面照下载地址：cert_type=0:身份证正面cert_type=1:护照带人像 图片cert_type=B:港澳居民来 往内地通行证照带人像 图片// cert_type=C:台湾居民来 往大陆通行证照带人像 图片
        private String ident_back_path;//证件反面照下载地址：cert_type=0:身份证反面cert_type=1:护照封图片cert_type=B:港澳居民来 往内地通行证照封图图 片cert_type=C:台湾居民来 往大陆通行证照封图图 片
        private String id_photo_optional;//是否需要上传身份照片 0-只需要头像面 1-头像面与国徽面都需要 2-都不需要
        private String cert_type;//证件类型: 0：身份证； 1：护照； B：港澳居民来往内地通 行证, C：台湾居民来往大陆通 行证(默认为0，仅当支持其 他证件时，证件类型 1/B/C接口允许同步传 参)
        private String bank_card_no;//个人银行卡
        private String is_mini_program;//是否小程序认证，默认 为0 1：代表小程序认证； 0：非小程序  小程序支持的实名套餐类型： 0：三要素标准方案； 1：三要素补充方案； 2：四要素标准方案； 4：四要素补充方案： 9：人脸识别方案
    }


}
