package cn.iclass.udap.minicontract.wechat;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.OauthApi;
import com.foxinmy.weixin4j.mp.model.OauthToken;
import com.foxinmy.weixin4j.mp.model.User;

import cn.iclass.udap.minicontract.domain.SAccount;
import cn.iclass.udap.minicontract.repository.SAccountDao;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/oauth")
public class OauthController {
	
	private Logger logger = LoggerFactory.getLogger(OauthController.class);
	
	private static final String OAUTH_SERVER = "https://sooc.iclass.cn/miniapi/oauth";
	
	@Resource
	private SAccountDao accountDao;
	
	@ApiIgnore
	@ResponseBody
	@GetMapping(value = { "/test" })
	public String oauthTest() {
		return "ok";
	}
	
	@GetMapping(value = { "/oauth2token" })
	public String oauthRest(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {

		User user = checkCookie(request);

		if (user != null) {

			String redirectUrl = OAUTH_SERVER + "/usertoken";

			return "redirect:" + redirectUrl;

		} else {

			OauthApi oapi = new OauthApi();

			String redirectUrl = OAUTH_SERVER + "/authtoken";

			String redirect_Url = oapi.getUserAuthorizationURL(redirectUrl, "state", "snsapi_base");
			
			logger.info(redirect_Url);

			return "redirect:" + redirect_Url;
		}

	}
	
	@GetMapping(value = "/usertoken")
	@ResponseBody
	public User usertoken(HttpServletRequest request, HttpServletResponse response) {
		User user = checkCookie(request);
		return user;
	}

	@GetMapping(value = "/authtoken")
	@ResponseBody
	public User oauthToken(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "code") String code, @RequestParam(value = "state") String system,
			RedirectAttributes redirectAttributes) {

		User user = getUserByCode(code);

		if (user != null) {
			this.saveUserCookies(user, response);
		}

		return user;
	}
	
	@GetMapping(value = { "/token/dec/{tokenValue}" } )
	public User decOauthToken(@PathVariable("tokenValue") String tokenValue){
		
		User user = null;
		
		try {
			String userInfo = DesUtil.decryption(tokenValue);
			JSONObject obj = JSON.parseObject(userInfo);

			user = JSON.toJavaObject(obj, User.class);

		} catch (Exception e) {
			
			return new User();
			
		}
		
		return user;
	} 
	
	/**
	 * 从回调中的code内获得用户
	 * @param code
	 * @return
	 */
	@ApiIgnore
	public User getUserByCode(String code) {
		
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
		
			return new User();
		}
		
	}
	
	
	/**
	 * 保存cookies
	 * 
	 * @param user
	 * @param response
	 */
	@ApiIgnore
	private void saveUserCookies(User user, HttpServletResponse response) {
				
		String p = JSON.toJSON(user).toString();
		
		String token = "";
		
		try {
			token = DesUtil.encryption(p);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		Cookie cookie = new Cookie("wechat-token", token);
		cookie.setMaxAge(172800); // 保存两天
		cookie.setDomain("iclass.cn");
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	/**
	 * 确认是否有cookies存在，如果存在则直接返回用户对象
	 * 
	 * @param request
	 * @return
	 */
	@ApiIgnore
	private User checkCookie(HttpServletRequest request) {

		String tokenValue = "";

		if (null != request.getCookies()) {

			Cookie[] cookies = request.getCookies();

			for (Cookie cookie : cookies) {
				if ("wechat-token".equals(cookie.getName()))
					tokenValue = cookie.getValue();
			}

			User user = null;

			if (null != tokenValue && tokenValue.length() > 0) {

				try {
					String userInfo = DesUtil.decryption(tokenValue);
					JSONObject obj = JSON.parseObject(userInfo);
					user = JSON.toJavaObject(obj, User.class);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

			}

			return user;

		} else
			return null;
	}

}
