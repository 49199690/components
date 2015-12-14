/**
 * Creation Date:2015年12月8日-上午10:04:24
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package cn.nongph.components.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * 字符压缩<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2015年12月8日-上午10:04:24
 * @since 2015年12月8日-上午10:04:24
 */
public class CharSequenceCompress {

	/**
	 * Creates a new input stream with a default decompressor and buffer size.
	 * @param in the input stream
	 */
	public static byte[] uncompress(final byte[] src) throws IOException {
		byte[] result = src;
		byte[] uncompressData = new byte[src.length];
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(src);
		InflaterInputStream inflaterInputStream = new InflaterInputStream(byteArrayInputStream);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(src.length);

		try {
			while (true) {
				int len = inflaterInputStream.read(uncompressData, 0, uncompressData.length);
				if (len <= 0) {
					break;
				}
				byteArrayOutputStream.write(uncompressData, 0, len);
			}
			byteArrayOutputStream.flush();
			result = byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				byteArrayInputStream.close();
			} catch (IOException e) {
			}
			try {
				inflaterInputStream.close();
			} catch (IOException e) {
			}
			try {
				byteArrayOutputStream.close();
			} catch (IOException e) {
			}
		}

		return result;
	}

	public static byte[] compress(final byte[] src) throws IOException {
		return compress(src, Deflater.BEST_COMPRESSION);
	}

	/**
	 * Creates a new compressor using the specified compression level.
	 * Compressed data will be generated in ZLIB format.
	 * 
	 * @param level the compression level (0-9)
	 */
	public static byte[] compress(final byte[] src, final int level) throws IOException {
		byte[] result = src;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(src.length);
		Deflater deflater = new Deflater(level);
		DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater);
		try {
			deflaterOutputStream.write(src);
			deflaterOutputStream.finish();
			deflaterOutputStream.close();
			result = byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			deflater.end();
			throw e;
		} finally {
			try {
				byteArrayOutputStream.close();
			} catch (IOException e) {
			}

			deflater.end();
		}

		return result;
	}

	public static void main(String[] args) throws Exception {
		String data = "ABC阿斯顿建啊搜到回家啊搜if花生豆覅后山东规范化的施工方QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ阿打算将扩大看时间爱上发挥巨大是空间发挥到手机放回家都是李松";
		byte[] old = data.getBytes();
		System.out.println(old.length);

		byte[] news = compress(old);
		System.out.println(news.length);

		byte[] rt = uncompress(news);
		System.out.println(rt.length);

		String rtData = new String(rt);
		System.out.println(rtData.equals(data));
	}
}
