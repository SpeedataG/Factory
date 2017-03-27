package android.serialport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DeviceControl {
	private BufferedWriter CtrlFile;

	public DeviceControl(String path) {
		File DeviceName = new File(path);
		try {
			CtrlFile = new BufferedWriter(new FileWriter(DeviceName, false));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // open
			// file
	}

	public void PowerOnDevice(String gpio) // poweron barcode device
	{
		try {
			CtrlFile.write("-wdout"+gpio+" 1");
			CtrlFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void PowerOffDevice(String gpio) // poweroff barcode device
	{
		try {
			CtrlFile.write("-wdout"+gpio+" 0");
			CtrlFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void PowerOnw(String gpio) // poweron barcode device
	{
		try {
			CtrlFile.write(gpio+"on");
			CtrlFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void PowerOffw(String gpio) // poweroff barcode device
	{
		try {
			CtrlFile.write(gpio+"off");
			CtrlFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	//----------------------gps--------------------------------
	public void PowerOnDevice63() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout63 1");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOnDevice64() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout64 1");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOnDevice70() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout70 1");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOnDevice71() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout71 1");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOnDevice72() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout72 1");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOnDevice73() // poweroff gps device
	{
		try {
			CtrlFile.write("-wdout73 1");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOnDevice87() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout87 1");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOnDevice131() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout131 1");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOnDevice121() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout121 1");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOffDevice121() // poweroff gps device
	{
		try {
			CtrlFile.write("-wdout121 0");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOffDevice71() // poweroff gps device
	{
		try {
			CtrlFile.write("-wdout71 0");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOffDevice131() // poweroff gps device
	{
		try {
			CtrlFile.write("-wdout131 0");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOffDevice72() // poweroff gps device
	{
		try {
			CtrlFile.write("-wdout72 0");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void PowerOffDevice87() // poweroff gps device
	{
		try {
			CtrlFile.write("-wdout87 0");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOffDevice63() // poweroff gps device
	{
		try {
			CtrlFile.write("-wdout63 0");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOffDevice64() // poweroff gps device
	{
		try {
			CtrlFile.write("-wdout64 0");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOffDevice70() // poweroff gps device
	{
		try {
			CtrlFile.write("-wdout70 0");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOffDevice73() // poweroff gps device
	{
		try {
			CtrlFile.write("-wdout73 0");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void PowerOffLaser() // poweroff laser device
	{
		try {
			CtrlFile.write("lightoff");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOnLaser() // poweron laser device
	{
		try {
			CtrlFile.write("lighton");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOnDevice98() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout98 1");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOnDevice99() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout99 1");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOffDevice99() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout99 0");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void PowerOffDevice98() // poweron gps device
	{
		try {
			CtrlFile.write("-wdout98 0");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//-------------led test---------------------------
	public void PowerOnRed() throws IOException		
	{
		CtrlFile.write("-wdout15 1");
		CtrlFile.flush();
	}
	
	public void PowerOffRed() throws IOException	   
	{
  		CtrlFile.write("-wdout15 0");
  		CtrlFile.flush();
	}
	
	public void PowerOnGreen() throws IOException		
	{
		CtrlFile.write("-wdout16 1");
		CtrlFile.flush();
	}
	
	public void PowerOffGreen() throws IOException	   
	{
  		CtrlFile.write("-wdout16 0");
  		CtrlFile.flush();
	}
	
	public void PowerOnBlue() throws IOException		
	{
		CtrlFile.write("-wdout18 1");
		CtrlFile.flush();
	}
	
	public void PowerOffBlue() throws IOException	   
	{
  		CtrlFile.write("-wdout18 0");
  		CtrlFile.flush();
	}
	public void PowerOnlaser() throws IOException	   
	{
  		CtrlFile.write("-wdout46 1");
  		CtrlFile.flush();
	}
	public void PowerOfflaser() throws IOException	   
	{
  		CtrlFile.write("-wdout46 0");
  		CtrlFile.flush();
	}
	//--------------------------------------------
	//-----------------------------------------------------
	
	// public void PowerOnDevice() //poweron lf device
	// {
	// try {
	// CtrlFile.write("-wdout64 1");
	// CtrlFile.flush();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	//
	// public void PowerOffDevice() //poweroff lf device
	// {
	// try {
	// CtrlFile.write("-wdout64 0");
	// CtrlFile.flush();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	public void TriggerOnDevice() // make barcode begin to
									// scan
	{
		try {
			CtrlFile.write("trig");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void TriggerOffDevice() // make barcode stop scan
	{
		try {
			CtrlFile.write("trigoff");
			CtrlFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void DeviceClose() throws IOException // close file
	{
		CtrlFile.close();
	}
}