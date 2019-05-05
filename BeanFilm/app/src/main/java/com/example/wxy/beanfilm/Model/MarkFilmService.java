package com.example.wxy.beanfilm.Model;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.wxy.beanfilm.Bean.Comment;
import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Bean.MarkFilmSimple;
import com.example.wxy.beanfilm.Bean.RequestParameter;
import com.example.wxy.beanfilm.Bean.woffFont;

import org.litepal.crud.DataSupport;
import org.python.antlr.ast.Str;

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
public class MarkFilmService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String TAG = "MarkFilmService";
    private static final String MARKFILM = "com.example.wxy.beanfilm.Model.action.MARKFILM";

    // TODO: Rename parameters
    private static final String EXTRA_SOURCE = "com.example.wxy.beanfilm.Model.extra.SOURCE";
    private static final String EXTRA_DATE = "com.example.wxy.beanfilm.Model.extra.DATE";
    private static final String EXTRA_FILM = "com.example.wxy.beanfilm.Model.extra.FILM";
    private static final String EXTRA_USERID = "com.example.wxy.beanfilm.Model.extra.USERID";
    private static final String EXTRA_MYSTAR = "com.example.wxy.beanfilm.Model.extra.MYSTAR";
    private static final String EXTRA_STATE = "com.example.wxy.beanfilm.Model.extra.STATE";

    public MarkFilmService() {
        super("MarkFilmService");
    }

    public enum State{
        NULL,
        SUCCESS,
        NETWORK_ERROR
    }

    public State mState = State.NULL;

    private MarkFilmBinder mBinder = new MarkFilmBinder();
    public class MarkFilmBinder extends Binder {
        public MarkFilmService getService(){
            return MarkFilmService.this;
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
    public static void startActionMARKFILM(Context context, String param1, String param2,FilmSimple film,int userid,int mystar,String state, ServiceConnection mConnection) {
        Intent intent = new Intent(context, MarkFilmService.class);
        intent.setAction(MARKFILM);
        intent.putExtra(EXTRA_SOURCE, param1);//来源
        intent.putExtra(EXTRA_DATE, param2);//日期
        intent.putExtra(EXTRA_FILM,film);//电影
        intent.putExtra(EXTRA_USERID,userid);//用户id
        intent.putExtra(EXTRA_MYSTAR,mystar);//用户评分
        intent.putExtra(EXTRA_STATE,state);//想看或者看过
        context.startService(intent);
        context.bindService(intent,mConnection,BIND_AUTO_CREATE);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (MARKFILM.equals(action)) {
                final String source = intent.getStringExtra(EXTRA_SOURCE);
                final String date = intent.getStringExtra(EXTRA_DATE);
                final FilmSimple filmSimple = (FilmSimple) intent.getParcelableExtra(EXTRA_FILM);
                final int userid = intent.getIntExtra(EXTRA_USERID,-1);
                final int mystar = intent.getIntExtra(EXTRA_MYSTAR,-1);
                final String state = intent.getStringExtra(EXTRA_STATE);
                handleActionFoo(source, date,filmSimple,userid,mystar,state);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */

    final List<RequestParameter> parameter=new ArrayList<>();
    private void handleActionFoo(String source, String date,FilmSimple film,int userid,int mystar,String state) {
        // TODO: Handle action Foo
        MarkFilmSimple markfilm = new MarkFilmSimple(-1,film.getTitle(),film.getScore(),state,date,film.getBreif(),mystar,userid,film.getPic(),source,film.getUrl());
        DataSupport.deleteAll(MarkFilmSimple.class,"url = ?",markfilm.getUrl());//删除之前有的记录
        markfilm.save();//保存到本地数据库
        setRequestParameter("title",markfilm.getTitle());
        setRequestParameter("score",markfilm.getScore()+"");
        setRequestParameter("state",markfilm.getState());
        setRequestParameter("date",markfilm.getDate());
        setRequestParameter("info",markfilm.getInfo());
        setRequestParameter("myscore",markfilm.getMyscore()+"");
        setRequestParameter("userid",markfilm.getUserid()+"");
        setRequestParameter("pic",markfilm.getPic());
        setRequestParameter("source",markfilm.getSource());
        setRequestParameter("url",markfilm.getUrl());

        //创建一个FormBody.Builder
        FormBody.Builder builder=new FormBody.Builder();
        Log.d(TAG, "handleActionFoo: "+parameter.size());
        if (parameter!=null&&parameter.size()>0){
            for (RequestParameter p : parameter) {
                Log.d(TAG, "handleActionFoo: value"+p.getName()+p.getValue());
                builder.add(p.getName(),p.getValue());
            }
        }

        RequestBody requestBody =builder.build();
        HttpUtil.sendRequest("http://47.102.100.138:8080//SetMarkFilm",requestBody, new okhttp3.Callback(){

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
                switch (responseData){
                    case "true":
                        mState = State.SUCCESS;
                        break;
                    default:
                        mState = State.NETWORK_ERROR;
                        break;
                }
                mCallback.onDataChange(mState);
            }
        });
    }
    public void setRequestParameter(String name,String value){
        RequestParameter rp1=new RequestParameter(name,value);
        parameter.add(rp1);
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
