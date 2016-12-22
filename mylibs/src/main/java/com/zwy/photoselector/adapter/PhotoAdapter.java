package com.zwy.photoselector.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zwy.R;
import com.zwy.photoselector.utils.AndroidLifecycleUtils;

import java.io.File;
import java.util.ArrayList;


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
        View itemView = inflater.inflate(R.layout.__picker_item_photo, parent, false);
        return new PhotoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        if (isImgFromNetwork) {
            //TODO 更换相册默认图片
            Glide.with(mContext).load(photoPaths.get(position)).dontAnimate().override(100, 100).placeholder(R.drawable.__picker_camera).into(holder.ivPhoto);
            return;
        } else {
//        Uri uri = Uri.parse(photoPaths.get(position));
            Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());
            if (canLoadImage) {
                Glide.with(mContext).load(uri).centerCrop()
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.__picker_camera)
                        .error(R.drawable.__picker_camera)
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
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
            vSelected.setVisibility(View.GONE);
        }
    }

}
