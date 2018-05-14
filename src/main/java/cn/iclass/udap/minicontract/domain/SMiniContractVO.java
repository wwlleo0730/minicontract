package cn.iclass.udap.minicontract.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于创建contract的vo对象
 * 
 * @author think
 *
 */
@Getter
@Setter
public class SMiniContractVO implements Serializable {

	private String wxid;

	private String picUrl;

	private String title;

	private String content;

	// 创建者手机
	private String creatormobile;

	// 创建者姓名
	private String creatorname;

	// 接收者手机
	private String receivermobile;

	// 接收者姓名
	private String receivername;

}
