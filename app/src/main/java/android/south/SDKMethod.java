package android.south;


public class SDKMethod {

	private static final String TAG = "EepromDeviceNative";

	public SDKMethod() {

	}
	
	public int readE2(int addr,  byte[] buffer,  int length){
		int ret = 0;
		if((addr < 0 || length <0 || addr >127) && addr != 4096){
			return 0;
		}
		readE2PRom(addr, buffer, length);
		if(ret == 0){
			return 0;
		}
		return 1;
	}
	public int writeE2(int addr,  byte[] buffer,  int length){
		int ret = -1;
		if(addr < 0 || length <0 || addr >127){
			return 0;
		}
		if(buffer == null){
			return 0;
		}
		ret = writeE2PRom(addr, buffer, length);
		if(ret == 0){
			return 0;
		}
		return 1;
	}
	
	public int readNv(int addr, byte[] buffer, int length){
		int ret = 0;
		if(addr < 0 || length <0 || addr < 128){
			return 0;
		}
		ret = readE2PRom(addr, buffer, length);
		if(ret == 0){
			return 0;
		}
		return 1;
	}
	public int writeNv(int addr, byte[] buffer, int length){
		int ret = 0;
		if(addr < 0 || length <0 || addr < 128){
			return 0;
		}
		if(buffer == null){
			return 0;
		}
		ret = writeE2PRom(addr, buffer, length);
		if(ret == 0){
			return 0;
		}
		return 1;
	}
	public int setExtGps(int status){
		int ret = 0;
	    if(status < 0 || status > 1)
	    	return 0;
		ret = setExtGpsStatus(status);
		if(ret == 0){
			return 0;
		}
		return 1;
	}
	public int getExtGps(){
		int ret = 0;
		ret = getExtGpsStatus();
		 if(ret <0 || ret > 1)
		    return 0;
		return ret;
	}
	
	// JNI_eeprom
	private native int readE2PRom(int addr,  byte[] buf, int length);

	private native int writeE2PRom(int addr, byte[] buf, int length);
	
	//JNI_gpsnet
	
	private native int setExtGpsStatus(int status);
	
	private native int getExtGpsStatus();
	
	private native int enableNetwork(int status);

	private native int enableBt(int status);

	//private native int getBtStatus();
    
	static {
		System.loadLibrary("southsdk");
	}

}
