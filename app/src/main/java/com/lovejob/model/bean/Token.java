package com.lovejob.model.bean;

import java.io.Serializable;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob3
 * Package_Name:com.lovejob.model.bean
 * Created on 2016-11-29 00:22
 */

public class Token implements Serializable {
    String lv1, lv2, lv3, lv4, lv5, lv6, count;

    public Token(String lv1, String lv2, String lv3, String lv4, String lv5, String lv6, String count) {
        this.lv1 = lv1;
        this.lv2 = lv2;
        this.lv3 = lv3;
        this.lv4 = lv4;
        this.lv5 = lv5;
        this.lv6 = lv6;
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLv1() {
        return lv1;
    }

    public void setLv1(String lv1) {
        this.lv1 = lv1;
    }

    public String getLv2() {
        return lv2;
    }

    public void setLv2(String lv2) {
        this.lv2 = lv2;
    }

    public String getLv3() {
        return lv3;
    }

    public void setLv3(String lv3) {
        this.lv3 = lv3;
    }

    public String getLv4() {
        return lv4;
    }

    public void setLv4(String lv4) {
        this.lv4 = lv4;
    }

    public String getLv5() {
        return lv5;
    }

    public void setLv5(String lv5) {
        this.lv5 = lv5;
    }

    public String getLv6() {
        return lv6;
    }

    public void setLv6(String lv6) {
        this.lv6 = lv6;
    }
}
