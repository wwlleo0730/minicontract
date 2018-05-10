package cn.iclass.udap.minicontract.wechat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.OauthApi;
import com.foxinmy.weixin4j.mp.model.OauthToken;
import com.foxinmy.weixin4j.mp.model.User;

@RestController
public class WechatUserController {
	
	@GetMapping("/wechat/user/{code}")
	public User getUserByCode(@PathVariable String code){

		OauthApi oauthApi =  new OauthApi();
		WeixinProxy weixinProxy = new WeixinProxy();
			
		try {
			OauthToken token = 
					oauthApi.getAuthorizationToken(code);
			
			String openId = token.getOpenId();
			
			User user = weixinProxy.getUser(openId);
			
			return user;
			
		} catch (WeixinException e) {
			e.printStackTrace();
		}
		
		
		return new User();
		
		
	}

}
