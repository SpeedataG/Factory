package com.spdata.factory.wifibrobe;

/**
 * Created by 张明_ on 2018/7/10.
 * Email 741183142@qq.com
 */

public class WifiBean {

    /**
     * 5C:CF:7F:B7:36:03|5C:CF:7F:28:D1:5E|FF:FF:FF:FF:FF:FF|00|04|9|-68|0|0|0
     * 探针自身MAC，指探针WiFi模块的MAC地址，可用于区别数据由哪个探针设备采集到。
     * 源MAC，指探针抓取到的WiFi信号的发射端的MAC地址，一般为手机，为路由器的部分已经被过滤（过滤超过90%）。
     * 目的MAC，指探针抓取到的WiFi信号的接收端的MAC地址，可能为手机也可能为路由器。
     * 帧主类型，包含三个值，00、01、02，分别对应管理帧、控制帧、数据帧。
     * 帧子类型，在不同的主类型下，各类型表示的含义各不相同，详见WiFi类型说明文档。
     * 信道，是一个数字编号，特指WiFi协议标准下的一个特定通信频段。
     * 信号强度，指探针抓取到的WiFi信号的强度，最小值为“-100”；一般来说，此值越大表示发射设备离探针越近。
     * 是否在省电模式，指WiFi信号的发射设备是否处于省电模式，“0”表示“否”，“1”表示“是”；一般来说，如果设备处于省电模式，发出的WIFi信号会比较少。
     * 数据是否为非路由器发出，“0”表示为非路由器发出，一般是手机；“1”表示数据为路由器发出；为“0”时，可以用于做手机到探针的距离估算。
     * 保留字段，暂无意义，可忽略。
     **/
    private String zishenMac;
    private String yuanMAC;
    private String muMAC;
    private String zhenZhu;
    private String zhenZi;
    private String xinDao;
    private String xinHao;
    private String shengdianMode;
    private String isWifiSend;

    public WifiBean(String zishenMac, String yuanMAC, String muMAC, String zhenZhu, String zhenZi, String xinDao, String xinHao, String shengdianMode, String isWifiSend) {
        this.zishenMac = zishenMac;
        this.yuanMAC = yuanMAC;
        this.muMAC = muMAC;
        this.zhenZhu = zhenZhu;
        this.zhenZi = zhenZi;
        this.xinDao = xinDao;
        this.xinHao = xinHao;
        this.shengdianMode = shengdianMode;
        this.isWifiSend = isWifiSend;
    }

    public String getZishenMac() {
        return zishenMac;
    }

    public void setZishenMac(String zishenMac) {
        this.zishenMac = zishenMac;
    }

    public String getYuanMAC() {
        return yuanMAC;
    }

    public void setYuanMAC(String yuanMAC) {
        this.yuanMAC = yuanMAC;
    }

    public String getMuMAC() {
        return muMAC;
    }

    public void setMuMAC(String muMAC) {
        this.muMAC = muMAC;
    }

    public String getZhenZhu() {
        return zhenZhu;
    }

    public void setZhenZhu(String zhenZhu) {
        this.zhenZhu = zhenZhu;
    }

    public String getZhenZi() {
        return zhenZi;
    }

    public void setZhenZi(String zhenZi) {
        this.zhenZi = zhenZi;
    }

    public String getXinDao() {
        return xinDao;
    }

    public void setXinDao(String xinDao) {
        this.xinDao = xinDao;
    }

    public String getXinHao() {
        return xinHao;
    }

    public void setXinHao(String xinHao) {
        this.xinHao = xinHao;
    }

    public String getShengdianMode() {
        return shengdianMode;
    }

    public void setShengdianMode(String shengdianMode) {
        this.shengdianMode = shengdianMode;
    }

    public String getIsWifiSend() {
        return isWifiSend;
    }

    public void setIsWifiSend(String isWifiSend) {
        this.isWifiSend = isWifiSend;
    }
}
