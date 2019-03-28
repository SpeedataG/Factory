package com.spdata.factory.wifibrobe;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spdata.factory.R;

import java.util.List;

public class MainActAdapter extends BaseQuickAdapter<WifiBean, BaseViewHolder> {
    public MainActAdapter(int layoutResId, @Nullable List<WifiBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiBean item) {
        helper.setText(R.id.tv_zishenMac, item.getZishenMac());
        helper.setText(R.id.tv_yuanMAC, item.getYuanMAC());
        helper.setText(R.id.tv_muMAC, item.getMuMAC());
        helper.setText(R.id.tv_zhenZhu, item.getZhenZhu());
        helper.setText(R.id.tv_zhenZi, item.getZhenZi());
        helper.setText(R.id.tv_xinDao, item.getXinDao());
        helper.setText(R.id.tv_xinHao, item.getXinHao());
        helper.setText(R.id.tv_shengdianMode, item.getShengdianMode());
        helper.setText(R.id.tv_isWifiSend, item.getIsWifiSend());
    }
}
