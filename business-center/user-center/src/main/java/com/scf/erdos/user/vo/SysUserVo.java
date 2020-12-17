package com.scf.erdos.user.vo;

import com.scf.erdos.common.model.SysRole;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description : 系统用户视图
 * @author：bao-clm
 * @date: 2020/3/25
 * @version：1.0
 */

@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserVo implements Serializable {

    // 主键
    private Long id;
    // 登录账号
    private String username;
    // 真实姓名
    private String realname;
    // 移动电话
    private String phone;
    //角色/岗位名
    private String roleName;

    private boolean enabled;
    // 注册时间
    private String createTime;
}
