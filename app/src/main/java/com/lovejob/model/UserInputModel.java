package com.lovejob.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.modles
 * Created on 2016-11-21 13:18
 */

public class UserInputModel implements Serializable{
    private boolean isNotEmpty;
    private String[] params;

    @Override
    public String toString() {
        return "UserInputModel{" +
                "isNotEmpty=" + isNotEmpty +
                ", params=" + Arrays.toString(params) +
                '}';
    }

    public UserInputModel(boolean isNotEmpty, String[] params) {
        this.isNotEmpty = isNotEmpty;
        this.params = params;
    }

    public boolean isNotEmpty() {
        return isNotEmpty;
    }

    public void setNotEmpty(boolean notEmpty) {
        isNotEmpty = notEmpty;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
