package com.example.wxy.beanfilm;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Bean.Score;
import com.example.wxy.beanfilm.Model.ChartTools;
import com.example.wxy.beanfilm.Model.CompareService;

import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class CompareActivity extends AppCompatActivity{

    private static String TAG = "CompareActivity";
    public static final String EXTRA_FILM1 = "com.example.wxy.beanfilm.CompareActivity.EXTRA_FILM1";
    public static final String EXTRA_FILM2 = "com.example.wxy.beanfilm.CompareActivity.EXTRA_FILM2";

    private CompareService mCompareService;
    private Score mScore1 = new Score();
    private Score mScore2 = new Score();

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接
            mCompareService = ((CompareService.CompareBinder)service).getService();
            mCompareService.setCallback(new CompareService.Callback() {
                @Override
                public void onDataChange(CompareService.State state, Score s1,Score s2) {
                    mScore1 = s1;
                    mScore2 = s2;
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

    ColumnChartView columnChartView;
    PieChartView mPieChartView;
    TextView mFilmTitleFirstTextView;
    TextView mFilmTitleSecondTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        Toast.makeText(this,"这是对比活动",Toast.LENGTH_SHORT).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.Compare_toolbar);//工具栏
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        Intent intent = getIntent();
        FilmSimple f1 = (FilmSimple) intent.getParcelableExtra("EXTRA_FILM1");
        FilmSimple f2 = (FilmSimple) intent.getParcelableExtra("EXTRA_FILM2");
        CompareService.startAction1(this,f1,f2,mConnection);

        columnChartView = (ColumnChartView) this.findViewById(R.id.Compare_ColumnChart);
        mPieChartView = (PieChartView)this.findViewById(R.id.Compare_PieChart) ;
        mFilmTitleFirstTextView = (TextView)this.findViewById(R.id.Compare_f1_title) ;
        mFilmTitleSecondTextView = (TextView)this.findViewById(R.id.Compare_f2_title) ;
        mFilmTitleFirstTextView.setText(f1.getTitle());
        mFilmTitleSecondTextView.setText(f2.getTitle());
    }

    /*活动启动必需*/
    public static Intent newIntent(Context packageContext, FilmSimple f1,FilmSimple f2) {
        Intent intent = new Intent(packageContext, CompareActivity.class);
        intent.putExtra("EXTRA_FILM1", f1);
        Log.d(TAG, "onCreate: EXTRA_FILM1"+f1.getUrl());
        intent.putExtra("EXTRA_FILM2", f2);
        return intent;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //此处更新UI
            String str = new String();
            switch (msg.obj.toString()){
                case "SUCCESS":
                    str = "搜索成功";
                    ChartTools.setColumnChartViewData(columnChartView,mScore1,mScore2);
                    ChartTools.setPieChartData(mPieChartView,mScore1,mScore2);
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
