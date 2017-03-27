package common.utils;

/**
 * Created by lenovo on 2016-06-27.
 */
public class Cmd {
    /**
     * @param type 0x11  0x21
     * @return
     */
    public static byte[] getPowerCmd(byte type) {
        //aabb05000000110651
        //aabb05000000120651
        //  //IC卡复位3V
//        aabb05000000110651
//IC卡复位5V
//        aabb05000000120651
        byte[] cmd = new byte[]{(byte) 0xaa, (byte) 0xbb, 0x05, 0x00, 0x00, 0x00, 0x11, (byte)
                0x06, 0x51};
        cmd[6] = type;
        return cmd;
    }

    public static byte[] getRomdan(byte type) {
        //0084000008
        //aabb 0A00 0000 2306 00A4040012 51
        //aabb 0a00 0000 2306 0084000008 51
        //aabb 0500 0000 1306 0084000008 51
        byte[] cmd = new byte[]{(byte) 0xaa, (byte) 0xbb, 0x0a, 0x00, 0x00, 0x00, 0x13, 0x06,
                0x00, (byte)
                0x84, 0x00, 0x00, 0x08, 0x51};
        cmd[6] = type;
        return cmd;
    }

    /**
     * @param cmd  adpu指令
     * @param type 0x13卡1 0x23卡2
     * @return 3310格式指令
     */
    public static byte[] adpuPackage(byte[] cmd, byte type) {

        int addCount = 0;
        for (int j = 0; j < cmd.length; j++) {
            if (cmd[j] == (byte) 0xaa) {
                addCount++;
            }
        }
        byte[] result = new byte[cmd.length + 9 + addCount];
        result[0] = (byte) 0xaa;
        result[1] = (byte) 0xbb;
        int len = cmd.length + 5;
        result[2] = (byte) len;
        result[3] = 0x00;
        result[4] = 0x00;
        result[5] = 0x00;
        result[6] = type;
        result[7] = 0x06;
        result[result.length - 1] = 0x51;
        int startCount = 8;
        for (int i = 0; i < cmd.length; i++) {
            if (cmd[i] == (byte) 0xaa) {
                result[startCount] = cmd[i];
                result[startCount + 1] = 0x00;
                startCount++;
            } else {
                result[startCount] = cmd[i];
            }
            startCount++;
        }
//        System.arraycopy(cmd, 0, result, 8, cmd.length);
        return result;
    }

    public static byte[] unPackage(byte[] cmd) {//解包

        if (cmd == null || cmd.length < 3) {
            return null;
        }
        if (cmd[0] != (byte) 0xaa || cmd[1] != (byte) 0xbb || cmd[2] <= 6) {
            return null;
        }
        //aabb 1200 0000 1306 00 00 00 62 02 01 00 00 00 00 01 90 00 e5
        //aabb 0800 0000 2306 00 61 aa 32 76
        byte[] result = new byte[cmd.length - 10];
        int subCount = 0;//记录返回的数据中有几个0xaa
        int startCount = 9;
        for (int i = 0; i < cmd.length - 10; i++) {
            byte data = cmd[i + startCount];
            if (data == (byte) 0xaa) {
                startCount++;
                subCount++;
            }
            result[i] = data;
        }

        int length = result.length - subCount;
        byte[] finalresult = new byte[length];
        System.arraycopy(result, 0, finalresult, 0, length);
        return finalresult;
    }
    //aa bb 10 00 00 00 13 06 00 00 00 00 16 6b 14 a7 9f 90 00 d4
    //aa bb 10 00 00 00 13 06 00 00 00 00 47 e8 4b 17 fc 90 00 8a
}
