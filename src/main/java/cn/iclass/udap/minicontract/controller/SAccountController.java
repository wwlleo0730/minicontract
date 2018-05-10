package cn.iclass.udap.minicontract.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.iclass.udap.minicontract.domain.SAccount;
import cn.iclass.udap.minicontract.repository.SAccountDao;

@RestController
public class SAccountController {
	
	@Resource
	private SAccountDao accountDao;
	
	@GetMapping("/sAccounts")
    public List<SAccount> get() {
        return this.accountDao.findAll();
    }
	
	@GetMapping("/sAccounts/{wxid}")
    public SAccount getOneByWXid(@PathVariable(value = "wxid") String wxid) {
        return this.accountDao.findByWxid(wxid);
    }
	
	@PostMapping("/sAccounts")
	public SAccount saveSAccount(@RequestBody SAccount saccount) {
		this.accountDao.save(saccount);
		return saccount;
	}
	
	
}
