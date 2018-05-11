package cn.iclass.udap.minicontract.wechat;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.model.User;

@Controller
public class OauthController {
	
	private Logger logger = LoggerFactory.getLogger(OauthController.class);
	
	/**
	 * 确认是否有cookies存在，如果存在则直接返回用户对象
	 * 
	 * @param request
	 * @return
	 */
	private User checkCookie(HttpServletRequest request) {
		
		
		return null;

//		String tokenValue = "";
//
//		if (null != request.getCookies()) {
//
//			Cookie[] cookies = request.getCookies();
//
//			for (Cookie cookie : cookies) {
//				if ("wechat-token".equals(cookie.getName()))
//					tokenValue = cookie.getValue();
//			}
//
//			User user = null;
//
//			if (null != tokenValue && tokenValue.length() > 0) {
//
//				try {
//					String userInfo = DesUtil.decryption(tokenValue);
//					JSONObject obj = JSON.parseObject(userInfo);
//					Object attrs = obj.remove("extattr");
//					user = JSON.toJavaObject(obj, User.class);
//					
//					if (attrs != null) {
//						user.setExtattr(JSON.parseArray(attrs.toString(), NameValue.class));
//					}
//				} catch (Exception e) {
//					logger.error(e.getMessage());
//				}
//
//			}
//
//			return user;
//
//		} else
//			return null;
	}

}
