package cn.iclass.udap.minicontract.wechat;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.token.TokenManager;
import com.foxinmy.weixin4j.type.TicketType;

@Controller
public class OauthJsController {
	
	private static final Logger logger = LoggerFactory.getLogger(OauthJsController.class);
	
	@Resource
	private WeixinProxy weixinProxy;

	private static final String URL_PARA = "url";
	
	@ResponseBody
	@RequestMapping(value = { "/oauth2js" })
	public Object indexJump(HttpServletRequest request) {
				
		String surl = request.getParameter(URL_PARA);
		
		//解码
		String url;
		try {
			url = URLDecoder.decode(surl ,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			return new HashMap<String, String>().put("code", "error");
		}
		
		logger.info("receive url to js oauth->"+url);
		
		
		Map<String, String> ret = null;
		
		TokenManager tokenManager = 
				weixinProxy.getTicketManager(TicketType.jsapi);

		String jsapi_ticket = "";

		try {
			jsapi_ticket = tokenManager.getAccessToken();
			ret = sign(jsapi_ticket, url);
			ret.put("appId", "wx1d508b1b8fdbc79c");
		} catch (WeixinException e) {
			logger.error(e.getMessage());
			return new HashMap<String, String>().put("code", "error");
		}
		
		return ret;
	}
	
	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
				+ "&timestamp=" + timestamp + "&url=" + url;
		
		logger.info(string1);
		
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		logger.info("success create nonceStr"+ret.get("nonceStr"));
		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	

}
