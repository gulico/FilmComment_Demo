package com.example.wxy.beanfilm;

import android.annotation.SuppressLint;
import android.content.ComponentName;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxy.beanfilm.Bean.User;
import com.example.wxy.beanfilm.Model.LoginService;
import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "LoginActivity";
    private EditText mEditTextLoginId;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    private TextView mTextViewRegisterBotton;
    private TextView mTextViewForgetPassword;
    private LoginService mLoginService;
    private LoginService.LoginState mState;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接
            mLoginService = ((LoginService.LoginBinder)service).getService();
            mLoginService.setCallback(new LoginService.Callback() {
                @Override
                public void onDataChange(LoginService.LoginState newloginState) {
                    //此处依然不能更新UI
                    mState = newloginState;
                    Message msg = new Message();
                    msg.obj = newloginState;
                    loginhandler.sendMessage(msg);
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
        setContentView(R.layout.activity_login);

        /*返回键设置*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        mEditTextLoginId = (EditText)findViewById(R.id.login_id);
        mEditTextPassword = (EditText)findViewById(R.id.login_password);
        mButtonLogin = (Button)findViewById(R.id.login_button);
        mTextViewRegisterBotton = (TextView)findViewById(R.id.register_botton);
        mTextViewForgetPassword = (TextView)findViewById(R.id.forgetpassword_botton);

        mButtonLogin.setOnClickListener(this);
        mTextViewRegisterBotton.setOnClickListener(this);
        mTextViewForgetPassword.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                Log.d(TAG, "onClick: 按下登录");
                startLoginSercive();
                break;
            case R.id.register_botton:
                startActivity(RegisterActivity.newIntent(this));
                break;
            case R.id.forgetpassword_botton:
                break;
        }
    }

    /*启动登录服务*/
    public void startLoginSercive(){
        String email = mEditTextLoginId.getText().toString();
        String password = mEditTextPassword.getText().toString();
        User loginuser = new User();
        loginuser.setEmail(email);
        loginuser.setPassword(password);
        Intent toLoginService = LoginService.newIntent(this,loginuser);
        startService(toLoginService);
        bindService(toLoginService,mConnection,BIND_AUTO_CREATE);
    }

    @SuppressLint("HandlerLeak")
    private Handler loginhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //此处更新UI
            String str = new String();
            switch (msg.obj.toString()){
                case"ACCOUNT_NOT_EXISTENT":
                    str = "用户不存在";
                    break;
                case "PASSWORD_ERROR":
                    str = "密码错误";
                    break;
                case "NETWORK_ERROR":
                    str = "网络错误";
                    break;
                case "LOGIN_SUCCESS":
                    str = "登录成功";
                    Intent comebackToMine = new Intent();
                    comebackToMine.putExtra("LoginState",mState);
                    setResult(RESULT_OK,comebackToMine);
                    unbindService(mConnection);
                    finish();
                    break;
                default:
                    str = "未知错误";
            }
            //msg.obj.toString().equals(LoginService.LoginState.ACCOUNT_NOT_EXISTENT.name());
            Toast.makeText(getApplication(),str,Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
