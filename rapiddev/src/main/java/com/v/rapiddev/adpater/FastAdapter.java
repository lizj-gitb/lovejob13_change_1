package com.v.rapiddev.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassType: 万能的适配器
 * User:wenyunzhao
 * ProjectName:FastLibSimple
 * Package_Name:fast.fast.adpater
 * Created on 2016-10-09 16:19
 */
public abstract class FastAdapter<T> extends BaseAdapter {
    private List<T> list = new ArrayList<T>();
    private Context context;
    private int resId;//

    public FastAdapter(Context context, int resId) {
        this.context = context;
        this.resId = resId;
    }

    public void removeAll() {
//        this.list = new ArrayList<>();
        if (list.size() > 0)
            this.list.clear();
    }

    public List<T> getList() {

        return list;
    }

    public void setItem(int positoon, T t) {
        list.set(positoon, t);
        notifyDataSetChanged();
    }


    public void addItem(T item) {
        list.add(item);
    }

    public void remove(int position) {
        list.remove(position);
    }

    public void removeLast() {
        remove(getCount() - 1);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getViewHolder(position, convertView, parent);
    }

    public abstract View getViewHolder(int position, View convertView, ViewGroup parent);

}


