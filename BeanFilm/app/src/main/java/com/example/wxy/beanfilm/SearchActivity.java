package com.example.wxy.beanfilm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Fragment.SearchResultFragment;
import com.example.wxy.beanfilm.Model.CompareService;
import com.example.wxy.beanfilm.Model.SearchService;

import java.util.List;

import static com.example.wxy.beanfilm.Bean.FilmSimple.*;
import static com.example.wxy.beanfilm.Bean.FilmSimple.Source.*;

public class SearchActivity extends AppCompatActivity{

    private static String TAG = "SearchActivity" ;
    private boolean isFirst = true;
    private static Source sTagFlag = DOUBAN;//换页标志
    private String Search_key = "";

    private LinearLayout mLinearLayoutFirstTag;
    private LinearLayout mLinearLayoutSecondTag;
    private LinearLayout mCompareTag;
    private ImageView mImageViewFirst;
    private ImageView mImageViewSecond;

    private AppCompatActivity mAppCompatActivity;

    List<FilmSimple> mFilmSimples;

    private FragmentManager fm;
    private Fragment currentFragment;

    private SearchService mSearchService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接
            mSearchService = ((SearchService.SearchBinder)service).getService();
            mSearchService.setCallback(new SearchService.Callback() {
                @Override
                public void onDataChange(SearchService.SearchState newSearchState,List<FilmSimple> filmSimples) {
                    //此处依然不能更新UI
                    mFilmSimples = filmSimples;
                    Message msg = new Message();
                    msg.obj = newSearchState;
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
        setContentView(R.layout.activity_search);
        mAppCompatActivity = this;

        Intent intent  = getIntent();
        Search_key = intent.getStringExtra("search_key");
        startSearchSercive(Search_key, DOUBAN);

        mCompareTag = (LinearLayout) this.findViewById(R.id.source_nav_compare_button);
        mLinearLayoutFirstTag = (LinearLayout)this.findViewById(R.id.source_nav_first_tag);
        mLinearLayoutSecondTag = (LinearLayout)this.findViewById(R.id.source_nav_second_tag);
        mImageViewFirst = (ImageView)this.findViewById(R.id.source_nav_first_ico);
        mImageViewSecond = (ImageView)this.findViewById(R.id.source_nav_second_ico);

        mImageViewFirst.setColorFilter(Color.parseColor("#41bd56"));
        mImageViewSecond.setColorFilter(Color.parseColor("#969696"));

        /*碎片显示管理*/
        fm = getSupportFragmentManager();
        currentFragment = fm.findFragmentById(R.id.source_fragment_container);

        //单击豆瓣Tag
        mLinearLayoutFirstTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(sTagFlag != DOUBAN){//若当前显示不是豆瓣
                    sTagFlag = DOUBAN;

                    mImageViewFirst.setColorFilter(Color.parseColor("#41bd56"));
                    mImageViewSecond.setColorFilter(Color.parseColor("#969696"));

                    replaceFragment("douban");

                }
            }
        });

        //单击猫眼Tag
        mLinearLayoutSecondTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sTagFlag != MAOYAN){//若当前显示不是豆瓣
                    sTagFlag = MAOYAN;

                    mImageViewSecond.setColorFilter(Color.parseColor("#41bd56"));
                    mImageViewFirst.setColorFilter(Color.parseColor("#969696"));

                    replaceFragment("maoyan");

                }
            }
        });

        mCompareTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FilmSimple> checkedfilmSimples = ((SearchResultFragment)currentFragment).mAdapter.mCheckedFilmSiple;
                if(checkedfilmSimples.size()==2){
                    FilmSimple f1 = checkedfilmSimples.get(0);
                    FilmSimple f2 = checkedfilmSimples.get(1);
                    startActivity(CompareActivity.newIntent(mAppCompatActivity,f1,f2));
                }else{
                    Toast.makeText(mAppCompatActivity,"请选择两个电影",Toast.LENGTH_SHORT).show();
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
            switch (sTagFlag) {
                case DOUBAN:
                    Intent intent1  = getIntent();
                    Search_key = intent1.getStringExtra("search_key");
                    startSearchSercive(Search_key, DOUBAN);
                    //currentFragment = new SearchResultFragment(sTagFlag,mFilmSimples);
                    break;
                case MAOYAN:
                    Intent intent2  = getIntent();
                    Search_key = intent2.getStringExtra("search_key");
                    startSearchSercive(Search_key, Source.MAOYAN);
                    //currentFragment = new SearchResultFragment(sTagFlag,mFilmSimples);
                    break;
            }
            //getSupportFragmentManager().beginTransaction().add(R.id.source_fragment_container, currentFragment, tag).commit();
        }else {
            getSupportFragmentManager().beginTransaction().show(currentFragment).commit();
        }
    }


    /*启动查询服务*/
    public void startSearchSercive(String query, FilmSimple.Source type){
        Intent toSearchService = SearchService.newIntent(this,query,type);
        startService(toSearchService);
        bindService(toSearchService,mConnection,BIND_AUTO_CREATE);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //此处更新UI
            String str = new String();
            String tag = "";
            switch (sTagFlag ){
                case DOUBAN:
                    tag = "douban";
                    break;
                case MAOYAN:
                    tag = "maoyan";
                    break;
                default:
            }
            switch (msg.obj.toString()){
                case"SEARCH_SUCCESS":
                    str = "搜索成功";
                    currentFragment = new SearchResultFragment().newInstance(sTagFlag, mFilmSimples);
                    if(isFirst) {
                        isFirst = false;
                        fm.beginTransaction()
                                .add(R.id.source_fragment_container, currentFragment)
                                .commit();
                    }else {
                        getSupportFragmentManager().beginTransaction().add(R.id.source_fragment_container, currentFragment, tag).commit();
                    }
                    //updateUI();
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
