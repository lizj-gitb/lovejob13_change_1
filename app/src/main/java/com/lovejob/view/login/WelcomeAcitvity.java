package com.lovejob.view.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.captain_miao.grantap.CheckAnnotatePermission;
import com.example.captain_miao.grantap.annotation.PermissionDenied;
import com.example.captain_miao.grantap.annotation.PermissionGranted;
import com.lovejob.R;
import com.lovejob.model.StaticParams;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CirclePageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ClassType:欢迎页面
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.view.loginmodle
 * Created on 2016-11-21 10:37
 */

public class WelcomeAcitvity extends Activity {


    private boolean isInto = false;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.indicator)
    CirclePageIndicator indicator;
    @Bind(R.id.btnHome)
    Button btnHome;
    @Bind(R.id.guideImage)
    ImageView guideImage;
    private GalleryPagerAdapter adapter;
    private int[] images = {
            R.mipmap.index1,
            R.mipmap.index2,
            R.mipmap.index3,
            R.mipmap.index4
    };
    private String toOtherActivity;
    private String otherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hiteTitleBar();
        setContentView(R.layout.welcomelayout);
        ButterKnife.bind(this);
        V.d("into welcome activity...");
        initGuideGallery();
        toOtherActivity = getIntent().getStringExtra("toOtherActivity");
        otherId = getIntent().getStringExtra("otherId");
    }

    public class GalleryPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView item = new ImageView(WelcomeAcitvity.this);
            item.setScaleType(ImageView.ScaleType.CENTER_CROP);
            item.setImageResource(images[position]);
            container.addView(item);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }

    private void initGuideGallery() {
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInto) {
                    new AppPreferences(WelcomeAcitvity.this).put(StaticParams.FileKey.__ISFIRSTSTARTAPP__, false);
                    Intent intent =new Intent(WelcomeAcitvity.this, LoginAcitvity.class);
                    intent.putExtra("otherId", otherId);
                    intent.putExtra("toOtherActivity", toOtherActivity);
                    startActivity(intent);
                    finish();
                } else {
                    askPerm();
                }
            }
        });

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setVisibility(View.VISIBLE);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setVisibility(View.VISIBLE);

        adapter = new GalleryPagerAdapter();
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == images.length - 1) {
                    //滑动到最后一页时
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(3000);
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (indicator.getVisibility() == View.GONE) {
//                                            SharedPreferences.getInstance().putBoolean("first-time-use", false);
//                                            startActivity(new Intent(context, Aty_Login.class));
//                                            finish();
//                                        }
//                                    }
//                                });
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
                    btnHome.setVisibility(View.VISIBLE);
                    btnHome.startAnimation(fadeIn);
                    askPerm();
                } else {
                    indicator.setVisibility(View.VISIBLE);
                    btnHome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void askPerm() {
        CheckAnnotatePermission
                .from(this, this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    @PermissionGranted()
    public void permissionGranted() {
        isInto = true;
    }

    @PermissionDenied()
    public void permissionDenied() {
        isInto = false;
        Toast.makeText(this, "为了更好的体验app请开启相关权限！", Toast.LENGTH_SHORT).show();
    }

    private void hiteTitleBar() {
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        V.d("welcome activity title was hite");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        V.d("开始销毁");
        for (int i = 0; i < 10; i++) {
            System.gc();
            System.runFinalization();
        }
    }
}
