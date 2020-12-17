package com.scf.erdos.factoring.vo.signupOnline;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 合同签约情况
 * @author：bao-clm
 * @date: 2020/8/7
 * @version：1.0
 */

@Data
public class SignupStateVo implements Serializable {

    private String partyType;//签约方类型（a ，甲方 ；b，乙方；c，丙方；d，丁方；e，戊方）
    private String createTime;//签署时间
    private String status;//状态（0，待签署；1，已成功）

}
