package cn.iclass.udap.minicontract.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.iclass.udap.minicontract.domain.SAccount;


public interface SAccountDao  extends JpaRepository<SAccount, Long>{
	
	/**
	 * 根据微信ID查找
	 * @param wxid
	 * @return
	 */
	SAccount findByWxid(String wxid);

}
