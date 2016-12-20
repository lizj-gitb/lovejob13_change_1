package com.v.rapiddev.adpater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.v.rapiddev.R;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:RapidDevSample
 * Package_Name:com.v.rapiddev.adpater
 * Created on 2016-11-24 16:22
 */

public class DynNewsAdapter<T> extends StaticPagerAdapter {
    private List<T> imags = new ArrayList<T>();

    public DynNewsAdapter(List<T> imags) {
        this.imags = imags;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
//            view.setImageResource(imgs[position]);
//        MyApplication.imageloader.displayImage(StaticParam.QiNiuYunUrl_News + imgs.get(position).getPictrueid(), view, ImageMode.DefaultImage, true, true);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Glide.with(container.getContext()).load(getItem(position)).placeholder(R.drawable.defaultnews).into(view);
        return view;
    }

    public void addItem(T t) {
        imags.add(t);
    }

    public T getItem(int position) {
        return imags.get(position);
    }

    @Override
    public int getCount() {
        return imags.size();
    }

}
