package common.psam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DeviceControl {
    private BufferedWriter CtrlFile;
    private String gpio = "64";

    public DeviceControl(String path) throws IOException {
        File DeviceName = new File(path);
        CtrlFile = new BufferedWriter(new FileWriter(DeviceName, false));    //open file
    }

    public DeviceControl(String path, String gpio) throws IOException {
        this.gpio = gpio;
        File DeviceName = new File(path);
        CtrlFile = new BufferedWriter(new FileWriter(DeviceName, false));    //open file
    }

    public void PowerOnDevice()        //poweron psam device
    {
        try {
            CtrlFile.write("-wdout" + gpio + " 1");   //上电IO口调整
            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void PowerOffDevice()     //poweroff psam device
    {
        try {
            CtrlFile.write("-wdout" + gpio + " 0");
            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //44 复位管脚
    public void PsamResetDevice(int resetGPIO) throws IOException        //reset psam device
    {
        //变成输出
        CtrlFile.write("-wdir" + resetGPIO + " 1");
        CtrlFile.flush();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CtrlFile.write("-wdout" + resetGPIO+" 0");
                CtrlFile.flush();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CtrlFile.write("-wdout"+resetGPIO+" 1");
        CtrlFile.flush();
    }

    public void DeviceClose() throws IOException        //close file
    {
        CtrlFile.close();
    }
}