package cn.iclass.udap.minicontract;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import cn.iclass.udap.minicontract.domain.SAccount;
import cn.iclass.udap.minicontract.domain.SMiniContract;
import cn.iclass.udap.minicontract.repository.Constant;
import cn.iclass.udap.minicontract.repository.SAccountDao;
import cn.iclass.udap.minicontract.repository.SMiniContractDao;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SMiniContractTest {
	
	@Resource
	private SAccountDao accountDao;
	
	@Resource
	private SMiniContractDao scontractDao;
	
	@Test
	public void createMinicontract(){
		
		
//		SAccount a = new SAccount();
//		a.setAvatar("123");
//		a.setLoginName("a");
//		a.setMobile("11111111111");
//		a.setNickname("aaa");
//		a.setWxid("openid_aaa");
//		
//		this.accountDao.save(a);
//		
//		SAccount b = new SAccount();
//		b.setAvatar("123");
//		b.setLoginName("b");
//		b.setMobile("22222222222");
//		b.setNickname("bbb");
//		b.setWxid("openid_bbb");
//		
//		this.accountDao.save(b);
		
		SAccount creator = this.accountDao.getOne(1L);
		
		SMiniContract contract = new SMiniContract();
			
		contract.setCreator(creator);
		contract.setContent("hahahahahhaha");
		contract.setTitle("112333");
		contract.setStatus(Constant.CONTRACT_INIT);
		
		contract.setPhotoUrl("http://localhost/ppp.jpg");
		
		this.scontractDao.save(contract);
		
	}

}
