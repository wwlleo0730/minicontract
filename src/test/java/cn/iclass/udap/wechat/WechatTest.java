package cn.iclass.udap.wechat;

import org.junit.Test;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.OauthApi;
import com.foxinmy.weixin4j.mp.model.OauthToken;
import com.foxinmy.weixin4j.mp.model.User;

public class WechatTest {
	
	@Test
	public void testGetInfo(){
		
		
		OauthApi oauthApi =  new OauthApi();
		
		WeixinProxy weixinProxy = new WeixinProxy();
		
		String code = "0012gdJe14ejOs0SQtKe1QmhJe12gdJF";
		
		try {
			OauthToken token = 
					oauthApi.getAuthorizationToken(code);
			
			
			System.out.println(token.toString());
			
			String openId = token.getOpenId();
			
			User user = weixinProxy.getUser(openId);
			
			System.out.println(user.toString());
			
		} catch (WeixinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	

}
