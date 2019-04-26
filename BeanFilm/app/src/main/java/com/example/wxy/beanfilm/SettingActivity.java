package com.example.wxy.beanfilm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    LinearLayout mSignOutLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Setting_toolbar);//工具栏
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        mSignOutLinearLayout = (LinearLayout) findViewById(R.id.Setting_sign_out);//登出

        SharedPreferences userinfo = getSharedPreferences("account", Context.MODE_PRIVATE);
        String useremail = userinfo.getString("email","");
        if(!useremail.equals("")) {//有用户
            mSignOutLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = getSharedPreferences("account",MODE_PRIVATE).edit();
                    editor.clear();//清空账户
                    editor.commit();
                    Toast.makeText(SettingActivity.this,"退出成功",Toast.LENGTH_SHORT).show();
                    Intent comebackToMine = new Intent();
                    setResult(RESULT_OK,comebackToMine);
                    finish();
                }
            });
        }else{
            mSignOutLinearLayout.setVisibility(View.GONE);
        }

    }
}
