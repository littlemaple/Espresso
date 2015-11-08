package com.medzone.mcloud.base.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.util.Log;

import com.medzone.mcloud.utils.TimeUtils;

@Deprecated
public class BaseMeasureDataUtil {

	// -----------------------------public------------------------------

	/**
	 * According to the current time, and create new MEASURE_UID six random
	 * number.
	 * 
	 * @return measureUID
	 */
	public static String createUID() {

		Random random = new Random();
		int rand = random.nextInt(899999);
		rand = rand + 100000;
		long milliSecond = System.currentTimeMillis();
		String datestring = TimeUtils.getTime(milliSecond, TimeUtils.YYYYMMDD_HHMMSS);
		return String.format(Locale.getDefault(), "%s%d", datestring, rand);
	}

	/**
	 * 
	 * 
	 * @param uid
	 *            measureUID
	 * @return analytic measurement time ,which unit is millisecond .
	 */
	public static long parseUID2Millisecond(String uid) {
		long l = TimeUtils.getMillisecondDate(parseUID2DateString(uid));
		if (l < 0) {
			TimeUtils.getMillisecondDate(parseUID2DateString(uid));
			Log.e("robert", "parse error:" + l + "------orginial:" + uid);
		}
		return l;
	}

	/**
	 * 
	 * 
	 * @param uid
	 *            measureUID
	 * @return analytic measurement time , EG: "201409261860050505";
	 */
	public static String parseUID2DateString(String uid) {
		return uid.substring(0, 14);
	}

	/**
	 * 
	 * @return System.currentTimeMillis() / 1000;
	 */
	public static Long getCurrentSeconds() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 
	 * <p>
	 * newScale scale of the result returned. (1)
	 * <p>
	 * roundingMode rounding mode to be used to round the result.(ROUND_HALF_UP)
	 * 
	 * @param value
	 * @return BigDecimal
	 */
	public static BigDecimal convertAccuracy(Float value) {

		final String tmp;
		if (value == null) {
			tmp = new String("0");
		}
		else {
			tmp = String.valueOf(value);
		}
		return new BigDecimal(tmp).setScale(1, BigDecimal.ROUND_HALF_UP);

	}

	/**
	 * 
	 * @param value
	 * @param newScale
	 * @param roundingMode
	 *            BigDecimal.ROUND_HALF_UP
	 * @return
	 */
	protected static BigDecimal convertAccuracy(Float value, int newScale, int roundingMode) {

		final String tmp;
		if (value == null) {
			tmp = new String("0");
		}
		else {
			tmp = String.valueOf(value);
		}
		return new BigDecimal(tmp).setScale(newScale, roundingMode);

	}

	// -----------------------------BP------------------------------

	public static float convertmmHg2Kpa(float mmHg) {

		double ret = mmHg / 0.75 / 10;

		String str = String.valueOf(ret);

		BigDecimal mData = new BigDecimal(str).setScale(1, BigDecimal.ROUND_HALF_UP);

		return mData.floatValue();
	}

	public static float convertKpa2mmHg(float kpa) {
		return (float) (7.5 * kpa);
	}

	// -----------------------------OXYL------------------------------

	/** 将血氧长期数据转化为List */
	public static List<Integer> convertOXYLByteArr2List(byte[] bytes) {

		if (bytes != null && bytes.length > 0) {

			List<Integer> oxygenLongList = new LinkedList<Integer>();
			String oxyl = new String(bytes);
			char[] oxylChar = oxyl.toCharArray();
			String ret = "oxyl--->血氧长期:";
			for (int i = 0; i < oxylChar.length; i++) {
				int oxygen = oxylChar[i];
				oxygenLongList.add(oxygen);
				ret += oxygen;
				ret += ",";
			}
			return oxygenLongList;
		}
		return null;
	}

	/** 将血氧List转化为byte数组 */
	public static byte[] convertOXYLList2ByteArr(List<Integer> list) {
		if (list != null && list.size() > 0) {
			char[] data = new char[list.size()];
			int i;
			for (i = 0; i < list.size(); i++) {
				data[i] = (char) list.get(i).intValue();
			}
			return new String(data).getBytes();
		}
		return null;
	}

