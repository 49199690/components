package cn.nongph.components.hash;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5哈希算法<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2015年11月25日-下午1:18:12
 * @since 2015年11月25日-下午1:18:12
 */
public class MD5Hash {

	private ThreadLocal<MessageDigest> md5Holder = new ThreadLocal<MessageDigest>() {
		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException("no md5 algorythm found");
			}
		}
	};
	
	public long hash(String key) {
		return this.hash(key.getBytes(Charset.forName("UTF-8")));
	}
	
	public long hash(byte[] key) {
		MessageDigest md5 = md5Holder.get();
		md5.reset();
		md5.update(key);
		
		byte[] bKey = md5.digest();
		long res = 
				  ((long) (bKey[3] & 0xFF) << 24)
				| ((long) (bKey[2] & 0xFF) << 16)
				| ((long) (bKey[1] & 0xFF) << 8)
				| (long) (bKey[0] & 0xFF);
		
		return res;
	}

}
