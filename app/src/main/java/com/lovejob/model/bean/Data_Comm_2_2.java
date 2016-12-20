package com.lovejob.model.bean;
/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob_Android
 * Package_Name:com.lovejob_android.modle
 * Created on 2016-10-14 16:33
 */

public class Data_Comm_2_2 {
    String answerName;
    String answerContent;

    public Data_Comm_2_2(String answerName, String answerContent) {
        this.answerName = answerName;
        this.answerContent = answerContent;
    }

    public String getAnswerName() {
        return answerName;
    }

    public void setAnswerName(String answerName) {
        this.answerName = answerName;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }
}
