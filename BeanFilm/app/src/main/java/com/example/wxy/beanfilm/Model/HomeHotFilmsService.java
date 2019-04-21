package com.example.wxy.beanfilm.Model;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.wxy.beanfilm.Bean.FilmSimple;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class HomeHotFilmsService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static String TAG = "HomeHotFilmsService";
    private static final String ACTION_FOO = "com.example.wxy.beanfilm.Model.action.FOO";

    public enum State{
        NULL,//初始状态
        SEARCH_SUCCESS,//搜索到记录
        NOT_EXISTENT,//无记录
        NETWORK_ERROR,//网络错误
    }
    public State mState = State.NULL;
    // TODO: Rename parameters
    //private static final String EXTRA_PARAM1 = "com.example.wxy.beanfilm.Model.extra.PARAM1";

    private HomeHotFilmsBinder mBinder = new HomeHotFilmsBinder();
    public class HomeHotFilmsBinder extends Binder {
        public HomeHotFilmsService getService(){
            return HomeHotFilmsService.this;
        }
    }

    private Callback mCallback;

    public HomeHotFilmsService() {
        super("HomeHotFilmsService");
    }

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
    public static void startActionFoo(Context context, ServiceConnection mConnection) {
        Intent intent = new Intent(context, HomeHotFilmsService.class);
        intent.setAction(ACTION_FOO);
        //intent.putExtra(EXTRA_PARAM1, param1);
        //intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
        context.bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                //final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                //final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo() {
        // TODO: Handle action Foo
        //throw new UnsupportedOperationException("Not yet implemented");
        final List<FilmSimple> filmSimples = new ArrayList();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    //FilmSimpleLab filmSimpleLab = FilmSimpleLab.get(getBaseContext());

                    Document doc = Jsoup.connect("https://movie.douban.com/")
                            .get();
                    Elements titleLinks = doc.select("li[class^=ui-slide-item]");
                    for(Element e : titleLinks){
                        String mTitle = e.select("li.title").text();//标题
                        String mUri = e. select("li.title").select("a").attr("href");//详情页面链接
                        String mPic = e.select("li.poster").select("img").attr("src");//图片链接
                        String mScoreSTR = e.select("li.rating").select("span.subject-rate").text();
                        Log.d(TAG, "run: 热门"+mTitle+mUri+mPic+mScoreSTR);
                        float mScore;
                        if(mScoreSTR.equals(""))
                            mScore = 0;
                        else
                            mScore = Float.parseFloat(mScoreSTR);//评分

                        FilmSimple f = new FilmSimple();
                        f.setTitle(mTitle);
                        f.setUrl(mUri);
                        f.setPic(mPic);
                        f.setScore(mScore);
                        //filmSimpleLab.addFilmSimleLab(f);
                        filmSimples.add(f);
                    }
                    //List<FilmSimple> filmSimples = filmSimpleLab.getFilmSimples();

                    if(filmSimples.size()==0)
                        mState = HomeHotFilmsService.State.NOT_EXISTENT;//搜索结果不存在
                    else
                        mState = HomeHotFilmsService.State.SEARCH_SUCCESS;//搜索结果大于等于1
                    //Log.d(TAG, "run: mSearchState"+mSearchState);
                    mCallback.onDataChange(mState,filmSimples);
                }catch (Exception e){
                    mState = HomeHotFilmsService.State.NETWORK_ERROR;//网络错误
                    //List<FilmSimple> filmSimples = filmSimpleLab.getFilmSimples();
                    mCallback.onDataChange(mState,filmSimples);
                }
            }
        }.start();
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
        void onDataChange(State newState, List<FilmSimple> filmSimples);
    }


}
