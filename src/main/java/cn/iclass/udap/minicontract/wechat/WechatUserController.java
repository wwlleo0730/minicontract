package cn.iclass.udap.minicontract.wechat;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.OauthApi;
import com.foxinmy.weixin4j.mp.model.OauthToken;
import com.foxinmy.weixin4j.mp.model.User;

import cn.iclass.udap.minicontract.domain.SAccount;
import cn.iclass.udap.minicontract.repository.SAccountDao;

@RestController
public class WechatUserController {
	
	private static final Logger logger = LoggerFactory.getLogger(WechatUserController.class);
	
	@Resource
	private SAccountDao accountDao;
	
	@GetMapping("/wechat/user/{code}")
	public User getUserByCode(@PathVariable String code){

		OauthApi oauthApi =  new OauthApi();
		
		WeixinProxy weixinProxy = new WeixinProxy();
			
		try {
			OauthToken token = 
					oauthApi.getAuthorizationToken(code);
			
			logger.info("token:-->" + token);
			
			String openId = token.getOpenId();
			
			logger.info("openId:-->" + openId);
			
			User user = weixinProxy.getUser(openId);
			
			//说明是新用户
			if(null == accountDao.findByWxid(openId)){
				
				SAccount account = new SAccount();
				
				account.setAvatar(user.getHeadimgurl());
				account.setLoginName(user.getNickName());
				account.setWxid(openId);
				
				this.accountDao.save(account);
				
			}
			
			return user;
			
		} catch (WeixinException e) {
			e.printStackTrace();
		}
		
		
		return new User();
	}

}
