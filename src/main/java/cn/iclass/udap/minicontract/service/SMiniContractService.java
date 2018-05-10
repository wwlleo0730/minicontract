package cn.iclass.udap.minicontract.service;

import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import cn.iclass.udap.minicontract.domain.SAccount;
import cn.iclass.udap.minicontract.domain.SMiniContract;
import cn.iclass.udap.minicontract.repository.Constant;
import cn.iclass.udap.minicontract.repository.SAccountDao;
import cn.iclass.udap.minicontract.repository.SMiniContractDao;

@Service
public class SMiniContractService {
	
	private Logger logger =  LoggerFactory.getLogger(SMiniContractService.class);

	@Resource
	private SMiniContractDao sMiniContractRepository;

	@Resource
	private EthService ethService;

	@Resource
	private SAccountDao sAccountDao;

	public void doEthereumMini(SMiniContract contract) {
		
		String txHash = "";

		try {
			
			txHash = ethService.mint(contract.getCreator().getWxid(),
					contract.getReceiver().getWxid(),
					contract.getCreator().getMobile(), 
					contract.getReceiver().getMobile(), 
					contract.getTitle(),contract.getContent(), 
					"", contract.getPhotoUrl());
		
		} catch (ExecutionException e) {
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
		
		contract.setEthReceipt(txHash); //保存hash
		
		this.sMiniContractRepository.save(contract);

	}

	/**
	 * 确认是否需要写入区块链
	 * 
	 * @param smini
	 * @return
	 */
	public boolean checkDoEthereum(SMiniContract smini) {
		return smini.isIscreatorsign() && smini.isIsreceiversign();
	}

	/**
	 * 合同签字
	 */
	public SMiniContract signContract(long contractId, String wxid) {

		SMiniContract smini = this.sMiniContractRepository.findOne(contractId);
		
		String creator_wxid = "";
		String receiver_wxid = "";
		
		if(null != smini.getCreator()){
			creator_wxid = smini.getCreator().getWxid();
		}
		
		if(null != smini.getReceiver()){
			receiver_wxid = smini.getReceiver().getWxid();
		}
	
		
		// 判断当前签字的身份
		if (creator_wxid.equals(wxid)) {
			// 创建者签名
			smini.setIscreatorsign(true);
			smini.setCreatorsigntime(System.currentTimeMillis());
			smini.setStatus(Constant.CONTRACT_FINISH);
			this.sMiniContractRepository.save(smini);
		}

		if (receiver_wxid.equals(wxid)) {
			// 被邀请者签名
			smini.setIsreceiversign(true);
			smini.setReceiversigntime(System.currentTimeMillis());
			smini.setStatus(Constant.CONTRACT_FINISH);
			this.sMiniContractRepository.save(smini);
		}
		
		if (checkDoEthereum(smini)) {
			
			logger.info("start writing data to Ethereum，the contrract id -->"+smini.getId());
			this.doEthereumMini(smini);
		}
		
		return smini;
	}

	/**
	 * 创建合同
	 * 
	 * @param wxid
	 * @param content
	 * @param picurl
	 * @return
	 */
	public SMiniContract createContract(String wxid, String title, String content, String picurl) {

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
	 * @param contract
	 *            vo
	 * 
	 *            暂时控制只允许修改内容和图片地址
	 * 
	 */
	public SMiniContract updateContract(long id, SMiniContract contract) {

		SMiniContract smini = this.sMiniContractRepository.findOne(id);

		smini.setTitle(contract.getTitle());
		smini.setContent(contract.getContent());
		smini.setPhotoUrl(contract.getPhotoUrl());

		smini.setCreatorsigntime(null);
		smini.setReceiversigntime(null);
		smini.setIscreatorsign(false);
		smini.setIsreceiversign(false);

		smini.setLastModifyTime(System.currentTimeMillis());

		this.sMiniContractRepository.save(smini);
		
		return smini;
	}
	
	/**
	 * 账号合同绑定
	 * @param id 合同ID
	 * @param wxid openid
	 * @return
	 */
	public SMiniContract bindContractAndAccount(long id , String wxid){
		
		SMiniContract smini = this.sMiniContractRepository.findOne(id);
		
		SAccount receiver = this.sAccountDao.findByWxid(wxid);
		
		smini.setReceiver(receiver);
		
		this.sMiniContractRepository.save(smini);
		
		return smini;
	}

}
