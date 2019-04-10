package com.example.wxy.beanfilm.Model;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.wxy.beanfilm.Bean.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.id.progress;

public class LoginService extends IntentService {

    private String TAG = "LoginService";
    private static final String LOGIN_USER = "com.example.wxy.beanfilm.Model.login_user";

    public enum LoginState{
        NULL,
        ACCOUNT_NOT_EXISTENT ,
        PASSWORD_ERROR,
        NETWORK_ERROR,
        LOGIN_SUCCESS,
    }
    public LoginState loginState = LoginState.NULL;//当前登录状态

    private User mLoginUser = new User();

    private LoginBinder mBinder = new LoginBinder();
    public class LoginBinder extends Binder{
        public LoginService getService(){
            return LoginService.this;
        }
    }

    private Callback mCallback;

    public LoginService() {
        super("LoginService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mLoginUser = (User) intent.getSerializableExtra(LOGIN_USER);
        RequestBody requestBody = new FormBody.Builder()
                .add("email",mLoginUser.getEmail())
                .build();
        HttpUtil.sendRequest("http://47.102.100.138:8080//Login",requestBody, new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                //默认网络异常
                loginState = LoginState.NETWORK_ERROR;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                String responseData = response.body().string();
                //Log.d(TAG, "onResponse: "+responseData);
                loginState = parseJSON(responseData);
                switch (loginState){
                    case LOGIN_SUCCESS:
                        saveLocalAccount();
                    default:
                        break;
                }
                mCallback.onDataChange(loginState);
            }
        });
    }

    /*解析Json数据*/
    private LoginState parseJSON(String jsonData){
        Gson gson = new Gson();
        User user = new User();
        user = gson.fromJson(jsonData, User.class);
        try{
            if(user.getId() == 0){
                return LoginState.ACCOUNT_NOT_EXISTENT;
            }
            else if(!user.getPassword().equals(mLoginUser.getPassword())){
                return LoginState.PASSWORD_ERROR;
            }
            else{
                mLoginUser = user;
                return LoginState.LOGIN_SUCCESS;
            }
        }catch (Exception e){
            System.out.println(e);
            return  LoginState.NETWORK_ERROR;
        }
    }

    /*保存账户信息到本地*/
    private void saveLocalAccount(){
        SharedPreferences.Editor editor = getSharedPreferences("account",MODE_PRIVATE).edit();
        editor.putInt("id",mLoginUser.getId());
        editor.putString("name",mLoginUser.getName());
        editor.putString("email",mLoginUser.getEmail());
        editor.putString("password",mLoginUser.getPassword());
        editor.putString("ico",mLoginUser.getIco());
        editor.apply();
    }

    /*服务销毁：回收资源*/
    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    /*服务启动必需*/
    public static Intent newIntent(Context packageContext, User user) {
        Intent intent = new Intent(packageContext, LoginService.class);
        intent.putExtra(LOGIN_USER, user);
        return intent;
    }

    /*回调接口等*/
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public static interface Callback {
        void onDataChange(LoginState newloginState);
    }

}

