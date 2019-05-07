package com.example.wxy.beanfilm.Model;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.wxy.beanfilm.Bean.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RegisterService extends IntentService {
    private String TAG = "RegisterService";
    private static final String ACTION_REGISTER = "com.example.wxy.beanfilm.Model.action.REGISTER";
    private static final String EXTRA_REGISTERUSER = "com.example.wxy.beanfilm.Model.extra.REGISTERUSER";

    public enum State{
        NULL,//初始状态
        SUCCESS,//注册成功
        HAS_EMAIL,//账户已存在
        NETWORK_ERROR,//网络错误
        ERROR//注册失败
    }
    public State mState = State.NULL;

    public RegisterService() {
        super("RegisterService");
    }

    private RegisterBinder mBinder = new RegisterBinder();
    public class RegisterBinder extends Binder {
        public RegisterService getService(){
            return RegisterService.this;
        }
    }

    private Callback mCallback;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionRegister(Context context, User user, ServiceConnection mConnection) {
        Intent intent = new Intent(context, RegisterService.class);
        intent.setAction(ACTION_REGISTER);
        intent.putExtra(EXTRA_REGISTERUSER, user);
        context.startService(intent);
        context.bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_REGISTER.equals(action)) {
                final User param1 = (User)intent.getSerializableExtra(EXTRA_REGISTERUSER);
                handleActionRegister(param1);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionRegister(User user) {
        // TODO: Handle action Foo
        //throw new UnsupportedOperationException("Not yet implemented");
        RequestBody requestBody = new FormBody.Builder()
                .add("email",user.getEmail())
                .add("name",user.getName())
                .add("password",user.getPassword())
                .build();
        HttpUtil.sendRequest("http://47.102.100.138:8080//Register",requestBody, new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                //默认网络异常
                mState = State.NETWORK_ERROR;
                System.out.println("错误！"+e);
                mCallback.onDataChange(mState);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                String responseData = response.body().string();
                Log.d(TAG, "onResponse: "+responseData);
                switch (responseData){
                    case "\"该邮箱已被注册\"":
                        mState = State.HAS_EMAIL;
                        break;
                    case "\"注册成功\"":
                        mState = State.SUCCESS;
                        break;
                    case "\"注册失败\"":
                        mState = State.ERROR;
                        break;
                    default:
                        break;
                }
                mCallback.onDataChange(mState);
            }
        });
    }

    /*服务销毁：回收资源*/
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /*回调接口等*/
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public static interface Callback {
        void onDataChange(State state);
    }
}
