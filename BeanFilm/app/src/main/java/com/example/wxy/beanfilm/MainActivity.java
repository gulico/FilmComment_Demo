package com.example.wxy.beanfilm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static int sTagFlag = 2;//首页为1，个人主页为2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*碎片显示管理*/
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new MineFragment();//暂时先显示个人页面
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        /**/
    }
}
