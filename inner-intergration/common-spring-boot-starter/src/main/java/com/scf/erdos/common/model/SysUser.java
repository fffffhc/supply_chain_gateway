package com.scf.erdos.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description : 用户实体
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper=true)
public class SysUser  extends Model<SysUser>  implements Serializable {

	private static final long serialVersionUID = -5886012896705137070L;
	@TableId(value="id",type=IdType.ID_WORKER)  //雪花算法  id生成策略
	@JsonSerialize(using=ToStringSerializer.class)
	private Long id;
	private String username;
	private String password;
	private String realname;
	private String birthday;
	private String nickname;
	private String headImgUrl;
	private String idNum;
	private String phone;
	private Integer sex;
	private Boolean enabled;
	private String type;
	private String companyId;//企业id
	private String companyName;//企业名称
	private String companyAddr;//企业地址
	private String telephone;//办公电话
	private String creditNo;//统一社会信用代码
	private String post;//职位
	private String contacter;//联系人
	private String email;//邮箱
	@TableField(value="createTime")
	private Date createTime;
	@TableField(value="updateTime")
	private Date updateTime;
	private List<SysRole> roles;
	private String roleId;
	private String oldPassword;
	private String newPassword;
	private String caStatus;
	private String fddCustomerId;
}
