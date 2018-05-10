package cn.iclass.udap.minicontract.service;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import cn.iclass.udap.minicontract.domain.SAccount;
import cn.iclass.udap.minicontract.domain.SMiniContract;
import cn.iclass.udap.minicontract.repository.SAccountDao;
import cn.iclass.udap.minicontract.repository.SMiniContractDao;

@Service
public class SMiniContractService {
	
	
	@Resource
	private SMiniContractDao sMiniContractRepository;
	
	@Resource
	private EthService ethService;
	
	@Resource
	private SAccountDao sAccountDao;
	
	public void doEthereumMini(SMiniContract contract){
		
		 
		
		//ethService.mint(A, B, Aphone, Bphone, tilte, content, sound, picture)
		
	}
	
	/**
	 * 确认是否需要写入区块链
	 * @param smini
	 * @return
	 */
	boolean checkDoEthereum(SMiniContract smini){
		
		return false;
	}
	
	
	/**
	 * 合同签字
	 */
	public void signContract(long contractId , String wxid){
		
		SMiniContract smini = 
				this.sMiniContractRepository.findOne(contractId);
		
		String creator_wxid = smini.getCreator().getWxid();
		
		String receiver_wxid = smini.getReceiver().getWxid();
		
		//判断当前签字的身份
		
		if(creator_wxid.equals(wxid)){
			//创建者签名
			
			if(checkDoEthereum(smini)){
				
				//this.doEthereum();
				return ;
				
			}
			
		}
		
		if(receiver_wxid.equals(wxid)){
			//被邀请者签名
			
			if(checkDoEthereum(smini)){
				
				//this.doEthereum();
				
				return ;
			}
			
		}
		
		//说明只有一个人签了字，不做处理
		
		
		
	}

	/**
	 * 创建合同
	 * @param wxid
	 * @param content
	 * @param picurl
	 * @return
	 */
	public SMiniContract createContract(String wxid,String title,String content,String picurl){
		
		SMiniContract contract = new SMiniContract();
		
		contract.setTitle(title);
		contract.setContent(content);
		contract.setPhotoUrl(picurl);
		
		SAccount creator = this.sAccountDao.findByWxid(wxid);
		
		contract.setCreator(creator);
		
		this.sMiniContractRepository.save(contract);
		
		return contract;
	}
	
	/**
	 * 
	 * @param id
	 * @param contract vo
	 * 
	 * 暂时控制只允许修改内容和图片地址
	 * 
	 */
	public void updateContract(long id , SMiniContract contract){
		
		SMiniContract smini = 
				this.sMiniContractRepository.findOne(id);
		
		smini.setContent(contract.getContent());
		smini.setPhotoUrl(contract.getPhotoUrl());
		
		smini.setCreatorsigntime(null);
		smini.setReceiversigntime(null);
		smini.setIscreatorsign(false);
		smini.setIsreceiversign(false);
		
		smini.setLastModifyTime(System.currentTimeMillis());
		
		
		this.sMiniContractRepository.save(smini);
	}

}
