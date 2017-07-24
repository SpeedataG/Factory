package common.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.SystemClock;
import android.serialport.SerialPortBackup;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import common.psam.DeviceControl;

public class CardOperation {

	// 00A00BA0004040000003454E45524759
	//aa bb 15 00 00 00 23 06 00 a4 04 00 0b a0 00 00 00 03 45 4e 45 52 47 59 51
	//aa bb 15 00 00 00 11 06 00 a4 04 00 0b a0 00 00 00 03 45 4e 45 52 47 59 51
	byte[] ic_cmd_change_dir = {0x00, (byte) 0xA4, 0x04, 0x00, 0x0b,
			(byte) 0xA0, 0x00, 0x00, 0x00, 0x03, 0x45, 0x4e, 0x45, 0x52, 0x47,
			0x59};
	byte[] psam_cmd_get_commonal = {0x00, (byte) 0xb0, (byte) 0x95, 0x00, 0x0a};// app
	// id
	byte[] cmd_get_card_flog = {0x00, (byte) 0xb0, (byte) 0x96, 0x00, 0x01};// card_flog
	byte[] cmd_get_pwd_usemode = {0x00, (byte) 0xb0, (byte) 0x96, 0x04, 0x01};//
	byte[] ic_cmd_get_user_id = {0x00, (byte) 0xb0, (byte) 0x95, 0x0a, 0x0a};
	byte[] cmd_get_pay_infortion = {0x00, (byte) 0xb0, (byte) 0x9c, 0x00, 0x0c};// 支付信息
	byte[] psam_cmd_get_df = {0x00, (byte) 0xb0, (byte) 0x97, 0x00, 0x15};
	byte[] psam_cmd_devId = {0x00, (byte) 0xb0, (byte) 0x96, 0x00, 0x06};
	byte[] psam_cmd_commonality_data = {0x00, (byte) 0xb0, (byte) 0x95, 0x00,
			0x0d};
	byte[] psam_cmd_df_commonality_data = {0x00, (byte) 0xb0, (byte) 0x97,
			0x00, 0x0d};
	// 00 A4 04 00 0E 31 50 41 59 2E 53 59 53 2E 44 44 46 30 31
	// byte[] psam_cmd_change_dir = { 0x00, (byte) 0xA4, 0x04, 0x00, 0x0E, 0x31,
	// 0x50,
	// 0x41, 0x59, 0x2E, 0x53, 0x59, 0x53, 0x2E, 0x44, 0x44, 0x46, 0x30,
	// 0x31 };
	// 00A4040010D15600000145442F4550000000000000
	byte[] psam_cmd_change_dir = {0x00, (byte) 0xA4, 0x04, 0x00, 0x10,
			(byte) 0xD1, 0x56, 0x00, 0x00, 0x01, 0x45, 0x44, 0x2F, 0x45, 0x50,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
	boolean isHasUser = false;
	byte[] temp_read = new byte[64];
	int readcount = 0;
	byte[] get_respond = new byte[5];
	/**
	 * 消费流程
	 *
	 * @param amount
	 */
	byte[] psamID = new byte[6];
	private MyLogger logger = MyLogger.kLog();
	//    public static String CardSlot_PSAM = "/dev/psam_slot0";
//    public static String CardSlot_User = "/dev/psam_slot1";
	private byte key = 0x02;
	private byte[] pin = {0x00, 0x20, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00};
	private byte[] blance = {(byte) 0x80, 0x5c, 0x00, 0x01, 0x04};
	// private byte[] PsamId=null;0x00, 0x20, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00
	// 用户卡读出10byteID 截取八位
	// private byte[] cardID = new byte[8];
	// 文件
//    private File devFilePSAM = null;
//    private File devFileUser = null;
	private RandomAccessFile devPSAM = null;
	private RandomAccessFile devUser = null;
	private Context mContext;
	private byte[] temp1;
	private byte[] bytes;

	private byte typeSendReceIC = 0x23;
	private byte typePowerIC = 0x21;
	private byte typeSendRecePSAM = 0x13;
	private byte typePowerPsam = 0x11;

	private byte[] cmd_ic_get9d_data = {0x00, (byte) 0xB0, (byte) 0x9D, 0x00, 0x1E};
	private SerialPortBackup mSerialPort;//串口读写关闭功能
	private int fd;//文件句柄
	private int baurate = 115200;
	private String serialport = "ttyMT3";
	private DeviceControl mDeviceControl;//gpio 操作对象
	// private int carFlog = -1;
	private byte[] add_count_limit;// 加油次数限制
	private byte[] add_volum_limit;// 加油量限制
	private byte[] add_amount_limit;// 加油金额限制
	private byte[] ic_cmd_read_pay_infor = {0x00, (byte) 0xB0, (byte) 0x9C,
			0x00, (byte) 0x40};

	public CardOperation(Context mContext) {
		super();
		this.mContext = mContext;
		get_respond[0] = 0x00;
		get_respond[1] = (byte) 0xc0;
		get_respond[2] = 0x00;
		get_respond[3] = 0x00;
		initDevice();

	}

	public static byte[] hexStringToByte(String hex) {
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
	 * 跳转到DF文件下 adf
	 */
	public boolean sendIcChangeDir() {//改
		byte[] readData = writeAndRead(Cmd.adpuPackage(ic_cmd_change_dir, typeSendReceIC),
				"sendIcChangeDir");
		if (readData == null)
			return false;
		else return true;
	}



	/**
	 * 初始化串口 gpio
	 */
	private boolean initDevice() {//改加
		mSerialPort = new SerialPortBackup();
		try {
//            logger.d("====serialport" + serialport + "==baurate" + baurate + "==gpio" + gpio);
			mSerialPort.OpenSerial("/dev/" + serialport, baurate);
			fd = mSerialPort.getFd();
			logger.d("--onCreate--open-serial=" + fd);
		} catch (SecurityException e) {
			e.printStackTrace();
//            System.exit(0);//终止timer
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mDeviceControl = new DeviceControl("sys/class/misc/mtgpio/pin", "96");
			mDeviceControl.PowerOnDevice();
			mDeviceControl.PsamResetDevice(99);
			final ProgressDialog progressDialog=new ProgressDialog(mContext);
			progressDialog.setTitle("Reset PSAM");
			progressDialog.setMessage("Reset……");
			progressDialog.setCancelable(false);
			progressDialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					SystemClock.sleep(3000);
					progressDialog.cancel();
				}
			}).start();
		} catch (IOException e){
			e.printStackTrace();
			mDeviceControl = null;
			return false;
		}
		return true;
	}

	/**
	 * psam 软件上电
	 *
	 * @return
	 */
	public byte[] activePSAM() {//改
		byte[] powerCmd = Cmd.getPowerCmd(typePowerPsam);
		return writeAndRead(powerCmd, "psam power ");
	}

	public byte[] activeIC() {//改
		byte[] powerCmd = Cmd.getPowerCmd(typePowerIC);
		return writeAndRead(powerCmd, "IC power");
	}

	/**
	 * @param cmd 写指令
	 * @param key 输出log
	 * @return 读到的数据
	 */
	private byte[] writeAndRead(byte[] cmd, String key) {
		int writeCount = mSerialPort.WriteSerialByte(fd, cmd);
		if (writeCount < 0) {
			logger.e(key + " writeCount=" + writeCount);
			return null;
		}
		SystemClock.sleep(100);
		//read serialport
		try {
			byte[] read = mSerialPort.ReadSerial(fd, 1024);
			int readCount = 0;
			showSendData(cmd, key);
			while (read == null && readCount <= 10) {
				readCount++;
				logger.e(key+" readcount=" + readCount);
				read = mSerialPort.ReadSerial(fd, 1024);
			}
			if (read == null) {
				logger.e(key + " read null");
			} else {
				showReceivedData(read, key, read.length);
			}
			byte[] unPackageData = Cmd.unPackage(read);
			if (unPackageData == null) {
				logger.e(key + " read null");
			} else {
				showReceivedData(unPackageData, key + "_unPackage==", unPackageData.length);
			}
//            showReceivedData(unPackageData, key + "_unPackage==", unPackageData.length);
//            showReceivedData(unPackageData, "unPackage " + key, unPackageData.length);
			return unPackageData;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void closePASM() {
		if (devPSAM != null) {
			try {
				logger.d("close psam");
				devPSAM.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			devPSAM = null;
		}
	}

	public void closeUser() {
		if (devUser != null) {
			try {
				logger.d("close user");
				devUser.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			devUser = null;
		}
	}

	/**
	 * @return 0 不需要秘钥验证   1需要秘钥
	 */
	public int isNeedPin() {
		sendIcChangeDir();
		int result = -1;
		byte[] temp_read = writeAndRead(Cmd.adpuPackage(cmd_get_pwd_usemode, typeSendReceIC),
				"isNeedPin");
		if (temp_read != null) {
			if (temp_read[0] == 0x00) {
				result = 0;
			} else
				result = 1;
		}
		logger.d("isNeedPin  " + result);
		return result;
	}



	private boolean isReturnIcCardBean(byte[] data) {
		if (data == null) {
			return true;
		}
		return false;
	}





	private byte[] getRandom() throws IOException {
		byte[] cmd_ic_random = {0x00, (byte) 0x84, 0x00, 0x00, 0x04};
		byte[] temp_read = writeAndRead(Cmd.adpuPackage(cmd_ic_random, typeSendReceIC),
				"cmd_ic_random");
		if (temp_read == null || temp_read.length < 8)
			return null;
		byte[] random = new byte[8];
		System.arraycopy(temp_read, 0, random, 0, 8);
		// showReceivedData(temp_read, "cmd_ic_random", readcount);
		// byte[] random=new byte[];
		return random;
	}


	private void showErrorData(byte[] temp_read, String key, int len) {
		byte[] temp = new byte[temp_read.length];
		System.arraycopy(temp_read, 0, temp, 0, temp_read.length);
		logger.e(key + " error:" + getBytetoString(temp, len));
	}

	private void showReceivedData(byte[] temp_read, String key, int len) {
		byte[] temp = new byte[temp_read.length];
		System.arraycopy(temp_read, 0, temp, 0, temp_read.length);
		logger.d(key + "_rece:" + getBytetoString(temp, len));
	}

	private void showSendData(byte[] temp_read, String key) {
		byte[] temp = new byte[temp_read.length];
		System.arraycopy(temp_read, 0, temp, 0, temp_read.length);
		logger.d(key + " send:" + getBytetoString(temp, temp_read.length));
	}

	private String getBytetoString(byte[] res, int len) {
		String result = "";
		for (int i = 0; i < len; i++) {
			result += String.format("%02x ", res[i]);
		}
		return result;
	}

	/**
	 * int转换为4个byte
	 *
	 * @param data
	 * @return
	 */
	private byte[] intToByteArray(int data) {
		byte[] result = new byte[4];
		result[0] = (byte) ((data >> 24) & 0xFF);
		result[1] = (byte) ((data >> 16) & 0xFF);
		result[2] = (byte) ((data >> 8) & 0xFF);
		result[3] = (byte) (data & 0xFF);
		return result;
	}

	/**
	 * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
	 * 0xD9}
	 *
	 * @param src String
	 * @return byte[]
	 */
	public byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < src.length() / 2; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/**
	 * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	 *
	 * @param src0 byte
	 * @param src1 byte
	 * @return byte
	 */
	public byte uniteBytes(byte src0, byte src1) {
		try {
			byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
					.byteValue();
			_b0 = (byte) (_b0 << 4);
			byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
					.byteValue();
			byte ret = (byte) (_b0 ^ _b1);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

}
