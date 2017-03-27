//package com.spdata.factory;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.serialport.DeviceControl;
//import android.serialport.SerialPort;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//import common.utils.DataConversionUtils;
//
////import com.example.serialporthelper.DataConversionUtils;
////import com.example.serialporthelper.MainActivity.ReadThread;
////import com.example.serialporthelper.MainActivity.ReadThread;
//public class GpsOpen extends Activity {
//
//	private Context mContext;
//	private Button close;
//	public Button sendButton,sendButton1;
//	private Button clear;
//	private Button clear1;
//	private Button set;
//	private Timer timer1;
//	public String sendstring = "0200640d000000030001000204000600008103";
//	public String sendstring2 ="0200646600000003000100000D706F7765725F75700F0B1D0A1C07080600030000000000070826000300000000000708120003000000000007080D00030000000000070808000300000000000708280003000000000007080C0003000000000007082900000000000000E603";
//	private SerialPort mSerialPort;
//	static final String TAG = "SerialPort";
//	byte[] cmd = new byte[3];
//	private int fd;
//
//
////	private Handler handler = null;
////	private byte[] temp1 = new byte[1024];
////	private byte[] temp2 = new byte[1024];
////	public byte[] sendcmd2={0x02, 0x00, 0x64, 0x66, 0x00, 0x00, 0x00, 0x03, 0x00, 0x01, 0x00, 0x00, 0x0D, 0x70, 0x6F, 0x77, 0x65, 0x72, 0x5F, 0x75, 0x70, 0x0F, 0x0B, 0x1D, 0x0A, 0x1C, 0x07, 0x08, 0x06, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x07, 0x08, 0x26, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x07, 0x08, 0x12, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x07, 0x08, 0x0D, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x07, 0x08, 0x08, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x07, 0x08, 0x28, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x07, 0x08, 0x0C, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x07, 0x08, 0x29, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xE6, 0x03};
//	private TextView mTextView1;
//	private EditText edvreadInfor=null;
//
//	private ReadThread mReadThread;
//	public static Handler handler = new Handler();
//
//	// ReadThread t = new ReadThread();
//	private Spinner spinProvince=null;
//	private boolean debugtest = true;
//
//	DeviceControl DevCtrl;
//
//
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.gps);
//	    DeviceControl gpio=new DeviceControl("/sys/class/misc/mtgpio/pin");
//		gpio.PowerOnDevice63();
//		gpio.PowerOnDevice64();
//		mSerialPort = new SerialPort();
//		spinProvince=(Spinner)super.findViewById(R.id.province);
//		edvreadInfor = (EditText) findViewById(R.id.edv_infor);
//		sendButton = (Button) this.findViewById(R.id.button1);
//		sendButton1= (Button) this.findViewById(R.id.button2);
////		spinProvince.setOnItemSelectedListener(new ProvOnItemSelectedListener());
//		sendButton1.setOnClickListener(new ClickEvent());
//
//		TimerTask task = new SynchroTimerTask();
//	    Timer timer = new Timer();
//	    timer.schedule(task,31000);
//
//		handler = new Handler() {
//		@Override
//			public void handleMessage(Message msg) {
//
//				super.handleMessage(msg);
//				byte[] temp2 = (byte[]) msg.obj;
//		//
//		//			edvreadInfor.append("read null");
////		//			Log.e("read", "");
//		//			return;
//
//		//		}
//				if(temp2==null){
//					Log.e("read", "--------!!!!");
//
//					return;
//				}
//				try{
//		//		edvreadInfor.append("\n");
//				edvreadInfor.append(DataConversionUtils.byteArrayToAscii(temp2));
//			    Log.e("read", "!!!!");
//				}catch (Exception e) {
//        		}
//
//
//		}
//	  };
//
//	}
///*
//    private Handler handler = new Handler()
//    {
//		public void handleMessage(Message msg)
//		{
//			super.handleMessage(msg);
//			temp2 = (byte[]) msg.obj;
//			Log.e(TAG, "handle-----------------------------------"+temp2);
//
//	//		String accept_show = bytesToHexString(temp2);
//			EditText.append(DataConversionUtils.byteArrayToAscii(temp2));
//		    EditText.append("\n");
//		//    sendButton.setOnClickListener(new ClickEvent());
//	//		EditText.append(accept_show + "\n");
//	//		mTextViewS.append(msg.obj.toString());
//			return;
//		}
//    };
//   */
//    private class  ProvOnItemSelectedListener implements OnItemSelectedListener{
//
//    	      public void onItemSelected(AdapterView<?> adapter,View view,int position,long id) {
//    		            //获取选择的项的值
//                String sInfo=adapter.getItemAtPosition(position).toString();
//                if(sInfo.equals("38400")){
//                	Log.e(TAG, "1-----------------------------------");
//                	mSerialPort.CloseSerial(fd);
//                	try {
//            			Thread.sleep(5);
//            		} catch (InterruptedException e) {
//            		}
//                	fd=mSerialPort.OpenSerial("/dev/ttyMT1",38400);
//                	try {
//            			Thread.sleep(5);
//            		} catch (InterruptedException e) {
//            		}
//
//
//                }
//                if(sInfo.equals("57600")){
//                	Log.e(TAG, "2-----------------------------------");
//                	mSerialPort.CloseSerial(fd);
//                	try {
//            			Thread.sleep(5);
//            		} catch (InterruptedException e) {
//            		}
//                	fd=mSerialPort.OpenSerial("/dev/ttyMT1",57600);
//                	try {
//            			Thread.sleep(5);
//            		} catch (InterruptedException e) {
//            		}
//
//
//                }
//                if(sInfo.equals("9600")){
//                	Log.e(TAG, "3-----------------------------------");
//                	mSerialPort.CloseSerial(fd);
//                	try {
//            			Thread.sleep(5);
//            		} catch (InterruptedException e) {
//            		}
//                	fd=mSerialPort.OpenSerial("/dev/ttyMT1",9600);
//                	try {
//            			Thread.sleep(5);
//            		} catch (InterruptedException e) {
//            		}
//
//                }
//
//    		   }
//    		   public void onNothingSelected(AdapterView<?> arg0) {
//    		            String sInfo="什么也没选！";
//
//
//    		   }
//    }
//
//	public class SynchroTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//        	try{
//        		fd=mSerialPort.OpenSerial("/dev/ttyMT1",38400);
//        		Thread.sleep(5);
//				mSerialPort.WriteSerialByte(fd,HexString2Bytes(sendstring));
//				Thread.sleep(10);
//				Log.e(TAG, "step1-2-----------------------------------");
//				mSerialPort.CloseSerial(fd);
//				Thread.sleep(5);
//				fd=mSerialPort.OpenSerial("/dev/ttyMT1",57600);
//        		Thread.sleep(5);
//				mSerialPort.WriteSerialByte(fd,HexString2Bytes(sendstring2));
//				Thread.sleep(10);
//
//				spinProvince.setOnItemSelectedListener(new ProvOnItemSelectedListener());
//				sendButton.setOnClickListener(new ClickEvent());
//
//				final Timer timer1 = new Timer();
//				TimerTask task = new TimerTask(){
//					public void run() {
//				          try{
//				        	  mSerialPort.WriteSerialByte(fd,HexString2Bytes(sendstring2));
//
//							  Thread.sleep(5);
//				        	  byte[] temp1 = mSerialPort.ReadSerial(fd, 1024);
//				        	  int i=0;
//				        	  i++;
////							String accept_show = bytesToHexString(temp1);
////							mTextView1.append(accept_show + "\n");
//						    Log.e(TAG, "read1------------qq---------------------");
//
//							if(handler == null){
//								handler = new Handler();
//							}
//								Message msg = new Message();
//								if(temp1==null ||i>10){
//									timer1.cancel();
//									Log.e(TAG, "read------------qqqqq---------------------");
//									return;
//								}
//								msg.obj = temp1;
//								handler.sendMessage(msg);
//								Log.e(TAG, "read--------------qq------------------------");
//
//				        	}catch (Exception e) {
//					     		// TODO Auto-generated catch block
//						    	e.printStackTrace();
//				    	}
//
//				         }
//				};
//			    timer1.schedule(task,0,2000);
//				long a = 123;
//					String b = a.toSring();
//	//		EditText.append("\n");
//	//		Log.e(TAG, "read-----------q---------------------------");
//	//    	EditText.append(DataConversionUtils.byteArrayToAscii(temp1));
//
//	//    	Log.e(TAG, "read------------w--------------------------");
//	//		mReadThread = new ReadThread();
//	//		mReadThread.start();
//        	   }catch (Exception e) {
//			     		// TODO Auto-generated catch block
//				    	e.printStackTrace();
//            	}
//  //      	spinProvince.setOnItemSelectedListener(new ProvOnItemSelectedListener());
////			sendButton.setOnClickListener(new ClickEvent());
//        	}
//  }
///*
//	public class newTimerTask extends TimerTask {
//        @Override
//        public void run() {
//          try{
//        	  mSerialPort.WriteSerialByte(fd,HexString2Bytes(sendstring2));
//
//			  Thread.sleep(5);
//        	  byte[] temp1 = mSerialPort.ReadSerial(fd, 1024);
//
////			String accept_show = bytesToHexString(temp1);
////			mTextView1.append(accept_show + "\n");
//		    Log.e(TAG, "read1------------qq---------------------");
//
//			if(handler == null){
//				handler = new Handler();
//			}
//				Message msg = new Message();
//				if(temp1==null){
//					Log.e(TAG, "read------------qqqqq---------------------");
//					timer1.cancel();
//					return;
//				}
//				msg.obj = temp1;
//				handler.sendMessage(msg);
//				Log.e(TAG, "read--------------qq------------------------");
//
//        	}catch (Exception e) {
//	     		// TODO Auto-generated catch block
//		    	e.printStackTrace();
//    	}
//
//         }
//        }
//	*/
//	class ClickEvent implements View.OnClickListener {
//		public void onClick(View v) {
//				if(v==sendButton){
//			//		spinProvince.setOnItemSelectedListener(new ProvOnItemSelectedListener());
//
//					mReadThread = new ReadThread();
//					mReadThread.start();
//
//				}
//				if(v==sendButton1){
//		//			spinProvince.setOnItemSelectedListener(new ProvOnItemSelectedListener());
//					fd=mSerialPort.OpenSerial("/dev/ttyMT1",57600);
//					Log.e(TAG, "read--------------------------------------");
//					x2ReadThread mthread= new x2ReadThread();
//					mthread.start();
//
//				}
//		}
//	}
//	private class x2ReadThread extends Thread {
//		@Override
//		public void run() {
//			super.run();
//
//				try {
//					Thread.sleep(400);
//					byte[] temp1 = mSerialPort.ReadSerial(fd, 1024);
//					Thread.sleep(10);
//				//	edvreadInfor.append(DataConversionUtils.byteArrayToAscii(temp1));
//					Log.e(TAG, "readxx3xx--------------------------------------");
//					if(handler == null){
//						handler = new Handler();
//					}
//						Message msg = new Message();
//						if(temp1==null){
//							Log.e(TAG, "read------------qqqqq---------------------");
//							return;
//						}
//						msg.obj = temp1;
//						handler.sendMessage(msg);
//				    edvreadInfor.append("\n");
//				//	Thread.sleep(5);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//
//				}
//		}
//	}
//	private class ReadThread extends Thread {
//		@Override
//		public void run() {
//			super.run();
//
//				try {
//					Log.e(TAG, "readxx1--------------------------------------");
//					mSerialPort.WriteSerialByte(fd,HexString2Bytes(sendstring2));
//					Log.e(TAG, "readxx2--------------------------------------");
//					Thread.sleep(5);
//					byte[] temp1 = mSerialPort.ReadSerial(fd, 1024);
//					Thread.sleep(10);
//					edvreadInfor.append(DataConversionUtils.byteArrayToAscii(temp1));
//					Log.e(TAG, "readxx3--------------------------------------");
//				    edvreadInfor.append("\n");
//				//	Thread.sleep(5);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//
//				}
//		}
//	}
//	public byte uniteBytes(byte src0, byte src1) {
//		try {
//			byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
//					.byteValue();
//			_b0 = (byte) (_b0 << 4);
//			byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
//					.byteValue();
//			byte ret = (byte) (_b0 ^ _b1);
//			return ret;
//		} catch (Exception e) {
//			// TODO: handle exception
//
//			return 0;
//		}
//
//	}
//	public byte[] HexString2Bytes(String src) {
//		byte[] ret = new byte[src.length() / 2];
//		byte[] tmp = src.getBytes();
//		for (int i = 0; i < src.length() / 2; i++) {
//			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
//		}
//		return ret;
//	}
//	public static String bytesToHexString(byte[] src, int length) {
//		StringBuilder stringBuilder = new StringBuilder(" ");
//		if (src == null || length <= 0) {
//			return null;
//		}
//		for (int i = 0; i < length; i++) {
//			int v = src[i] >= 0 ? src[i] : src[i] + 256;
//			String hv = Integer.toHexString(v);
//			if (hv.length() < 2) {
//				stringBuilder.append(0);
//			}
//			stringBuilder.append(hv + " ");
//		}
//		return stringBuilder.toString();
//	}
//	public static String bytesToHexString(byte[] src) {
//		StringBuilder stringBuilder = new StringBuilder(" ");
//		if (src == null || src.length <= 0) {
//			return null;
//		}
//		for (int i = 0; i < src.length; i++) {
//			int v = src[i] >= 0 ? src[i] : src[i] + 256;
//			String hv = Integer.toHexString(v);
//			if (hv.length() < 2) {
//				stringBuilder.append(0);
//			}
//			stringBuilder.append(hv + " ");
//		}
//		return stringBuilder.toString();
//	}
//
//	public static String toHexString(String s) {
//		String str = "";
//		for (int i = 0; i < s.length(); i++) {
//			int ch = (int) s.charAt(i);
//			String s4 = Integer.toHexString(ch) + " ";
//			str = str + s4;
//		}
//		return str;
//	}
//
//	public static String toStringHex(String s) {
//		byte[] baKeyword = new byte[s.length() / 2];
//
//		for (int i = 0; i < baKeyword.length; i++) {
//			try {
//				baKeyword[i] = (byte) (0xff & Integer.parseInt(
//						s.substring(i * 2, i * 2 + 2), 16));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		try {
//			s = new String(baKeyword, "utf-8");// UTF-16le:Not
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		return s;
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//
//		super.onResume();
//
////			mReadThread = new ReadThread();
////			mReadThread.start();
//
//	}
//
//	public void onDestroy() {
//
//		mSerialPort.CloseSerial(fd);
//		finish();
//		super.onDestroy();
//	}
//
//
//
//}
