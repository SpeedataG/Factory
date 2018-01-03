package common.utils;

/**
 * Created by lenovo-pc on 2018/1/3.
 */

public class permissUtils {
    public permissUtils(String[] permiss) {
        this.permiss = permiss;
    }

    private String[] permiss = null;

    public String[] getPermiss() {
        return permiss;
    }

    public void setPermiss(String[] permiss) {
        this.permiss = permiss;
    }
}