	/** 将心率长期数据转化为List */
	public static List<Integer> convertRATELByteArr2List(byte[] bytes) {
		if (bytes != null && bytes.length > 0) {

			List<Integer> rateLongList = new LinkedList<Integer>();
			String ratel = new String(bytes);
			char[] ratelChar = ratel.toCharArray();
			String ret = "oxyl--->心率长期:";
			for (int i = 0; i < ratelChar.length; i += 2) {
				int position = i;
				if (position > ratelChar.length - 2) break;

				int rate = ratelChar[position] * 256 + ratelChar[position + 1];
				rateLongList.add(rate);
				ret += rate;
				ret += ",";
			}
			return rateLongList;
		}
		return null;
	}

	/**
	 * 将心率List转化为byte数组
	 * */
	public static byte[] convertRATELList2ByteArr(List<Integer> list) {
		if (list != null && list.size() > 0) {
			char[] data = new char[list.size() * 2];
			int i;
			for (i = 0; i < list.size(); i++) {
				int position = i * 2;
				int value = list.get(i).intValue();
				if (value > 256) {
					data[position] = 1;
					data[position + 1] = (char) (value - 256);
				}
				else {
					data[position] = 0;
					data[position + 1] = (char) value;
				}
			}
			return new String(data).getBytes();
		}
		return null;
	}

	/** 获取平均值 */
	public static double getAverage(List<Integer> list) {
		if (list == null || list.size() == 0) return 0;
		double total = 0;
		int size = list.size();
		for (Integer i : list) {
			if (i == null || i.intValue() <= 0) {
				size--;
				continue;
			}
			total += i;
		}
		if (size <= 0) {
			return 0;
		}
		return total / size;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] int2ByteArrWithLittleEndian(int value) {
		byte[] bytes = new byte[4];

		bytes[3] = (byte) (value >> 24 & 0xff);

		bytes[2] = (byte) (value >> 16 & 0xff);

		bytes[1] = (byte) (value >> 8 & 0xff);

		bytes[0] = (byte) (value & 0xff);
		return bytes;
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static int byteArr2IntWithLittleEndian(byte[] bytes) {
		int value = bytes[0] & 0xff | (bytes[1] & 0xff << 8) | (bytes[2] & 0xff << 16) | (bytes[3] & 0xff << 24);
		return value;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] int2ByteArrWithBigEndian(int value) {
		byte[] bytes = new byte[4];

		bytes[0] = (byte) (value >> 24 & 0xff);

		bytes[1] = (byte) (value >> 16 & 0xff);

		bytes[2] = (byte) (value >> 8 & 0xff);

		bytes[3] = (byte) (value & 0xff);
		return bytes;
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static int byteArr2IntWithBigEndian(byte[] bytes) {
		int value = ((bytes[0] & 0xff) << 24) | ((bytes[1] & 0xff) << 16) | ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
		return value;
	}

	// ---------------end------------------------//

	public static byte[] convertIntegerList2ByteArray(List<Integer> list) {
		if (list == null) return null;
		byte[] bytes = new byte[list.size() * 4];
		for (int i = 0; i < list.size(); i++) {
			byte[] tmp = int2ByteArrWithBigEndian(list.get(i) == null ? 0 : list.get(i));
			bytes[i * 4] = tmp[0];
			bytes[i * 4 + 1] = tmp[1];
			bytes[i * 4 + 2] = tmp[2];
			bytes[i * 4 + 3] = tmp[3];
		}
		return bytes;

	}

	public static List<Integer> convertBytes2IntegerList(byte[] bytes) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < bytes.length; i += 4) {
			byte[] tmp = new byte[4];
			tmp[0] = bytes[i];
			tmp[1] = bytes[i + 1];
			tmp[2] = bytes[i + 2];
			tmp[3] = bytes[i + 3];
			list.add(byteArr2IntWithBigEndian(tmp));
		}
		return list;
	}

}
