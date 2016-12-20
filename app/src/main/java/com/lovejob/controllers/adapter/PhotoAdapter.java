package com.lovejob.controllers.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lovejob.R;
import com.lovejob.model.ThreadPoolUtils;
import com.v.rapiddev.utils.V;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import me.iwf.photopicker.utils.AndroidLifecycleUtils;

/**
 * Created by donglua on 15/5/31.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private ArrayList<String> photoPaths = new ArrayList<String>();
    private LayoutInflater inflater;

    private Context mContext;
    private boolean isImgFromNetwork;

    public PhotoAdapter(Context mContext, ArrayList<String> photoPaths, boolean isImgFromNetwork) {
        this.photoPaths = photoPaths;

        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.isImgFromNetwork = isImgFromNetwork;
    }

    public ArrayList<String> getList() {
        return this.photoPaths;
    }

    public void addItem(ArrayList<String> photoPaths) {
        this.photoPaths = photoPaths;
        notifyDataSetChanged();
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(me.iwf.photopicker.R.layout.__picker_item_photo, parent, false);
        return new PhotoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        if (isImgFromNetwork) {
            Glide.with(mContext).load(photoPaths.get(position)).dontAnimate().override(100, 100).placeholder(R.drawable.ic_launcher).into(holder.ivPhoto);
//            ThreadPoolUtils.getInstance().addTask(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Bitmap bitmap= Glide.with(mContext).load(photoPaths.get(position)).asBitmap().into(100,100).get();
//                        V.d("大小："+bitmap.getByteCount());
//                        holder.ivPhoto.setImageBitmap(bitmap);
//                        bitmap.recycle();
//                        bitmap=null;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
//                        boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());
//                        if (canLoadImage) {
//                            Glide.with(mContext).load(uri).centerCrop()
//                                    .thumbnail(0.1f)
//                                    .placeholder(R.drawable.ic_launcher)
//                                    .error(R.mipmap.ic_launcher)
//                                    .fitCenter()
//                                    .into(holder.ivPhoto);
//                        }
//                    }
//                }
//            });
            return;
        } else {
//        Uri uri = Uri.parse(photoPaths.get(position));
            Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());
            if (canLoadImage) {
                Glide.with(mContext).load(uri).centerCrop()
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .fitCenter()
                        .into(holder.ivPhoto);
            }
        }
    }


    @Override
    public int getItemCount() {
        return photoPaths.size();
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(me.iwf.photopicker.R.id.iv_photo);
            vSelected = itemView.findViewById(me.iwf.photopicker.R.id.v_selected);
            vSelected.setVisibility(View.GONE);
        }
    }

}
