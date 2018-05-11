package cn.iclass.udap.minicontract.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 合约
 * @author wuwl
 *
 */

@Setter
@Getter
@Entity
@Table(name = "smini_contract")
public class SMiniContract extends IdEntity{

	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 起草人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private SAccount creator;
	
	//创建者手机
	private String creatormobile;
	
	//创建者姓名
	private String creatorName;
	
	/**
	 * 起草人是否签字
	 */
	private boolean iscreatorsign = false;
	
	/**
	 * 起草人签字时间
	 */
	private Long creatorsigntime;
	
	/**
	 * 签约人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private SAccount receiver;
	
	//接收者手机
	private String receivermobile;
	
	//接收者姓名
	private String receiverName;
	
	/**
	 * 接收人是否签字
	 */
	private boolean isreceiversign = false;

	/**
	 * 接收人签字时间
	 */
	private Long receiversigntime;
	
	/**
	 * 当前合同状态、已签和未签
	 */
	private String status;
	
	/**
	 * 内容
	 */
	private String content;
	
    private String photoUrl;

    private String videoUrl;

    private String audioUrl;

    private long dbidex;

    private String ethReceipt;

}
