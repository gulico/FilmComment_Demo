package com.example.wxy.beanfilm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wxy.beanfilm.Bean.Actor;
import com.example.wxy.beanfilm.Bean.Comment;
import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Fragment.SearchResultFragment;
import com.example.wxy.beanfilm.Model.FilmDetailService;
import com.example.wxy.beanfilm.Model.SearchService;

import java.util.List;

public class FilmDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "com.example.wxy.beanfilm.EXTRA.URL";

    private FilmSimple mFilmSimple = new FilmSimple();
    private FilmSimple.Source sTagFlag = FilmSimple.Source.NULL;
    //private FilmDetailService.State mState ;

    private FilmDetailService mFilmDetailService;

   private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接
            mFilmDetailService = ((FilmDetailService.FilmDetailBinder)service).getService();
            mFilmDetailService.setCallback(new FilmDetailService.Callback() {
                @Override
                public void onDataChange(FilmDetailService.State state,FilmSimple filmSimple,List<Comment> comments) {
                    //此处依然不能更新UI
                    mFilmSimple = filmSimple;
                    Message msg = new Message();
                    msg.obj = state;
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
        setContentView(R.layout.activity_film_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.film_deteail_toolbar);//工具栏
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.film_deteail_toolbar);

        ImageView mFilmPosterImageView = (ImageView)findViewById(R.id.film_deteail_poster) ;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        String Url = intent.getStringExtra(EXTRA_URL);

    }

    /*活动启动必需*/
    public static Intent newIntent(Context packageContext, String URL) {
        Intent intent = new Intent(packageContext, SearchService.class);
        intent.putExtra(EXTRA_URL, URL);
        return intent;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //此处更新UI
            String str = new String();
        /*    String tag = "";
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
            Toast.makeText(getApplication(),str,Toast.LENGTH_LONG).show();*/
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

}
