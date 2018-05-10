package cn.iclass.udap.minicontract.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.iclass.udap.minicontract.domain.SAccount;
import cn.iclass.udap.minicontract.domain.SMiniContract;
import cn.iclass.udap.minicontract.domain.SMiniContractVO;
import cn.iclass.udap.minicontract.repository.SAccountDao;
import cn.iclass.udap.minicontract.repository.SMiniContractDao;
import cn.iclass.udap.minicontract.service.SMiniContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
public class SMiniContractController {
	
	@Resource
	private SMiniContractDao sMiniContractRepository;
	
	@Resource
	private SMiniContractService sminiContractService;

	
	@ApiOperation(value = "所有合同", notes = "获得所有合同方法，暂时不分页", httpMethod = "GET")
	@GetMapping("/sMiniContracts")
	public List<SMiniContract> get() {
		return this.sMiniContractRepository.findAll();
	}

	@ApiOperation(value = "根据ID获得合同信息", notes = "根据ID获得合同信息", httpMethod = "GET")
	@GetMapping("/sMiniContracts/{id}")
	public SMiniContract getOne(@PathVariable("id") long id) {
		return this.sMiniContractRepository.findOne(id);
	}
	
	@ApiOperation(value = "作为发起方，获得合同列表", notes = "作为发起方，获得合同列表", httpMethod = "GET")
	@GetMapping("/sMiniContractsByCreator")
    public List<SMiniContract> sMiniContractsByCreator(
            @RequestParam String wxid ) {
        return this.sMiniContractRepository.findByCreatorWxid(wxid);
    }
	
	@ApiOperation(value = "作为接收方，获得合同列表", notes = "作为接收方，获得合同列表", httpMethod = "GET")
	@GetMapping("/sMiniContractsByReceiver")
    public List<SMiniContract> sMiniContractsByReceiver(
            @RequestParam String wxid ) {
        return this.sMiniContractRepository.findByReceiverWxid(wxid);
    }
	
	
	@ApiOperation(value = "创建合同", notes = "创建合同", httpMethod = "POST")
	@PostMapping("/sMiniContracts")
	public SMiniContract createContract(@RequestBody SMiniContractVO contractVO){
		
		return this.sminiContractService.createContract(contractVO.getWxid() , contractVO.getTitle(),
				contractVO.getContent() , contractVO.getPicUrl());
	}
	
	@ApiOperation(value = "合同用户绑定", notes = "接收者接收到一份分享的合同时，可以快速绑定的方法，id为合同id，wxid为获得的openid"
			, httpMethod = "POST")
	@PostMapping("/sMiniContracts/{id}/{wxid}")
	public SMiniContract bindAccount(@PathVariable long id ,@PathVariable String wxid){
		return this.sminiContractService.bindContractAndAccount(id, wxid);
	}
	
	@ApiOperation(value = "修改合同，并重置双方签名信息", notes = "修改合同内容方法，暂时只允许修改标题，内容和图片地址"
			, httpMethod = "POST")
	@PostMapping("/sMiniContracts/{id}")
	public SMiniContract updateContract(@PathVariable long id , 
			@RequestBody SMiniContract contract){
		//更新合同
		return sminiContractService.updateContract(id,contract);	
	}
	
	@ApiOperation(value = "签订合同方法", notes = "签订合同方法，调用方只需要传合同id和wxid，后台自动判断是谁签署"
			, httpMethod = "POST")
	@PostMapping("/sign/{id}/{wxid}")
	public boolean signContract(@PathVariable long id , @PathVariable String wxid){
		sminiContractService.signContract(id, wxid);
		return true;
	}
	
	
	@ApiOperation(value = "删除合同", notes = "删除合同方法"
			, httpMethod = "DELETE")
	@DeleteMapping("/sMiniContracts/{id}")
	public boolean deleteContract(@PathVariable long id){
		this.sMiniContractRepository.delete(id);
		return true;
	}

}
