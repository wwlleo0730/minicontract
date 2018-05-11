package cn.iclass.udap.minicontract.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于创建contract的vo对象
 * @author think
 *
 */
@Getter
@Setter
public class SMiniContractVO implements Serializable{

	
	private String wxid;
	
	private String picUrl;
	
	private String title;
	
	private String content;
	
	
}
