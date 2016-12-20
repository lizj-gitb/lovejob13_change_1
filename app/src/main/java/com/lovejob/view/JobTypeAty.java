package com.lovejob.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.MyApplication;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.ThreadPoolUtils;
import com.lovejob.model.Utils;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.http.okhttp3.zokhttp.ZokHttp;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;
import com.v.rapiddev.utils.Utils_RapidDev;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.MyListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/24.
 */
public class JobTypeAty extends BaseActivity {
    @Bind(R.id.lv_jobtype_left)
    MyListView lvJobtypeLeft;
    @Bind(R.id.sv_jobtype_left)
    PullToRefreshScrollView svJobtypeLeft;
    @Bind(R.id.lv_jobtype_right)
    MyListView lvJobtypeRight;
    @Bind(R.id.sv_jobtype_right)
    PullToRefreshScrollView svJobtypeRight;
    @Bind(R.id.img_jobtype_back)
    ImageView imgJobtypeBack;
    private Activity context;
    FastAdapter<ThePerfectGirl.PositionTypeInfoDTO> adapter_left;
    FastAdapter<ThePerfectGirl.PositionTypeInfo> adapter_right;
    private Call call_gettypeList;


    private void initAdpater() {
        adapter_left = new FastAdapter<ThePerfectGirl.PositionTypeInfoDTO>(context, R.layout.item_gv_jobtype_up) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_gv_jobtype_up, position);
                ImageView imageView = (ImageView) viewHolder.getView(R.id.iv_item_jobtype);
                imageView.setVisibility(View.VISIBLE);
                TextView textView = (TextView) viewHolder.getView(R.id.tv_item_lv_jobtype);
                textView.setText(getItem(position).getPositionName());
                if (adapter_right.getList().size() == 0) {
                    for (int i = 0; i < getItem(position).getList().size(); i++) {
                        adapter_right.addItem(getItem(position).getList().get(i));
                    }
                    adapter_right.notifyDataSetChanged();
                }
                return viewHolder.getConvertView();
            }
        };
        adapter_right = new FastAdapter<ThePerfectGirl.PositionTypeInfo>(context, R.layout.item_gv_jobtype_up) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_gv_jobtype_up, position);
                ImageView imageView = (ImageView) viewHolder.getView(R.id.iv_item_jobtype);
                imageView.setVisibility(View.GONE);
                TextView textView = (TextView) viewHolder.getView(R.id.tv_item_lv_jobtype);
                textView.setText(getItem(position).getPositionName());
                return viewHolder.getConvertView();
            }
        };
    }

    private void getDataFromService() {
        call_gettypeList = LoveJob.getPosstionTypeList(new OnAllParameListener() {
            @Override
            public void onSuccess(final ThePerfectGirl thePerfectGirl) {
                //TODO
                for (int i = 0; i < thePerfectGirl.getData().getPositionTypeInfoDTOs().size(); i++) {
                    adapter_left.addItem(thePerfectGirl.getData().getPositionTypeInfoDTOs().get(i));
                }
                adapter_left.notifyDataSetChanged();
                //
                V.d("职位类型获取成功，开始序列化到文件");
                ThreadPoolUtils.getInstance().addTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
//需要一个文件输出流和对象输出流；文件输出流用于将字节输出到文件，对象输出流用于将对象输出为字节

                            File file = new File(Utils_RapidDev.getSDCardPath() + "china_cities.db");
                            if (file.exists()) {
                                V.e("文件已经存在");
                                file.delete();
                            }

                            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                            out.writeObject(thePerfectGirl);
                            out.close();
                            V.e("对象序列化到文件成功");
                        } catch (IOException e) {
                            V.e("对象序列化到文件失败");
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    @OnClick({R.id.img_jobtype_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_jobtype_back:
                finish();
                break;
        }
    }

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_jobtype);
        ButterKnife.bind(this);
        context = this;
        initAdpater();
        lvJobtypeLeft.setAdapter(adapter_left);
        lvJobtypeRight.setAdapter(adapter_right);
        //设置上啦下拉   关闭
        svJobtypeLeft.setMode(PullToRefreshBase.Mode.DISABLED);
        svJobtypeRight.setMode(PullToRefreshBase.Mode.DISABLED);
        lvJobtypeLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                V.d(adapter_left.getItem(position).getPositionName());
                adapter_right.removeAll();
                for (int i = 0; i < adapter_left.getItem(position).getList().size(); i++) {
                    adapter_right.addItem(adapter_left.getItem(position).getList().get(i));
                }
                adapter_right.notifyDataSetChanged();
            }
        });
        lvJobtypeRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("positionName", adapter_right.getItem(position).getPositionName());
                intent.putExtra("positionNumber", adapter_right.getItem(position).getPositionNumber());
                setResult(StaticParams.RequestCode.RequestCode_ToWheelSelector_jobtype, intent);
                finish();
            }
        });
    }

    @Override
    public void onResume_() {
        if (MyApplication.getJobType() != null) {
            ThePerfectGirl thePerfectGirl = MyApplication.getJobType();
            for (int i = 0; i < thePerfectGirl.getData().getPositionTypeInfoDTOs().size(); i++) {
                adapter_left.addItem(thePerfectGirl.getData().getPositionTypeInfoDTOs().get(i));
            }
            adapter_left.notifyDataSetChanged();
        } else {
            getDataFromService();
        }
    }

    @Override
    public void onDestroy_() {

    }
}
