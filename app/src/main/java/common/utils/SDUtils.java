package common.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.format.Formatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/7/24.
 */
public class SDUtils {


    public Context mContext;

//    public SDUtils(Context mContext) {
//
//    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public String getSDTotalSize(String str) {
        StatFs stat = new StatFs(str);
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
    }


    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public String getSDAvailableSize(String str) {
        StatFs stat = new StatFs(str);
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public String getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public String getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
    }

    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public void copyBigDataToSD(String str) throws IOException
    {
//        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (str+"/Test.zip");
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(directory);
        myInput = mContext.getAssets().open("ss.zip");
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while(length > 0)
        {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
//    private Activity mActivity;
    private StorageManager mStorageManager;
    private Method mMethodGetPaths;

    public SDUtils(Context mContext) {
        this.mContext = mContext;
        if (mContext != null) {
            mStorageManager = (StorageManager)mContext
                    .getSystemService(Activity.STORAGE_SERVICE);
            try {
                mMethodGetPaths = mStorageManager.getClass()
                        .getMethod("getVolumePaths");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getVolumePaths() {
        String[] paths = null;
        try {
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }
}

