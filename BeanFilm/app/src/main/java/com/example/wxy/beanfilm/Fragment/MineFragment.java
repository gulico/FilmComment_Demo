package com.example.wxy.beanfilm.Fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxy.beanfilm.Bean.MarkFilmSimple;
import com.example.wxy.beanfilm.Bean.MarkFilmSimpleLab;
import com.example.wxy.beanfilm.LoginActivity;
import com.example.wxy.beanfilm.Model.Adapter.MyFragmentStatePagerAdapter;
import com.example.wxy.beanfilm.Model.GetUserFilmsService;
import com.example.wxy.beanfilm.R;
import com.example.wxy.beanfilm.SettingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WXY on 2019/1/24.
 */

public class MineFragment extends Fragment {

    private String TAG = "MineFragment";

    private AppCompatActivity mAppCompatActivity;
    private LinearLayout mLinearLayoutUser;
    private TextView mTextViewUserName;
    private ImageView mImageViewUserIcon;
    private static boolean isFirst  = true;

    List<MarkFilmSimple> mFilmSimples = new ArrayList<>();
    private GetUserFilmsService mGetUserFilmsService;
    private ServiceConnection mGetUserFilmsConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接
            mGetUserFilmsService = ((GetUserFilmsService.GetUserFilmsBinder)service).getService();
            mGetUserFilmsService.setCallback(new GetUserFilmsService.Callback() {
                @Override
                public void onDataChange(GetUserFilmsService.State newState, List<MarkFilmSimple> filmSimples) {
                    //此处依然不能更新UI
                    mFilmSimples = filmSimples;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ViewPager mViewPager1;
    private TabLayout mTabLayout;
    private String[] tabTitle = {"想看","看过"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_mine, container, false);
        mAppCompatActivity = ((AppCompatActivity) getActivity());

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.mine_toolbar);//工具栏
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)v.findViewById(R.id.mine_collapsing_toolbar);
        mAppCompatActivity = ((AppCompatActivity) getActivity());

        //将ToolBar设置为ActionBar
        setHasOptionsMenu(true);
        mAppCompatActivity.setSupportActionBar(toolbar);

        mTextViewUserName = (TextView)v.findViewById(R.id.mine_user_neme);
        mImageViewUserIcon = (ImageView)v.findViewById(R.id.mine_user_icon);

        initViews(v);
        //initData();
        mLinearLayoutUser = (LinearLayout)v.findViewById(R.id.mine_user);
        updataUserUI();
        initData();
        return v;
    }

    /*工具栏选择*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.mine_toolbar,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Toast.makeText(mAppCompatActivity,"点击设置按钮！",Toast.LENGTH_SHORT).show();
                Intent intentToSettingActivity = new Intent(mAppCompatActivity, SettingActivity.class);
                startActivityForResult(intentToSettingActivity,2);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                updataUserUI();
                break;
            case 2:
                clearUserUI();
                break;
        }
    }

    private void initViews(View rootView) {
        mViewPager1 = (ViewPager) rootView.findViewById(R.id.mViewPager1);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.mTabLayout);
    }
    MyFragmentStatePagerAdapter mMyFragmentStatePagerAdapter ;//想看看过适配器
    private void initData() {
        for (int i=0; i<tabTitle.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTitle[i]));
        }
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#7CCD7C"));
        mTabLayout.setTabTextColors(Color.GRAY, Color.parseColor("#41bd56"));

        mMyFragmentStatePagerAdapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(),tabTitle);
        mViewPager1.setAdapter(mMyFragmentStatePagerAdapter);
        mViewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager1.setCurrentItem(tab.getPosition());
                int position = mViewPager1.getCurrentItem();
                if(position==0)
                    mMyFragmentStatePagerAdapter.getF1().getLocalData();
                else if(position==1)
                    mMyFragmentStatePagerAdapter.getF2().getLocalData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void updataUserUI(){
        //查看本地是否有可默认登录用户
        SharedPreferences userinfo = mAppCompatActivity.getSharedPreferences("account", Context.MODE_PRIVATE);
        String useremail = userinfo.getString("email","");
        if(!useremail.equals("")){//有用户
            mLinearLayoutUser.setClickable(false);
            String username = userinfo.getString("name","");
            mTextViewUserName.setText(username);
            //initData();
            GetUserFilmsService.startActionGetUserFilms(getActivity(),mGetUserFilmsConnection);
            //mImageViewUserIcon设置头像
        }else if(!mLinearLayoutUser.hasOnClickListeners()){//无用户，需要登录
            mLinearLayoutUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToLoginActivity = new Intent(mAppCompatActivity, LoginActivity.class);
                    startActivityForResult(intentToLoginActivity,1);
                }
            });

            mTextViewUserName.setText("请登录");
        }
    }

    void clearUserUI(){
        SharedPreferences userinfo = mAppCompatActivity.getSharedPreferences("account", Context.MODE_PRIVATE);
        String useremail = userinfo.getString("email","");
        if(!mLinearLayoutUser.isClickable() && useremail.equals("")){//无用户，需要登录
            mLinearLayoutUser.setClickable(true);
            mLinearLayoutUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToLoginActivity = new Intent(mAppCompatActivity, LoginActivity.class);
                    startActivityForResult(intentToLoginActivity,1);
                }
            });
        }
        mTextViewUserName.setText("请登录");
        mMyFragmentStatePagerAdapter.getF1().onTypeClick(new ArrayList<MarkFilmSimple>());
        mMyFragmentStatePagerAdapter.getF2().onTypeClick(new ArrayList<MarkFilmSimple>());
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
                    str = "获取标记成功";
                    //updateFilmLabNUI();
                    Log.d(TAG, "handleMessage: 大小"+mFilmSimples.size());
                    mMyFragmentStatePagerAdapter.getF1().onTypeClick(mFilmSimples);
                    mMyFragmentStatePagerAdapter.getF2().onTypeClick(mFilmSimples);
                    break;
                case "NETWORK_ERROR"://网络异常
                    str = "网络异常";
                    break;
                default:

            }
            Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mGetUserFilmsConnection);
    }
}
