package common.utils;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	public static void ShowMessage(Context context, String message,
			boolean debug) {
		if (debug) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}

	public static void ShowMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static String getCurrentTime() {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(new Date());
	}

	public static String getCurrentTime(String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());
	}

	/**
	 * 
	 * byte数组转换成16进制字符串
	 * 
	 * @param src
	 * 
	 * @return
	 */

	public static String bytesToHexString(byte[] src) {

		StringBuilder stringBuilder = new StringBuilder();

		if (src == null || src.length <= 0) {

			return null;

		}

		for (int i = 0; i < src.length; i++) {

			int v = src[i] & 0xFF;

			String hv = Integer.toHexString(v);

			if (hv.length() < 2) {

				stringBuilder.append(0);

			}

			stringBuilder.append(hv);

		}

		return stringBuilder.toString();

	}

	public static String parseAscii(String data) {// ASCII转换为字符串

		// String s = "20 20 20 20 00 89";// ASCII码

		String[] chars = data.split(" ");
//		System.out.println("ASCII 汉字 \n----------------------");
		String result = "";
		for (int i = 0; i < chars.length; i++) {
			// System.out.println(chars[i] + " "
			// + (char) Integer.parseInt(chars[i]));
			result += (char) Integer.parseInt(chars[i]);
		}
		return result;
	}
	
	public static String getAscii(byte[] cmds) {
		int tRecvCount = cmds.length;
		StringBuffer tStringBuf = new StringBuffer();
		String nRcvString;
		char[] tChars = new char[tRecvCount];
		for (int i = 0; i < tRecvCount; i++) {
			tChars[i] = (char) cmds[i];
		}
		tStringBuf.append(tChars);
		nRcvString = tStringBuf.toString(); // nRcvString从tBytes转成了String类型的"123"
		return nRcvString;
	}
	
	/**
	 * byte[]转int
	 * 
	 * @param bytes
	 * @return
	 */
	public static int byteArrayToInt(byte[] bytes) {
		int value = 0;
		// 由高位到低位
		for (int i = 0; i < bytes.length; i++) {
			int shift = (bytes.length - 1 - i) * 8;
			value += (bytes[i] & 0x000000FF) << shift;// 往高位游
		}
		return value;
	}
	
	/**
	 * 
	 * hexString-byte[] "130632199104213021"->{0x13,0x06....,0x21}
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToByteArray(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}
	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
}
