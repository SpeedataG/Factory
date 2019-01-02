package com.spdata.factory;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.base.act.FragActBase;
import common.event.ViewMessage;

@EActivity(R.layout.tc01_gpio_layout)
public class Tc01GpioAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_GPIOS, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_GPIOS, App.KEY_FINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "GPIO测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }


    @AfterViews
    protected void main() {
        onWindowFocusChanged(true);
        initTitlebar();
        setSwipeEnable(false);
        checkGpio();
        if (errList.size() == 0) {
            tvVersionInfor.setTextColor(Color.GREEN);
            tvVersionInfor.setText("GPIO测试全部通过");
            btnPass.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < errList.size(); i++) {
                tvVersionInfor.setTextColor(Color.RED);
                tvVersionInfor.append(errList.get(i) + "-测试不通过\n");
            }
        }
    }

    private String[] byteGpios = {"93", "94", "95", "96", "98", "119", "125", "83", "88", "87", "121", "126", "128", "72", "9", "99", "127", "129", "73", "122", "120", "82", "140", "70", "124", "132", "131", "71"};
    private List errList = new ArrayList();


    private void checkGpio() {
        List list = MainGPIO();
        for (int j = 0; j < byteGpios.length; j++) {
            for (int i = 1; i < list.size(); i++) {
                String lists = list.get(i).toString();
                String gpio = lists.substring(0, lists.indexOf(":"));
                String upordown = lists.substring(6, 7);
                //gpio去空格
                if (byteGpios[j].equals(gpio.trim())) {
                    if (upordown.equals("1")) {
                    } else if (upordown.equals("0")) {
                        errList.add(byteGpios[j]);
                    } else {
                        Toast.makeText(Tc01GpioAct.this, "未知错误！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    continue;
                }
            }
        }
    }

    public List MainGPIO() {
        BufferedReader reader = null;
        List lists = new ArrayList();
        try {
            reader = new BufferedReader(new FileReader("sys/class/misc/mtgpio/pin"));
            String line = null;
            try {
                for (int i = 1; i < 203; i++) {
                    if ((line = reader.readLine()) != null) {
                        lists.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return lists;
    }

}
