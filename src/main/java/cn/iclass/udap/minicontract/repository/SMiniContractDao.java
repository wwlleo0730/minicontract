package cn.iclass.udap.minicontract.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.iclass.udap.minicontract.domain.SMiniContract;

@Repository
public interface SMiniContractDao extends JpaRepository<SMiniContract, Long>{
	
	List<SMiniContract> findByReceiverId(long id);
	
	List<SMiniContract> findByCreatorId(long id);
	
	List<SMiniContract> findByReceiverWxid(String wxid);
	
	List<SMiniContract> findByCreatorWxid(String wxid);
	
	

}
