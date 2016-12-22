package com.zwy.views.edittext;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import com.zwy.R;

/**
 * Created by Administrator on 2016/12/14.
 */

public class ClearEditText extends EditText {
    private final static String TAG = "EditTextWithDel";
    private Context mContext;
    private Drawable imgClear;

    public ClearEditText(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        imgClear = mContext.getResources().getDrawable(R.drawable.ic_search_clear);
//        imgAble = mContext.getResources().getDrawable(R.drawable.delete);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)){
                    setCompoundDrawablesWithIntrinsicBounds(null,null,new BitmapDrawable(),null);
                }else {
//                    imgClear.setBounds(-30, 0, imgClear.getMinimumWidth(), imgClear.getMinimumHeight());
//                    setCompoundDrawables(null,null,imgClear,null);
                    setCompoundDrawablesWithIntrinsicBounds(null,null,imgClear,null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }



    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgClear != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;
            if(rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}