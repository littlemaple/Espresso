package com.medzone.mcloud.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import android.util.Base64;

public class MD5Util {

	/**
	 * Ĭ�ϵ������ַ�����ϣ��������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�,apacheУ�����ص��ļ�����ȷ���õľ���Ĭ�ϵ�������
	 */
	protected static char			hexDigits[]		= { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	public static final String		key				= "3d1f215e34b8f562c507115bd4494e47";

	protected static MessageDigest	messagedigest	= null;
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException nsaex) {
			System.err.println(MD5Util.class.getName() + "Failed to initialize,NoSuchAlgorithmException indicates that a requested algorithm could not be found.");
			nsaex.printStackTrace();
		}
	}

	/**
	 * �����ַ�����md5У��ֵ
	 * 
	 * @param s
	 * @return
	 */
	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	/**
	 * �ж��ַ�����md5У�����Ƿ���һ����֪��md5����ƥ��
	 * 
	 * @param password
	 *            ҪУ����ַ���
	 * @param md5PwdStr
	 *            ��֪��md5У����
	 * @return
	 */
	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}

	/**
	 * �����ļ���md5У��ֵ
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String(File file) throws IOException {
		InputStream fis;
		fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0) {
			messagedigest.update(buffer, 0, numRead);
		}
		fis.close();
		return bufferToHex(messagedigest.digest());
	}

	/**
	 * JDK1.4�в�֧����MappedByteBuffer����Ϊ����update��������������������Ҫ����MappedByteBuffer��
	 * ԭ���ǵ�ʹ�� FileChannel.map ����ʱ��MappedByteBuffer �Ѿ���ϵͳ��ռ����һ������� ��ʹ��
	 * FileChannel.close �������޷��ͷ��������ģ���FileChannel��û���ṩ���� unmap �ķ�����
	 * ��˻�����޷�ɾ���ļ��������
	 * 
	 * ���Ƽ�ʹ��
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String getFileMD5String_old(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messagedigest.update(byteBuffer);
		return bufferToHex(messagedigest.digest());
	}

	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];// ȡ�ֽ��и� 4 λ������ת��, >>>
		// Ϊ�߼����ƣ�������λһ������,�˴�δ�������ַ����кβ�ͬ
		char c1 = hexDigits[bt & 0xf];// ȡ�ֽ��е� 4 λ������ת��
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static String encrypt(String txt) {

		String xRandom = Integer.toString(new Random().nextInt(999999));
		messagedigest.update(xRandom.getBytes());
		byte[] xkey = messagedigest.digest();// ����
		int xkeylen = xkey.length;

		String tmp = "";
		for (int i = 0; i < txt.length(); i++) {
			int j = i % xkeylen;

			char first = (char) xkey[j];
			int ix = txt.charAt(i);
			int jy = xkey[j];

			char second = (char) (ix ^ jy);
			tmp = tmp + first + second;
		}

		String paramTxt = getMD5String(txt) + tmp;

		int paramLength = paramTxt.length();
		int keyLength = key.length();

		byte[] ret = new byte[paramLength];

		for (int i = 0; i < paramLength; i++) {
			int j = i % keyLength;
			int ix = paramTxt.charAt(i);
			int jy = key.charAt(j);
			ret[i] = (byte) (ix ^ jy);
		}

		String result = Base64.encodeToString(ret, Base64.DEFAULT);
		return result;
	}
}
