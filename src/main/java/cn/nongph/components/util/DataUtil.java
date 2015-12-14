package cn.nongph.components.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;

import cn.nongph.components.codec.Base64Codec;
import cn.nongph.components.similarity.JaroWrinklerDistance;
import cn.nongph.components.similarity.LevenshteinDistance;

/**
 * 数据工具类
 * 
 * @author lisong
 */
@SuppressWarnings("rawtypes")
public class DataUtil {

	public static Blob string2Blob(String str) {
		try {
			return new SerialBlob(str.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String blob2String(Blob blob) {
		try {
			if (blob == null)
				return "";
			return new String(blob.getBytes(1, (int) blob.length()), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Clob string2Clob(String str) {
		try {
			return new SerialClob(str.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String clob2String(Clob clob) {
		try {
			return clob.getSubString(1, (int) clob.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isEnglishLetter(char input) {
		return (input >= 'a' && input <= 'z') || (input >= 'A' && input <= 'Z');
	}

	public static boolean isArabicNumber(char input) {
		return input >= '0' && input <= '9';
	}

	public static boolean isArabicNumber(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!DataUtil.isArabicNumber(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isCJKCharacter(char input) {
		return Character.UnicodeBlock.of(input) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
	}

	public static Integer getInt(Object value, int defaultValue) {
		try {
			if (DataUtil.isNullOrEmpty(value))
				return defaultValue;
			return Integer.parseInt(String.valueOf(value));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Long getLong(Object value, long defaultValue) {
		try {
			if (DataUtil.isNullOrEmpty(value))
				return defaultValue;
			return Long.parseLong(String.valueOf(value));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Double getDouble(Object value, double defaultValue) {
		try {
			if (DataUtil.isNullOrEmpty(value))
				return defaultValue;
			return Double.parseDouble(String.valueOf(value));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Float getFloat(Object value, float defaultValue) {
		try {
			if (DataUtil.isNullOrEmpty(value))
				return defaultValue;
			return Float.parseFloat(String.valueOf(value));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Boolean getBoolean(Object value, boolean defaultValue) {
		try {
			if (DataUtil.isNullOrEmpty(value))
				return defaultValue;
			return Boolean.parseBoolean(String.valueOf(value));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String getString(Object value, String defaultValue) {
		if (value == null)
			return defaultValue;
		else
			return value.toString();
	}

	public static BigDecimal getBigDecimal(String str, String defaultValue) {
		if (DataUtil.isNullOrEmpty(str))
			return new BigDecimal(defaultValue);
		else
			return new BigDecimal(str);
	}

	public static double scaleDouble(double d, int scale) {
		BigDecimal val = new BigDecimal(d);
		return val.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double div(String dividend, String divisor, int scale) {
		BigDecimal a = new BigDecimal(Double.valueOf(dividend));
		BigDecimal b = new BigDecimal(Double.valueOf(divisor));
		return (a.divide(b, scale, BigDecimal.ROUND_HALF_UP).doubleValue());
	}

	/**
	 * @method isNullOrEmpty
	 * @description 判断对象是否为空
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		String str = obj.toString().trim();
		if (str.equals("") || str.equalsIgnoreCase("null"))
			return true;

		if (obj instanceof Collection) {
			Collection collection = (Collection) obj;
			return collection.isEmpty();
		} else if (obj instanceof Map) {
			Map map = (Map) obj;
			return map.isEmpty();
		} else if (obj instanceof Object[]) {
			Object[] array = (Object[]) obj;
			return (array.length == 0);
		}

		return false;
	}

	/**
	 * @method isNotNullOrEmpty
	 * @description 判断对象是否不为空
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNotNullOrEmpty(Object obj) {
		return !isNullOrEmpty(obj);
	}

	/**
	 * 将base64字符串转换成图片文件
	 * 
	 * @param base64String
	 * @return
	 * @throws IOException
	 */
	public static File base64String2ImageFile(String base64String,
			String imageFilePath) throws IOException {
		byte[] bytes = Base64Codec.decrypt(base64String);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		BufferedImage bi1 = ImageIO.read(bais);
		File file = new File(imageFilePath);// 可以是jpg,png,gif格式
		ImageIO.write(bi1, "png", file);// 不管输出什么格式图片，此处不需改动

		return file;
	}

	/**
	 * 将图片文件转换为base64字符串
	 * 
	 * @param imageFilePath
	 * @return
	 * @throws IOException
	 */
	public static String imageFile2Base64String(String imageFilePath)
			throws IOException {
		File file = new File(imageFilePath);
		BufferedImage bi = ImageIO.read(file);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, "png", baos);
		byte[] bytes = baos.toByteArray();

		return Base64Codec.encrypt(bytes).trim();
	}

	/**
	 * 读取流信息
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String read(InputStream inputStream) throws IOException {
		StringBuilder result = new StringBuilder();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (IOException e) {
			throw e;
		} finally {
			if (br != null) {
				br.close();
				br = null;
			}
		}
	}

	/**
	 * 获取对象所有属性名
	 */
	public static String[] getDeclaredFieldNames(Object o) {
		Field[] fields = getDeclaredFields(o);
		String[] fieldNames = new String[fields.length];

		for (int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i].getName();
		}
		return fieldNames;
	}

	/**
	 * 获取对象的所有方法名
	 */
	public static Method[] getDeclaredMethods(Object o) {
		try {
			return o.getClass().getDeclaredMethods();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取对象的所有属性名
	 */
	public static Field[] getDeclaredFields(Object o) {
		try {
			return o.getClass().getDeclaredFields();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据属性名获取属性值
	 */
	public static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据属性名设置属性值
	 */
	public static void setFieldValueByName(String fieldName, Object o,
			Object value) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String setter = "set" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(setter,
					new Class[] { value.getClass() });
			method.invoke(o, new Object[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 计算两个字符串的编辑距离相似度
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static double getLevenshteinSimilarity(String str1, String str2) {
		final double levenshteinDistance = (double) new LevenshteinDistance()
				.compare(str1, str2);
		final int maxLength = Math.max(str1.length(), str2.length());
		final double matchScore = 1 - levenshteinDistance / maxLength;
		return matchScore;
	}

	/**
	 * 计算两个字符串的JaroWrinkler相似度
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static double getJaroWrinklerSimilarity(String str1, String str2) {
		return new JaroWrinklerDistance().compare(str1, str2);
	}

	/**
	 * 获取当前应用classpath  （web应用除外，需要指定WEB-INF的目录）
	 * 
	 * @return
	 */
	public static String getClasspath() {
		return Thread.currentThread().getContextClassLoader().getResource("")
				.getPath();
	}

	/**
	 * 将异常堆栈转换为字符串
	 * 
	 * @param aThrowable
	 *            异常
	 * @return String
	 */
	public static String getStackTrace(Throwable throwable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		throwable.printStackTrace(printWriter);
		return result.toString();
	}

	/**
	 * 获取实体类的属性名-属性值的Map
	 * 
	 * @param entity
	 * @return
	 */
	public static Map<String, Object> getEntityFieldMap(Object entity) {
		if (DataUtil.isNullOrEmpty(entity)) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = DataUtil.getDeclaredFields(entity);

		for (Field field : fields) {
			String fieldName = field.getName();
			Object fieldValue = DataUtil.getFieldValueByName(fieldName, entity);
			if(fieldValue == null) {
				map.put(fieldName, "");
			} else {
				if(fieldValue instanceof Date) {
					map.put(fieldName, ((Date) fieldValue).getTime());
				} else if(fieldValue instanceof Clob) {
					map.put(fieldName, DataUtil.clob2String((Clob) fieldValue));
				} else if(fieldValue instanceof Blob) {
					map.put(fieldName, DataUtil.blob2String((Blob) fieldValue));
				} else if(fieldValue instanceof String) {
					map.put(fieldName, DataUtil.getString(fieldValue, ""));
				} else {
					map.put(fieldName, fieldValue);
				}
			}
		}

		return map;
	}
	
	/**
	 * 对象序列化
	 * @param destFilePath
	 * @param obj
	 * @throws IOException
	 */
	public static void serializeObject(String destFilePath, Object obj)
			throws IOException {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			File f = new File(destFilePath);

			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			if (!f.exists()) {
				f.createNewFile();
			}
			
            fos = new FileOutputStream(destFilePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
        	try {
				if(oos != null) {
					oos.close();
					oos = null;
				}
				if(fos != null) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
        }
	}
	
	/**
	 * 对象反序列化
	 * @param destFilePath
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object unSerializeObject(String destFilePath)
			throws IOException, ClassNotFoundException {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(destFilePath);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
        	e.printStackTrace();
        	throw e;
        } finally {
        	try {
				if(ois != null) {
					ois.close();
					ois = null;
				}
				if(fis != null) {
					fis.close();
					fis = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
        }
    }
	
	/**
	 * Returns the Class object (of type <code>Class&lt;T&gt;</code>) of the
	 * argument of type <code>T</code>.
	 * 
	 * @param <T>
	 *            The type of the argument
	 * @param t
	 *            the object to get it class
	 * @return <code>Class&lt;T&gt;</code>
	 */
	public static <T> Class<T> getClass(T t) {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) t.getClass();
		return clazz;
	}

	/**
	 * Converts the given <code>List&lt;T&gt;</code> to a an array of
	 * <code>T[]</code>.
	 * 
	 * @param c
	 *            the Class object of the items in the list
	 * @param list
	 *            the list to convert
	 */
	public static <T> T[] toArray(Class<T> c, List<T> list) {
		@SuppressWarnings("unchecked")
		T[] ta = (T[]) Array.newInstance(c, list.size());

		for (int i = 0; i < list.size(); i++)
			ta[i] = list.get(i);
		return ta;
	}

	/**
	 * Converts the given <code>List&lt;T&gt;</code> to a an array of
	 * <code>T[]</code>.
	 * 
	 * @param list
	 *            the list to convert
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the list is empty. Use {@link #toArray(Class, List)} if
	 *             the list may be empty.
	 */
	public static <T> T[] toArray(List<T> list) {
		return toArray(getClass(list.get(0)), list);
	}

	/**
	 * 字符串分割
	 * @param str
	 * @return
	 */
	public static String[] split(String str, String separator) {
		List<String> values = new ArrayList<String>();
		if (str == null)
			return new String[0];
		
		StringTokenizer tokenizer = new StringTokenizer(str, separator);
		values = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			values.add(tokenizer.nextToken());
		}
		return values.toArray(new String[values.size()]);
	}
	
	/**
	 * 字节单位换算
	 * @param bytes
	 * @return
	 */
	public static String bytesUnitConvert(long bytes) {
		if (bytes == 0)
			return "0 B";

		long radix = 1024;

		String[] sizes = { "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };

		int i = (int) (Math.floor(Math.log(bytes) / Math.log(radix)));

		double val = (bytes / Math.pow(radix, i));

		return val + " " + sizes[i];
	}
	
	public static void main(String[] args) {
		System.out.println(scaleDouble(3.141, 2));
		System.out.println(bytesUnitConvert(21445411));
	}
}
