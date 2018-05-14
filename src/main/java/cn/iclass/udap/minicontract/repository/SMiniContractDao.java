package cn.iclass.udap.minicontract.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.iclass.udap.minicontract.domain.SMiniContract;

@Repository
public interface SMiniContractDao extends JpaRepository<SMiniContract, Long>{
	
	List<SMiniContract> findByReceiverId(long id);
	
	List<SMiniContract> findByCreatorId(long id);
	
	List<SMiniContract> findByReceiverWxid(String wxid);
	
	List<SMiniContract> findByCreatorWxid(String wxid);
	
	@Query("from SMiniContract t where (t.creator.id = ?1  or t.receiver.id = ?1)"
			+ " order by t.createTime desc")
	List<SMiniContract> findByCreatorIdOrReceiverId(long id);
	
	@Query("from SMiniContract t where (t.creator.id = ?1  or t.receiver.id = ?1) and t.title like ?2"
			+ " order by t.createTime desc")
	List<SMiniContract> findByCreatorIdOrReceiverIdAndKeyWord(long id , String keyword);

}
