package cn.nongph.components.codec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Deprecated
public class MD5Codec {
	/**
	 * 
	 * @param src
	 *            :要进行加密的字符串
	 * @return 字符串的md5值
	 */
	public static String encrypt(String src) {
		char hexChar[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		// md5加密算法的加密对象为字符数组，这里是为了得到加密的对象
		byte[] b = src.getBytes();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(b);
			byte[] b2 = md.digest();// 进行加密并返回字符数组
			char str[] = new char[b2.length << 1];
			int len = 0;
			// 将字符数组转换成十六进制串，形成最终的密文
			for (int i = 0; i < b2.length; i++) {
				byte val = b2[i];
				str[len++] = hexChar[(val >>> 4) & 0xf];
				str[len++] = hexChar[val & 0xf];
			}
			return new String(str);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
