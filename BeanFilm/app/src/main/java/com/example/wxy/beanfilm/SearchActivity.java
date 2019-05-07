package com.example.wxy.beanfilm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Model.Adapter.SearchFilmFragmentStatePagerAdapter;
import com.example.wxy.beanfilm.Model.SearchService;

import java.util.ArrayList;
import java.util.List;

import static com.example.wxy.beanfilm.Bean.FilmSimple.*;
import static com.example.wxy.beanfilm.Bean.FilmSimple.Source.*;

public class SearchActivity extends AppCompatActivity{

    private static String TAG = "SearchActivity" ;
    private boolean isFirst = true;
    private static Source sTagFlag = DOUBAN;//换页标志
    private String Search_key = "";

    private LinearLayout mCompareTag;
    private ViewPager mViewPager1;
    private TabLayout mTabLayout;
    private String[] tabTitle = {"豆瓣","猫眼"};
    private boolean inittab2 = false;
    private boolean inittab1 = false;

    private AppCompatActivity mAppCompatActivity;

    List<FilmSimple> mFilmSimples;

    public List<FilmSimple> mCheckedFilmSiple = new ArrayList<FilmSimple>();
    public List<FilmSimple> mDouBanFilmSimples = new ArrayList<FilmSimple>();
    public List<FilmSimple> mMaoYanFilmSimples = new ArrayList<FilmSimple>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mAppCompatActivity = this;
        Intent intent  = getIntent();
        Search_key = intent.getStringExtra("search_key");
        mCompareTag = (LinearLayout) this.findViewById(R.id.source_nav_compare_button);

        initViews(this.getWindow().getDecorView());
        SearchService.startActionSearch(this,Search_key,DOUBAN,mConnection);
        SearchService.startActionSearch(this,Search_key,MAOYAN,mConnection);
        initData();

        mCompareTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCheckedFilmSiple.size()==2){
                    FilmSimple f1 = mCheckedFilmSiple.get(0);
                    FilmSimple f2 = mCheckedFilmSiple.get(1);
                    startActivity(CompareActivity.newIntent(mAppCompatActivity,f1,f2));
                }else{
                    Toast.makeText(mAppCompatActivity,"请选择两个电影",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void initViews(View rootView) {
        mViewPager1 = (ViewPager) rootView.findViewById(R.id.source_fragment_container);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.source_nav_compare_TabLayout);
    }
    public SearchFilmFragmentStatePagerAdapter mSearchFilmFragmentStatePagerAdapter ;
    private void initData() {
        for (int i=0; i<tabTitle.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTitle[i]));
        }
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#7CCD7C"));
        mTabLayout.setTabTextColors(Color.GRAY, Color.parseColor("#41bd56"));

        mSearchFilmFragmentStatePagerAdapter = new SearchFilmFragmentStatePagerAdapter(getSupportFragmentManager(),tabTitle);
        mViewPager1.setAdapter(mSearchFilmFragmentStatePagerAdapter);
        mViewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager1.setCurrentItem(tab.getPosition());
                int position = mViewPager1.getCurrentItem();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private SearchService mSearchService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            //服务连接
            mSearchService = ((SearchService.SearchBinder)service).getService();
            mSearchService.setCallback(new SearchService.Callback() {
                @Override
                public void onDataChangeDouBan(SearchService.SearchState newSearchState, List<FilmSimple> filmSimples) {
                    Message msg = new Message();
                    msg.obj = newSearchState;
                    msg.arg1 = 1;
                    mDouBanFilmSimples = filmSimples;
                    handler.sendMessage(msg);
                }

                @Override
                public void onDateChangeMaoYan(SearchService.SearchState newSearchState, List<FilmSimple> filmSimples) {
                    Message msg = new Message();
                    msg.obj = newSearchState;
                    msg.arg1 = 2;
                    mMaoYanFilmSimples = filmSimples;
                    Log.d(TAG, "onDataChange: "+mMaoYanFilmSimples.size());
                    handler.sendMessage(msg);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //服务断开
        }
    };

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
                    if(msg.arg1==1&&mSearchFilmFragmentStatePagerAdapter.getF1()!=null) {
                        Log.d(TAG, "handleMessage: 填充豆瓣");
                        mSearchFilmFragmentStatePagerAdapter.getF1().setSaerchList(mDouBanFilmSimples);
                        inittab1=true;
                    }
                    else if(msg.arg1==2&&mSearchFilmFragmentStatePagerAdapter.getF2()!=null) {
                        Log.d(TAG, "handleMessage: 填充猫眼");
                        mSearchFilmFragmentStatePagerAdapter.getF2().setSaerchList(mMaoYanFilmSimples);
                        inittab2 = true;
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
            Toast.makeText(getApplication(),str,Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
