package com.v.rapiddev.adpater;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ClassType:通用的ViewHolder
 * User:wenyunzhao
 * ProjectName:FastLibSimple
 * Package_Name:fast.fast.views
 * Created on 2016-10-14 10:48
 */

public class FFViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    private FFViewHolder() {
    }

    public FFViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * 获取ViewHolder对象 已优化
     *
     * @param context
     * @param convertView view
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static FFViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new FFViewHolder(context, parent, layoutId, position);
        } else {
            FFViewHolder holder = (FFViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    /***
     * 通过viewId返回View
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
//        View view = mViews.get(viewId);
//        if (view == null) {
//            view = mConvertView.findViewById(viewId);
//            mViews.put(viewId, view);
//        }
         View   view = mConvertView.findViewById(viewId);
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }
}
