package com.example.wxy.beanfilm.Model;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.wxy.beanfilm.Bean.Actor;
import com.example.wxy.beanfilm.Bean.Comment;
import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Bean.Score;

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
public class CompareService extends IntentService {
    private static String TAG = "CompareService";
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_1 = "com.example.wxy.beanfilm.Model.action.1";
    //private static final String ACTION_FILM2 = "com.example.wxy.beanfilm.Model.action.FILM2";

    // TODO: Rename parameters
    private static final String EXTRA_FILM1 = "com.example.wxy.beanfilm.Model.extra.FILM1";
    private static final String EXTRA_FILM2 = "com.example.wxy.beanfilm.Model.extra.FILM2";

    //private FilmSimple FILM1 = new FilmSimple();
    //private FilmSimple FILM2 = new FilmSimple();

    public enum State{
        NULL,
        SUCCESS,
        NETWORK_ERROR
    }

    public State mState = State.NULL;

    public CompareService() {
        super("CompareService");
    }

    private CompareBinder mBinder = new CompareBinder();
    public class CompareBinder extends Binder {
        public CompareService getService(){
            return CompareService.this;
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
    public static void startAction1(Context context, FilmSimple param1, FilmSimple param2,ServiceConnection mConnection) {
        Intent intent = new Intent(context, CompareService.class);
        intent.setAction(ACTION_1 );
        intent.putExtra(EXTRA_FILM1, param1);
        intent.putExtra(EXTRA_FILM2, param2);
        context.startService(intent);
        context.bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
   /* public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, CompareService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }*/

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_1.equals(action)) {
                final FilmSimple FILM1 = (FilmSimple) intent.getSerializableExtra(EXTRA_FILM1);
                final FilmSimple FILM2 = (FilmSimple) intent.getSerializableExtra(EXTRA_FILM2);
                handleAction1(FILM1,FILM2);
            }
            /*else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }*/
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleAction1(final FilmSimple f1, final FilmSimple f2) {
        final Score s1 = new Score();
        final Score s2 = new Score();
        // TODO: Handle action Foo
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{

                    for(int i = 0;i<2;i++){
                        String URL = i==0?f1.getUrl():f2.getUrl();
                        Document doc = Jsoup.connect(URL)
                                .get();
                        Log.d(TAG, "run: "+URL);
                        //String mTitle = doc.select("div[id=mainpic]").select("img").attr("alt");//标题
                        //String mUri = URL;//详情页面链接
                        String mScoreSTR = doc.select("div[class^=rating_self]").select("strong").text();//分数
                        float mScore;
                        if(mScoreSTR.equals(""))
                            mScore = 0;
                        else
                            mScore = Float.parseFloat(mScoreSTR);

                        String mNumSTR = doc.select("div.rating_sum").select("span").text();//评价人数
                        int mNum;
                        if(mNumSTR.equals(""))
                            mNum = 0;
                        else
                            mNum = Integer.parseInt(mNumSTR);

                        Elements startsLink = doc.select("div.ratings-on-weight").select("span.rating_per");//星级
                        (i==0?s1:s2).setScore(mScore);
                        (i==0?s1:s2).setNum(mNum);
                        float[] stars = new float[5];
                        int j = 0;
                        for(Element e:startsLink){
                            String starSTR = e.text();
                            Log.d(TAG, "run: "+starSTR);
                            String[] strarray = starSTR.split("%");
                            String star = new String();
                            star = strarray[0];
                            stars[j++] = Float.parseFloat(star);
                            Log.d(TAG, "run: "+stars[j-1]);
                        }
                        (i==0?s1:s2).setStars(stars);
                    }

                    mState = CompareService.State.SUCCESS;
                    mCallback.onDataChange(mState,s1,s2);
                }catch (Exception e){
                    mState = CompareService.State.NETWORK_ERROR;//网络错误
                    mCallback.onDataChange(mState,s1,s2);
                }
            }
        }.start();
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    /*private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
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
        void onDataChange(State state,Score s1,Score s2);
    }

}
