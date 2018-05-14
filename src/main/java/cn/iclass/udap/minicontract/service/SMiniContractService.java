package cn.iclass.udap.minicontract.service;

import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.iclass.udap.minicontract.core.ServiceException;
import cn.iclass.udap.minicontract.domain.SAccount;
import cn.iclass.udap.minicontract.domain.SMiniContract;
import cn.iclass.udap.minicontract.domain.SMiniContractVO;
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
		
		String cMobile = "";
		String rMobile = "";
		
		if(null != contract.getCreatormobile()) cMobile = contract.getCreatormobile();
		
		if(null != contract.getReceivermobile()) rMobile = contract.getReceivermobile();

		try {
			
			txHash = ethService.mint(contract.getCreator().getWxid(),
					contract.getReceiver().getWxid(),
					cMobile, 
					rMobile,
					contract.getTitle(),contract.getContent(), 
					"", contract.getPhotoUrl());
		
		} catch (ExecutionException e) {
			logger.error(e.getMessage());
			throw new ServiceException(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		
		logger.info("success get txHash : " +txHash);
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
	public SMiniContract signContract(long contractId, String wxid) throws Exception{

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
	 * @param vo
	 * @return
	 */
	public SMiniContract createContract(SMiniContractVO vo) {

		SMiniContract contract = new SMiniContract();

		contract.setTitle(vo.getTitle());
		contract.setContent(vo.getContent());
		contract.setPhotoUrl(vo.getPicUrl());
		
		contract.setCreatorname(vo.getCreatorname());
		contract.setCreatormobile(vo.getCreatormobile());
		contract.setReceivername(vo.getReceivername());
		contract.setReceivermobile(vo.getReceivermobile());

		SAccount creator = this.sAccountDao.findByWxid(vo.getWxid());

		contract.setCreator(creator);

		this.sMiniContractRepository.save(contract);

		return contract;
	}

	/**
	 * 创建合同
	 * 
	 * @param wxid
	 * @param content
	 * @param picurl
	 * @return
	 */
	@Deprecated
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
		
		boolean reSign = false;
		
		if(null != contract.getTitle() && !contract.getTitle().equals(smini.getTitle())){
			smini.setTitle(contract.getTitle());
			reSign = true;
		}
				
		if( null != contract.getContent() &&  !contract.getContent().equals(smini.getContent())){
			smini.setContent(contract.getContent());
			reSign = true;
		}
		
		if( null != contract.getCreatormobile() && !contract.getCreatormobile().equals(smini.getCreatormobile())){
			smini.setCreatormobile(contract.getCreatormobile());
			reSign = true;
		}
		
		if( null != contract.getCreatorname() && !contract.getCreatorname().equals(smini.getCreatorname())){
			
			smini.setCreatorname(contract.getCreatorname());
			reSign = true;
		}
		
		if( null != contract.getReceivermobile() && !contract.getReceivermobile().equals(smini.getReceivermobile())){
			smini.setReceivermobile(contract.getReceivermobile());
			reSign = true;
		}
		
		if( null != contract.getReceivername() && !contract.getReceivername().equals(smini.getReceivername())){
			smini.setReceivername(contract.getReceivername());
			reSign = true;
		}
		
		
		if( null != contract.getPhotoUrl() && ! contract.getPhotoUrl().equals(smini.getPhotoUrl())){
			smini.setPhotoUrl(contract.getPhotoUrl());
			reSign = true;
		}
		
		
		if(reSign){
			
			smini.setCreatorsigntime(null);
			smini.setReceiversigntime(null);
			smini.setIscreatorsign(false);
			smini.setIsreceiversign(false);	
		}


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
