package com.spdata.factory.bean;

/**
 * Created by Administrator on 2016/7/13.
 */
public class ListItem {
    private String title;
    private String subTitle;
    private int textcolor;
    private boolean isFinish;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getTextcolor() {
        return textcolor;
    }

    public void setTextcolor(int textcolor) {
        this.textcolor = textcolor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "isFinish=" + isFinish +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", textcolor=" + textcolor +
                ", state='" + state + '\'' +
                '}';
    }
}
