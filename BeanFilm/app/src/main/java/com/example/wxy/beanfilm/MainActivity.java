package com.example.wxy.beanfilm;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Fragment.HomeFragment;
import com.example.wxy.beanfilm.Fragment.MineFragment;
import com.example.wxy.beanfilm.Model.ActorsAdapter;
import com.example.wxy.beanfilm.Model.HomeFilmAdapter;
import com.example.wxy.beanfilm.Model.HomeHotFilmsService;
import com.example.wxy.beanfilm.Model.SearchService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static boolean sTagFlag = true;//首页为true，个人主页为false
    private static boolean isFirst = true;
    private LinearLayout mLinearLayoutHomeTag;
    private LinearLayout mLinearLayoutMineTag;
    private TextView mTextViewHome;
    private TextView mTextViewMine;
    private ImageView mImageViewHome;
    private ImageView mImageViewMine;

    private FragmentManager fm;
    private Fragment currentFragment;

    List<FilmSimple> mHotFilmSimples = new ArrayList<>();
    private HomeHotFilmsService mHomeHotFilmsService;
    private ServiceConnection mHomeHotFilmsConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接
            mHomeHotFilmsService = ((HomeHotFilmsService.HomeHotFilmsBinder)service).getService();
            mHomeHotFilmsService.setCallback(new HomeHotFilmsService.Callback() {
                @Override
                public void onDataChange(HomeHotFilmsService.State newState, List<FilmSimple> filmSimples) {
                    //此处依然不能更新UI
                    mHotFilmSimples = filmSimples;
                    Message msg = new Message();
                    msg.obj = newState;
                    handler.sendMessage(msg);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //服务断开
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayoutHomeTag = (LinearLayout)this.findViewById(R.id.main_nav_home_tag);
        mLinearLayoutMineTag = (LinearLayout)this.findViewById(R.id.main_nav_mine_tag);
        mTextViewHome = (TextView)this.findViewById(R.id.main_nav_home_text);
        mTextViewMine = (TextView)this.findViewById(R.id.main_nav_mine_text);
        mImageViewHome = (ImageView)this.findViewById(R.id.main_nav_home_ico);
        mImageViewMine = (ImageView)this.findViewById(R.id.main_nav_mine_ico);

        /*碎片显示管理*/
        fm = getSupportFragmentManager();
        currentFragment = fm.findFragmentById(R.id.fragment_container);

        /*
        if (currentFragment == null) {
            currentFragment = new HomeFragment();//暂时先显示个人页面
            fm.beginTransaction()
                    .add(R.id.fragment_container, currentFragment)
                    .commit();
        }*/
        HomeHotFilmsService.startActionFoo(this,mHomeHotFilmsConnection);

        //单击主页Tag
        mLinearLayoutHomeTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextPaint tp;
                if(!sTagFlag){//若当前显示个人主页

                    sTagFlag = !sTagFlag;

                    mTextViewHome.setTextColor(getResources().getColor(R.color.checked_text));
                    tp = mTextViewHome.getPaint();//加粗
                    tp.setFakeBoldText(true);
                    mImageViewHome.setAlpha(1.0f);

                    mTextViewMine.setTextColor(getResources().getColor(R.color.nomarl_text));
                    tp = mTextViewMine.getPaint();//加粗
                    tp.setFakeBoldText(false);
                    mImageViewMine.setAlpha(0.6f);

                    replaceFragment("home");

                }
            }
        });

        mLinearLayoutMineTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextPaint tp;
                if(sTagFlag){//若当前显示Home
                    sTagFlag = !sTagFlag;
                    mTextViewHome.setTextColor(getResources().getColor(R.color.nomarl_text));
                    tp = mTextViewHome.getPaint();//加粗
                    tp.setFakeBoldText(false);
                    mImageViewHome.setAlpha(0.6f);
                    mTextViewMine.setTextColor(getResources().getColor(R.color.checked_text));
                    tp = mTextViewMine.getPaint();//加粗
                    tp.setFakeBoldText(true);
                    mImageViewMine.setAlpha(1.0f);

                    replaceFragment("mine");
                }
            }
        });
    }

    /*替换碎片 */
    private void replaceFragment(String tag) {
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).commit();
        }
        currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment == null) {
            switch (tag) {
                case "home":
                    currentFragment = new HomeFragment(mHotFilmSimples);
                    break;
                case "mine":
                    currentFragment = new MineFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, currentFragment, tag).commit();
        }else {
            getSupportFragmentManager().beginTransaction().show(currentFragment).commit();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //此处更新UI
            String str = new String();

            switch (msg.obj.toString()){
                case"SEARCH_SUCCESS":
                    str = "搜索成功";
                    currentFragment = new HomeFragment(mHotFilmSimples);
                    if(isFirst) {
                        isFirst = false;
                        fm.beginTransaction()
                                .add(R.id.fragment_container, currentFragment)
                                .commit();
                    }
                    break;
                case "NOT_EXISTENT"://无记录
                    str = "无记录";
                    break;
                case "NETWORK_ERROR"://网络异常
                    str = "网络异常";
                    break;
                default:

            }
            Toast.makeText(getApplication(),str,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mHomeHotFilmsConnection);
    }
}
