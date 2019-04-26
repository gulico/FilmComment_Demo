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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxy.beanfilm.Bean.User;
import com.example.wxy.beanfilm.Model.RegisterService;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "RegisterActivity";
    TextView mNameTextView;
    TextView mEmailTextView;
    TextView mPassWordView;
    Button mRegisterButton;

    private RegisterService mRegisterService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接
            mRegisterService = ((RegisterService.RegisterBinder)service).getService();
            mRegisterService.setCallback(new RegisterService.Callback() {
                @Override
                public void onDataChange(RegisterService.State newRegisterState) {
                    //此处依然不能更新UI
                    Message msg = new Message();
                    msg.obj = newRegisterState;
                    Registerhandler.sendMessage(msg);
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
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        mNameTextView = (TextView)findViewById(R.id.Register_name);
        mEmailTextView = (TextView)findViewById(R.id.Register_email);
        mPassWordView = (TextView)findViewById(R.id.Register_password);
        mRegisterButton = (Button)findViewById(R.id.Register_button);

        mRegisterButton.setOnClickListener(this);
    }

    /*活动启动必需*/
    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, RegisterActivity.class);
        return intent;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Register_button:
                User user = CheckUser();
                Log.d(TAG, "onClick: 点击注册");
                if(user != null)
                    RegisterService.startActionRegister(this,user,mConnection);
                break;
        }
    }

    private User CheckUser(){
        User user = new User();
        String name = mNameTextView.getText().toString();
        String email = mEmailTextView.getText().toString();
        String password = mPassWordView.getText().toString();
        if(name.equals("")||email.equals("")||password.equals("")){
            Log.d(TAG, "CheckUser: 有空的");
            return null;
        }else{
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            return user;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler Registerhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //此处更新UI
            String str = new String();
            switch (msg.obj.toString()){
                case"HAS_EMAIL":
                    str = "该邮箱已被注册";
                    break;
                case "ERROR":
                    str = "注册失败";
                    break;
                case "NETWORK_ERROR":
                    str = "网络错误";
                    break;
                case "SUCCESS":
                    str = "注册成功";
                    break;
                default:
                    str = "未知错误";
            }
            unbindService(mConnection);
            Toast.makeText(getApplication(),str,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
