package com.spdata.factory.bean;

import java.io.Serializable;

/**
 * Created by suntianwei on 2016/11/28.
 */

public class ResultState implements Serializable {
    private int num;
    private String name;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
