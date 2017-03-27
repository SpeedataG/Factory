package common.utils;

import android.content.Context;
import android.util.Log;

import javax.mail.MessagingException;

import common.crash.utils.SendEmailUtil;

/**
 * 1.LOG信息打印工具类，需要和代码提示模版配套使用 最后修订 颜德情 20141123 修改打印null时候的崩溃
 * 2.LOG信息打印工具类，需要和代码提示模版配套使用 最后修订 颜德情 20160308 添加日志开启关闭方法
 *
 * @author Administrator
 */
public class LogUtil {
    public static final String TAG = "LogUtil";

    private static boolean debug = true; // ture,开启日志打印，true关闭日志打印


    public static void setDebug(boolean debug) {
        LogUtil.debug = debug;
    }

    private static Context mContext;


    public static void init(Context context) {
        mContext = context;
    }

    public static final int log_level1 = 1;
    public static final int log_level2 = 2;
    public static final int log_level3 = 3;
    public static final int log_level4 = 4;

    public static final int LEVEL_1 = 1; // ERROR日志
    public static final int LEVEL_2 = 2; // INFO日志
    public static final int LEVEL_3 = 3; // DEBUG日志

    public static final int log_level_default = log_level4;
    public static final int log_level_disable = 0xff;
    public static final boolean DEGUG_MODE = true;
    // private static final String[]
    // log_tag={SymbolPopupWindow.TAG,SymbolAdapter.TAG,
    // KeyboardAnimation.TAG};
    public static final String[] fileter = new String[]{}; // ־
    private static final int[] log_level = {1, 2, 3, 4};
    private static final String[] log_tag = {"SymbolPopupWindow.TAG",
            "SymbolAdapter.TAG", "KeyboardAnimation.TAG"};
    private static final int LOGE = 0;
    private static final int LOGI = 1;

    public static void i(String msg) {
        i(TAG, msg, log_level_default);
    }

    public static void i(String msg, int level) {
        i(TAG, msg, level);
    }

    public static void i(String TAG, String msg, int level) {
        log(TAG, msg, "", level, LOGI);
    }

    public static void e(String error) {
        log(TAG, "", error, log_level1, LOGE);
    }

    public static void e(String TAG, String msg, String error) {
        log(TAG, msg, error, log_level1, LOGE);
    }

    public static void log(String TAG, String msg, String error, int level,
                           int type) {
        if (debug) {
            for (int i = 0; i < log_level.length; i++) {
                if (level == log_level[i]) {
                    String out_tag = TAG;
                    for (int j = 0; j < log_tag.length; j++) {
                        if (TAG.equals(log_tag[j])) {
                            out_tag = LogUtil.TAG;
                            msg = TAG + " " + msg;
                            break;

                        }
                    }
                    if (type == LOGE) {
                        Log.e(out_tag, msg);
                        Log.e(out_tag, error);
                    } else {
                        Log.i(out_tag, msg);
                    }
                    break;
                }
            }
        }

    }

    public static void e(String tag, String info) {
        if (!debug) {
            return;
        } else {
            if (info == null) {
                info = "null";
            }
            info = decodeUnicode(info);
            if (fileter.length <= 0) {
                Log.e(tag, info);
            } else {
                for (int i = 0; i < fileter.length; i++) {
                    if (fileter[i].equalsIgnoreCase(tag)) {
                        Log.e(tag, info);
                    }
                }
            }
        }
    }

    public static void i(String tag, String info) {
        if (!debug) {
            return;
        } else {
            if (info == null) {
                info = "null";
            }
            info = decodeUnicode(info);
            if (fileter.length <= 0) {
                Log.i(tag, info);
            } else {
                for (int i = 0; i < fileter.length; i++) {
                    if (fileter[i].equalsIgnoreCase(tag)) {
                        Log.i(tag, info);
                    }
                }
            }
        }
    }

    public static void d(String tag, String info) {
        if (!debug) {
            return;
        } else {
            if (info == null) {
                info = "null";
            }
            info = decodeUnicode(info);
            if (fileter.length <= 0) {
                Log.d(tag, info);
            } else {
                for (int i = 0; i < fileter.length; i++) {
                    if (fileter[i].equalsIgnoreCase(tag)) {
                        Log.d(tag, info);
                    }
                }
            }
        }
    }

    /**
     * 只有在传入的debug =true & LogUtil.DEBUG_MODE 为true 的情况下才会打印LOG
     *
     * @param debug :true ,
     * @param tag
     * @param info
     */
    public static void i(boolean debug, String tag, String info) {
        if (debug) {
            if (info == null) {
                info = "null";
            }
            i(tag, info);
        }
    }

    /**
     * 只有在传入的debug =true & LogUtil.DEBUG_MODE 为true 的情况下才会打印LOG
     *
     * @param debug :true ,
     * @param tag
     * @param info
     */
    public static void e(boolean debug, String tag, String info) {
        if (debug) {
            if (info == null) {
                info = "null";
            }
            e(tag, info);
        }
    }

    /**
     * 只有在传入的debug =true & LogUtil.DEBUG_MODE 为true 的情况下才会打印LOG
     *
     * @param debug :true ,
     * @param tag
     * @param info
     */
    public static void d(boolean debug, String tag, String info) {
        if (debug) {
            if (info == null) {
                info = "null";
            }
            d(tag, info);
        }
    }

    /**
     * 解码unicode
     *
     * @param theString
     * @return
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }


    /**
     * 只有在传入的debug =true & LogUtil.DEBUG_MODE 为true 的情况下才会打印LOG
     *
     * @param debug :true ,
     * @param tag
     * @param info
     */
    public static void eMail(boolean debug, final String tag, final String info) {
        if (false) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        String name = info;
                        if (name == null) {
                            name = "null";
                        }
                        if (mContext != null) {
                            SendEmailUtil.sendTextByEmail(mContext,
                                    "tag=" + tag + "\n info=" + name + "");
                            e(tag, info);
                        }
                    } catch (MessagingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }.start();

        }
    }


}
