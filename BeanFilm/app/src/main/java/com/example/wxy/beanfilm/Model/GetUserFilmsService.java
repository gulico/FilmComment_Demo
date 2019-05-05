package com.example.wxy.beanfilm.Model;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.wxy.beanfilm.Bean.MarkFilmSimple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class GetUserFilmsService extends IntentService {

    private String TAG = "GetUserFilmsService";
    public enum State{
        NULL,
        SUCCESS,
        NETWORK_ERROR
    }

    public State mState = State.NULL;

    private static final String ACTION_GETFILMBYUSERID = "com.example.wxy.beanfilm.Model.action.GETFILMBYUSERID";

    // TODO: Rename parameters
    //private static final String EXTRA_USERID = "com.example.wxy.beanfilm.Model.extra.USERID";

    public GetUserFilmsService() {
        super("GetUserFilmsService");
    }

    private GetUserFilmsBinder mBinder = new GetUserFilmsBinder();
    public class GetUserFilmsBinder extends Binder {
        public GetUserFilmsService getService(){
            return GetUserFilmsService.this;
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
    public static void startActionGetUserFilms(Context context, ServiceConnection mConnection) {
        Intent intent = new Intent(context, GetUserFilmsService.class);
        intent.setAction(ACTION_GETFILMBYUSERID);
        context.startService(intent);
        context.getApplicationContext().bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GETFILMBYUSERID.equals(action)) {
                SharedPreferences userinfo = getSharedPreferences("account", Context.MODE_PRIVATE);
                String id = userinfo.getInt("id",-1)+"";
                handleActionGetUserFilms(id);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetUserFilms(String userid) {
        RequestBody requestBody = new FormBody.Builder()
                .add("userid",userid)
                .build();
        HttpUtil.sendRequest("http://47.102.100.138:8080//GetMarkFilm",requestBody, new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                //默认网络异常
                mState = State.NETWORK_ERROR;
                mCallback.onDataChange(mState,filmSimples);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                String responseData = response.body().string();
                //Log.d(TAG, "onResponse: "+responseData);
                mState = parseJSON(responseData);
                switch (mState){
                    case SUCCESS:
                        saveUserFilms();//保存到本地数据库
                    default:
                        break;
                }
                mCallback.onDataChange(mState,filmSimples);
            }
        });
    }

    List<MarkFilmSimple> filmSimples = new ArrayList<>();
    /*解析Json数据*/
    private State parseJSON(String jsonData){
        Gson gson = new Gson();
        filmSimples = gson.fromJson(jsonData,new TypeToken<List<MarkFilmSimple>>(){}.getType());
        return State.SUCCESS;
    }

    private void saveUserFilms(){
        DataSupport.deleteAll(MarkFilmSimple.class);//清空表
        for(MarkFilmSimple film:filmSimples){
            film.save();
        }
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
        void onDataChange(State state,List<MarkFilmSimple> filmSimples);
    }
}
