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
import com.example.wxy.beanfilm.Bean.MarkFilmSimple;
import com.example.wxy.beanfilm.Bean.Score;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
                final FilmSimple FILM1 = (FilmSimple) intent.getParcelableExtra(EXTRA_FILM1);
                final FilmSimple FILM2 = (FilmSimple) intent.getParcelableExtra(EXTRA_FILM2);
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
    List<Score> mScores = new ArrayList<Score>();
    private void handleAction1(final FilmSimple f1, final FilmSimple f2) {

        // TODO: Handle action Foo
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    if(f1.getUrl().indexOf("douban")!=-1)
                        f1.setSource(FilmSimple.Source.DOUBAN);
                    else f1.setSource(FilmSimple.Source.MAOYAN);
                    if(f2.getUrl().indexOf("douban")!=-1)
                        f2.setSource(FilmSimple.Source.DOUBAN);
                    else f2.setSource(FilmSimple.Source.MAOYAN);
                    if(f1.getSource()==FilmSimple.Source.DOUBAN)
                        mScores.add(DouBan(f1));
                    else if (f1.getSource()==FilmSimple.Source.MAOYAN)
                        mScores.add(MaoYan(f1));
                    Log.d(TAG, "run: "+mScores.get(0));
                    if(f2.getSource()==FilmSimple.Source.DOUBAN)
                        mScores.add(DouBan(f2));
                    else if (f2.getSource()==FilmSimple.Source.MAOYAN)
                        mScores.add(MaoYan(f2));
                    Log.d(TAG, "run: "+mScores.get(1));
                    mState = CompareService.State.SUCCESS;
                    mCallback.onDataChange(mState,mScores);
                }catch (Exception e){
                    mState = CompareService.State.NETWORK_ERROR;//网络错误
                    mCallback.onDataChange(mState,mScores);
                }
            }
        }.start();
    }

    private Score DouBan(FilmSimple filmSimple){
        Score score = new Score();
        try {
            String URL = filmSimple.getUrl();
            Document doc = Jsoup.connect(URL)
                    .get();
            Log.d(TAG, "run: " + URL);
            String mScoreSTR = doc.select("div[class^=rating_self]").select("strong").text();//分数
            float mScore;
            if (mScoreSTR.equals(""))
                mScore = 0;
            else
                mScore = Float.parseFloat(mScoreSTR);

            String mNumSTR = doc.select("div.rating_sum").select("span").text();//评价人数
            int mNum;
            if (mNumSTR.equals(""))
                mNum = 0;
            else
                mNum = Integer.parseInt(mNumSTR);

            Elements startsLink = doc.select("div.ratings-on-weight").select("span.rating_per");//星级
            filmSimple.setScore(mScore);
            filmSimple.setNum(mNum);
            float[] stars = new float[5];
            int j = 0;
            for (Element e : startsLink) {
                String starSTR = e.text();
                Log.d(TAG, "run: " + starSTR);
                String[] strarray = starSTR.split("%");
                String star = new String();
                star = strarray[0];
                stars[j++] = Float.parseFloat(star);
                Log.d(TAG, "run: " + stars[j - 1]);
            }

            score.setStars(stars);
            score.setNum(mNum);
            score.setScore(mScore);
            score.setTitle(filmSimple.getTitle());
            score.setSource(filmSimple.getSource());
        } catch (Exception e){
        }
        return score;
    }

    private Score MaoYan(FilmSimple filmSimple){
        Score score = new Score();
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("filmid",filmSimple.getUrl())
                    .build();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://47.102.100.138:8080//GetMaoYanScore")
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            score = parseJSON(responseData);
            score.setTitle(filmSimple.getTitle());
            score.setSource(filmSimple.getSource());
        }catch (Exception e){
            e.printStackTrace();
        }
        return score;
    }
    /*解析Json数据*/
    private Score parseJSON(String jsonData) throws JSONException {
        Score score = new Score();
        JSONObject jsonObject = new JSONObject(jsonData);
        score.setScore(Float.parseFloat(jsonObject.getString("score")));
        score.setNum(Integer.parseInt(jsonObject.getString("num")));
        return score;
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
        void onDataChange(State state,List<Score> mScore);
    }

}
