package cn.iclass.udap.minicontract.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.iclass.udap.minicontract.domain.SMiniContract;
import cn.iclass.udap.minicontract.repository.SMiniContractDao;


@RestController
public class SMiniContractController {
	
	@Resource
	private SMiniContractDao sMiniContractRepository;

	@GetMapping("/sMiniContracts")
	public List<SMiniContract> get() {
		return this.sMiniContractRepository.findAll();
	}

	@GetMapping("/sMiniContracts/{id}")
	public SMiniContract getOne(@PathVariable("id") long id) {
		return this.sMiniContractRepository.findOne(id);
	}
	
	@GetMapping("/sMiniContractsByCreator")
    public List<SMiniContract> sMiniContractsByCreator(
            @RequestParam String wxid ) {
        return this.sMiniContractRepository.findByCreatorWxid(wxid);
    }
	
	@GetMapping("/sMiniContractsByReceiver")
    public List<SMiniContract> sMiniContractsByReceiver(
            @RequestParam String wxid ) {
        return this.sMiniContractRepository.findByReceiverWxid(wxid);
    }
	
	

}
