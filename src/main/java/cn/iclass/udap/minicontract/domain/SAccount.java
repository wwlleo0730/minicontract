package cn.iclass.udap.minicontract.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "saccount")
public class SAccount extends IdEntity {

	
	private String wxid; //微信ID
	private String loginName;
	private String nickname;
	private String mobile;
	private String avatar;

}
