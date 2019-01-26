package com.example.wxy.beanfilm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wxy.beanfilm.Fragment.HomeFragment;
import com.example.wxy.beanfilm.Fragment.MineFragment;

public class MainActivity extends AppCompatActivity {

    private static boolean sTagFlag = true;//首页为true，个人主页为false
    private LinearLayout mLinearLayoutHomeTag;
    private LinearLayout mLinearLayoutMineTag;
    private TextView mTextViewHome;
    private TextView mTextViewMine;
    private ImageView mImageViewHome;
    private ImageView mImageViewMine;

    private FragmentManager fm;
    private Fragment currentFragment;
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

        if (currentFragment == null) {
            currentFragment = new HomeFragment();//暂时先显示个人页面
            fm.beginTransaction()
                    .add(R.id.fragment_container, currentFragment)
                    .commit();
        }

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

    /*替换碎片*/
    private void replaceFragment(String tag) {
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).commit();
        }
        currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment == null) {
            switch (tag) {
                case "home":
                    currentFragment = new HomeFragment();
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
}
