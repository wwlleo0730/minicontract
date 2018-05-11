package cn.iclass.udap.minicontract.wechat;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

public class DesUtil {

	private final static String DES_ALGORITHM = "DES";
	private final static String KEY = "mWPZMSOM9JVUHUQw4YQXgreObmk52soYlaTiReESAvW";

	public static String encryption(String plainData) throws Exception {

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(DES_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, generateKey(KEY));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {

		}

		try {
			// 不能把加密后的字节数组直接转换成字符串
			byte[] buf = cipher.doFinal(plainData.getBytes());
			return base64UrlEncode(buf);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new Exception("IllegalBlockSizeException", e);
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw new Exception("BadPaddingException", e);
		}

	}

	/**
	 * DES解密
	 * 
	 * @param secretData
	 * @param secretKey
	 * @return
	 * @throws Exception
	 */
	public static String decryption(String secretData) throws Exception {

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(DES_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, generateKey(KEY));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("NoSuchAlgorithmException", e);
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new Exception("NoSuchPaddingException", e);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new Exception("InvalidKeyException", e);

		}

		try {
			
			byte[] buf = cipher.doFinal(base64UrlDecode(secretData));
			return new String(buf);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new Exception("IllegalBlockSizeException", e);
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw new Exception("BadPaddingException", e);
		}
	}

	/**
	 * 获得秘密密钥
	 * 
	 * @param secretKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static SecretKey generateKey(String secretKey)
			throws NoSuchAlgorithmException {
		
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(secretKey.getBytes());
		// 为我们选择的DES算法生成一个KeyGenerator对象
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(DES_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
		}
		kg.init(secureRandom);
		// kg.init(56, secureRandom);

		// 生成密钥
		return kg.generateKey();
	}

	public static String base64UrlEncode(byte[] simple) {
		String s = new String(Base64.encodeBase64(simple)); // Regular base64
															// encoder
		s = s.split("=")[0]; // Remove any trailing '='s
		s = s.replace('+', '-'); // 62nd char of encoding
		s = s.replace('/', '_'); // 63rd char of encoding
		return s;
	}

	public static byte[] base64UrlDecode(String cipher) {
		String s = cipher;
		s = s.replace('-', '+'); // 62nd char of encoding
		s = s.replace('_', '/'); // 63rd char of encoding
		switch (s.length() % 4) { // Pad with trailing '='s
		case 0:
			break; // No pad chars in this case
		case 2:
			s += "==";
			break; // Two pad chars
		case 3:
			s += "=";
			break; // One pad char
		default:
			System.err.println("Illegal base64url String!");
		}
		return Base64.decodeBase64(s); // Standard base64 decoder
	}
	
// 测试
//	public static void main(String args[]) {
//		String input = "140408002";
//
//		String result;
//		try {
//			result = encryption(input);
//			System.out.println(result);
//			System.out.println(decryption(result));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

}
