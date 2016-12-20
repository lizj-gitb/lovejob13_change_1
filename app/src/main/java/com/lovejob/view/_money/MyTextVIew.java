package com.lovejob.view._money;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob
 * Package_Name:com.lovejob.view._money
 * Created on 2016-12-08 17:45
 */

public class MyTextVIew extends TextView {
    public MyTextVIew(Context context) {
        super(context);
    }

    public MyTextVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
