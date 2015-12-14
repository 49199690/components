package cn.nongph.components.util;

import java.math.BigDecimal;

/**
 * 地图数据计算工具类
 * @author leesun
 */
public class MapMathUtil {

	public static final Integer TURN_FORWARD = Integer.valueOf(1);
	public static final Integer TURN_LEFT_FORWARD = Integer.valueOf(2);
	public static final Integer TURN_RIGHT_FORWARD = Integer.valueOf(3);
	public static final Integer TURN_LEFT = Integer.valueOf(4);
	public static final Integer TURN_RIGHT = Integer.valueOf(5);
	public static final Integer TURN_LEFT_BACK = Integer.valueOf(6);
	public static final Integer TURN_RIGHT_BACK = Integer.valueOf(7);
	public static final Integer TURN_BACK = Integer.valueOf(8);

	public static final String TURN_FORWARD_STR = "直行";
	public static final String TURN_LEFT_FORWARD_STR = "稍向左转";
	public static final String TURN_RIGHT_FORWARD_STR = "稍向右转";
	public static final String TURN_LEFT_STR = "左转";
	public static final String TURN_RIGHT_STR = "右转";
	public static final String TURN_LEFT_BACK_STR = "转向左后方";
	public static final String TURN_RIGHT_BACK_STR = "稍向右后方";
	public static final String TURN_BACK_STR = "掉头";
	public static final String TURN_EAST = "往东";
	public static final String TURN_NORTH_EAST = "往东北方向";
	public static final String TURN_NORTH = "往北";
	public static final String TURN_NORTH_WEST = "往西北方向";
	public static final String TURN_WEST = "往西";
	public static final String TURN_SOUTH_WEST = "往西南方向";
	public static final String TURN_SOUTH = "往南";
	public static final String TURN_SOUTH_EAST = "往东南方向";

	/**
	 * 加密经纬度
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static String encode(double x, double y) {
		String _x = encode(x);
		String _y = encode(y);
		return _x + _y;
	}

	/**
	 * 加密经纬度字符串，形如：x1,y1;x2,y2;
	 * 
	 * @param line
	 * @return
	 */
	public static String encode(String line) {
		String[] _points = null;
		String _result = "";
		BigDecimal _lat_temp = new BigDecimal(0.0);
		BigDecimal _lng_temp = new BigDecimal(0.0);
		_points = line.split(";");
		for (String _point : _points) {
			String[] _latlng = _point.split(",");
			BigDecimal _lat = BigDecimal
					.valueOf(Double.parseDouble(_latlng[0]));
			BigDecimal _lng = BigDecimal
					.valueOf(Double.parseDouble(_latlng[1]));
			double _lat_disp = _lat.subtract(_lat_temp).doubleValue();
			double _lng_disp = _lng.subtract(_lng_temp).doubleValue();
			_result += encode(_lat_disp, _lng_disp);
			_lat_temp = _lat;
			_lng_temp = _lng;
		}
		return _result;
	}

	/**
	 * 解密经纬度
	 * 
	 * @param path
	 * @return
	 */
	public static String decode(String path) {
		int len = path.length();
		int index = 0;
		double lat = 0;
		double lng = 0;
		StringBuffer sb = new StringBuffer();

		while (index < len) {
			int b;
			int shift = 0;
			int result = 0;
			do {
				b = path.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			double dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = path.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			double dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			sb.append(";");
			sb.append(DataUtil.scaleDouble(lat * 1e-6, 6));
			sb.append(",");
			sb.append(DataUtil.scaleDouble(lng * 1e-6, 6));
		}
		return sb.substring(1);
	}

	/**
	 * 两点间的之间距离
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double linearDistanceOfTwoPoints(double x1, double y1,
			double x2, double y2) {
		double radLat1 = rad(y1);
		double radLat2 = rad(y2);
		double a = radLat1 - radLat2;
		double b = rad(x1) - rad(x2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378137.0;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	/**
	 * 根据一个点为中心，获取一个矩形范围
	 * 
	 * @param x
	 * @param y
	 * @param distance
	 * @return
	 */
	public static double[] getMaxMinPoint(double x, double y, double distance) {
		double[] dou = new double[4];
		dou[0] = x + distance / 103050;
		dou[1] = y + distance / 103050;
		dou[2] = x - distance / 103050;
		dou[3] = y - distance / 103050;
		return dou;
	}

	/**
	 * 根据两个点连线计算与标准坐标系之间的角度
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double getAngle(double x1, double y1, double x2, double y2) {
		double a = Math.pow(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2),
				0.5);
		double b = x1 - x2;

		double cosC = b / a;

		if (cosC < -1.0) {
			cosC = -1.0;
		} else if (cosC > 1.0) {
			cosC = 1.0;
		}
		double angle = Math.acos(cosC);
		angle = 180 * angle / Math.PI;
		// 定义 X 轴方向为0度，Y 轴方向为90度。
		if (y1 < y2) {
			// 第一二象限
			angle = 180 - angle;
		} else {
			// 第三四象限
			angle = angle + 180;
		}
		return (int) Math.round(angle);
	}

	/**
	 * 根据角度获取方向
	 * 
	 * @param angle
	 * @return
	 */
	public static String getTurnDirection(double angle) {
		if (angle >= 337.5 || angle < 22.5)
			return TURN_EAST;
		else if (angle >= 22.5 && angle < 67.5)
			return TURN_NORTH_EAST;
		else if (angle >= 67.5 && angle < 112.5)
			return TURN_NORTH;
		else if (angle >= 112.5 && angle < 157.5)
			return TURN_NORTH_WEST;
		else if (angle >= 157.5 && angle < 202.5)
			return TURN_WEST;
		else if (angle >= 202.5 && angle < 247.5)
			return TURN_SOUTH_WEST;
		else if (angle >= 247.5 && angle < 292.5)
			return TURN_SOUTH;
		else if (angle >= 292.5 && angle < 337.5)
			return TURN_SOUTH_EAST;
		else
			return null;
	}

	/**
	 * 根据两点经纬度获取方向
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static String getTurnDirection(double x1, double y1, double x2,
			double y2) {
		double angle = getAngle(x1, y1, x2, y2);
		return getTurnDirection(angle);
	}

	/**
	 * 加密单个double类型的数字
	 * 
	 * @param point
	 * @return
	 */
	private static String encode(double point) {
		// 取十进制乘以1e5
		int _point_int = (int) (point * 1e6);
		// 对二进制低位补0
		_point_int = _point_int << 1;
		// 如果原来的数是负数则求反
		if (point < 0) {
			_point_int = ~_point_int;
		}
		String resultString = "";
		while (_point_int >>> 5 > 0) {// 如果位块后面还有一个位块
			int _block = _point_int & 0x1F;// 将二进制数分为5位一组的块，倒序处理
			_block = (_block | 0x20) + 63;
			char _result = (char) _block;
			resultString += _result;
			_point_int = _point_int >>> 5;
		}
		resultString += (char) (_point_int + 63);
		return resultString;
	}

	/**
	 * 角度与数字之间的转化
	 * 
	 * @param d
	 * @return
	 */
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
}
