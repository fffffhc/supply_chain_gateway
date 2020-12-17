package com.scf.erdos.factoring.vo.company;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 企业注册信息完整情况对象
 * @author：bao-clm
 * @date: 2020/7/9
 * @version：1.0
 */

@Data
public class CompanyRegisterInfoVo implements Serializable {

    private Integer id;//主键
    private String companyAccount;
    private String status;
    private String caStatus;
}
