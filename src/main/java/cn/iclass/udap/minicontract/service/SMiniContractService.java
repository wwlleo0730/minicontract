package cn.iclass.udap.minicontract.service;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.iclass.udap.minicontract.domain.SMiniContract;
import cn.iclass.udap.minicontract.repository.SMiniContractDao;

@Service
public class SMiniContractService {
	
	
	@Resource
	private SMiniContractDao sMiniContractRepository;
	
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
