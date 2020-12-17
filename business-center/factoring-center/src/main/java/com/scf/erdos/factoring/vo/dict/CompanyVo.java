package com.scf.erdos.factoring.vo.dict;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 企业
 * @author：bao-clm
 * @date: 2020/6/8
 * @version：1.0
 */

@Data
public class CompanyVo implements Serializable {

    private int id;
    private String companyName;
}
