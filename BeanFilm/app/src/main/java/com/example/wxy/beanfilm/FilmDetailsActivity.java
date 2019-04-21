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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wxy.beanfilm.Bean.Actor;
import com.example.wxy.beanfilm.Bean.Comment;
import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Bean.FilmSimpleLab;
import com.example.wxy.beanfilm.Bean.woffFont;
import com.example.wxy.beanfilm.Fragment.MineFragment;
import com.example.wxy.beanfilm.Fragment.SearchResultFragment;
import com.example.wxy.beanfilm.Model.ActorsAdapter;
import com.example.wxy.beanfilm.Model.CommentsAdapter;
import com.example.wxy.beanfilm.Model.FilmDetailService;
import com.example.wxy.beanfilm.Model.SearchService;
import com.example.wxy.beanfilm.Model.StarTools;

import java.util.ArrayList;
import java.util.List;

import static com.example.wxy.beanfilm.Model.FilmDetailService.State.SUCCESS;

public class FilmDetailsActivity extends AppCompatActivity {

    private String TAG = "FilmDetailsActivity";
    public static final String EXTRA_URL = "com.example.wxy.beanfilm.EXTRA.URL";
    public static final String EXTRA_SOURCE = "com.example.wxy.beanfilm.EXTRA.SOURCE";

    private RecyclerView mActorsRecyclerView;
    private ActorsAdapter mActorsAdapter;
    private RecyclerView mCommentsRecyclerView;
    private CommentsAdapter mCommentsAdapter;
    private AppCompatActivity mAppCompatActivity;

    private FilmSimple mFilmSimple = new FilmSimple();
    private FilmSimple.Source sTagFlag = FilmSimple.Source.NULL;
    private List<Comment> mComments = new ArrayList<Comment>();
    private FilmDetailService.State mState ;
    private woffFont mWoffFont;

    private FilmDetailService mFilmDetailService;

   private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接
            mFilmDetailService = ((FilmDetailService.FilmDetailBinder)service).getService();
            mFilmDetailService.setCallback(new FilmDetailService.Callback() {
                @Override
                public void onDataChange(FilmDetailService.State state,FilmSimple filmSimple,List<Comment> comments) {
                    mFilmSimple = filmSimple;
                    mComments = comments;
                    Message msg = new Message();
                    msg.obj = state;
                    handler.sendMessage(msg);
                }

                @Override
                public void onDataChange(FilmDetailService.State state, FilmSimple filmSimple, List<Comment> comments, woffFont font) {
                    mFilmSimple = filmSimple;
                    mComments = comments;
                    mWoffFont = font;
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

    ImageView mFilmPosterImageView;//海报
    TextView mTitleTextView;//标题
    TextView mClassifyTextView;//电影分类
    TextView mDateTextView;//上映日期
    TextView mLastingTextView;//片长
    TextView mScoreTextView;//评分
    TextView mPeopleNumTextView;//评分人数
    TextView mBreifTextView;//简介
    ImageView mStar1;
    ImageView mStar2;
    ImageView mStar3;
    ImageView mStar4;
    ImageView mStar5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);
        mAppCompatActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.film_deteail_toolbar);//工具栏
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.film_deteail_collapsing_toolbar);

        mFilmPosterImageView = (ImageView)findViewById(R.id.film_deteail_poster) ;
        mTitleTextView  = (TextView)findViewById( R.id.film_deteail_title_textview);
        mClassifyTextView = (TextView)findViewById(R.id.film_deteail_classes);
        mDateTextView = (TextView)findViewById(R.id.film_deteail_Release_time);
        mLastingTextView = (TextView)findViewById(R.id.film_deteail_lasting_time);
        mScoreTextView = (TextView)findViewById(R.id.film_deteail_score_num);
        mPeopleNumTextView = (TextView)findViewById(R.id.film_deteail_person_num);
        mBreifTextView = (TextView)findViewById(R.id.film_deteail_breif);
        mStar1 = (ImageView)findViewById(R.id.star_n_score_star1) ;
        mStar2 = (ImageView)findViewById(R.id.star_n_score_star2) ;
        mStar3 = (ImageView)findViewById(R.id.star_n_score_star3) ;
        mStar4 = (ImageView)findViewById(R.id.star_n_score_star4) ;
        mStar5 = (ImageView)findViewById(R.id.star_n_score_star5) ;

        mActorsRecyclerView = (RecyclerView) findViewById(R.id.film_deteail_actors_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横向滚动
        mActorsRecyclerView.setLayoutManager(layoutManager);

        mCommentsRecyclerView = (RecyclerView) findViewById(R.id.film_deteail_comments_recyclerview);
        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String Url = intent.getStringExtra(EXTRA_URL);
        sTagFlag = (FilmSimple.Source)intent.getSerializableExtra(EXTRA_SOURCE);
        if(sTagFlag == FilmSimple.Source.DOUBAN)
            FilmDetailService.startActionDouban(this,Url,mConnection);
        else if(sTagFlag == FilmSimple.Source.MAOYAN)
            FilmDetailService.startActionMaoYan(this,Url,mConnection);
    }

    /*活动启动必需*/
    public static Intent newIntent(Context packageContext, String URL,FilmSimple.Source source) {
        Intent intent = new Intent(packageContext, FilmDetailsActivity.class);
        intent.putExtra(EXTRA_URL, URL);
        intent.putExtra(EXTRA_SOURCE,source);
        return intent;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //此处更新UI
            String str = new String();
            switch (msg.obj.toString()){
                case "SUCCESS":
                    str = "搜索成功";
                    if(sTagFlag== FilmSimple.Source.MAOYAN)
                        ;
                    upDataUI();
                    break;
                case "NETWORK_ERROR"://网络异常
                    str = "网络异常";
                    break;
                default:

            }
            Toast.makeText(getApplication(),str,Toast.LENGTH_LONG).show();
        }
    };

    void upDataUI(){

        Glide.with(this)
                .load(mFilmSimple.getPic())
                .centerCrop()
                .into(mFilmPosterImageView);
        mTitleTextView.setText(mFilmSimple.getTitle());
        String classifies = new String();
        for(String str:mFilmSimple.getClassify()){
            classifies = classifies+str+'/';
        }
        mClassifyTextView.setText(classifies);
        mDateTextView.setText("上映时间："+mFilmSimple.getDate());
        mLastingTextView.setText("片长："+mFilmSimple.getLasting());
        mScoreTextView.setText(mFilmSimple.getScore()+"");
        mPeopleNumTextView.setText(mFilmSimple.getNum()+"人");
        mBreifTextView.setText(mFilmSimple.getBreif());
        StarTools.setStars(mFilmSimple.getScore(),mStar1,mStar2,mStar3,mStar4,mStar5);

        upDateActorList();
        upDateCommentList();
    }

    void upDateActorList(){
        mActorsAdapter = new ActorsAdapter(mFilmSimple.getActors());
        mActorsRecyclerView.setAdapter(mActorsAdapter);
    }

    void upDateCommentList(){
        mCommentsAdapter = new CommentsAdapter(mComments);
        mCommentsRecyclerView.setAdapter(mCommentsAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

}
