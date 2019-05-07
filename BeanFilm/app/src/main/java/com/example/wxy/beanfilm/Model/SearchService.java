package com.example.wxy.beanfilm.Model;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Bean.FilmSimpleLab;
import com.example.wxy.beanfilm.Bean.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.wxy.beanfilm.Bean.FilmSimple.Source.DOUBAN;
import static com.example.wxy.beanfilm.Bean.FilmSimple.Source.MAOYAN;

public class SearchService extends IntentService {

    private static String TAG = "SearchService";
    private static final String SEARCH_KEY = "com.example.wxy.beanfilm.Model.search_key";
    private static final String WEB_TYPE = "com.example.wxy.beanfilm.Model.web_Type";
    private FilmSimple.Source TYPE = FilmSimple.Source.NULL;

    public enum SearchState{
        NULL,//初始状态
        SEARCH_SUCCESS,//搜索到记录
        NOT_EXISTENT,//无记录
        NETWORK_ERROR,//网络错误
    }
    public SearchState mSearchState = SearchState.NULL;
    //public FilmSimpleLab mFilmSimpleLab = FilmSimpleLab.get(getApplicationContext());

    private String mSearchkey = new String("");//要搜索的关键词

    private SearchBinder mBinder = new SearchBinder();
    public class SearchBinder extends Binder {
        public SearchService getService(){
            return SearchService.this;
        }
    }

    private Callback mCallback;

    public SearchService() {
        super("SearchService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public static void startActionSearch(Context context, String keyword,FilmSimple.Source source, ServiceConnection mConnection) {
        Intent intent = new Intent(context, SearchService.class);
        intent.putExtra(SEARCH_KEY , keyword);
        intent.putExtra(WEB_TYPE,source);
        context.startService(intent);
        context.bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mSearchkey = intent.getStringExtra(SEARCH_KEY);
        Log.d(TAG, "onHandleIntent: key"+mSearchkey);
        TYPE = (FilmSimple.Source) intent.getSerializableExtra(WEB_TYPE);
        switch (TYPE){
            case DOUBAN:
                SearchInDOUBAN();
                break;
            case MAOYAN:
                SearchInMAOYAN();
                break;
            default:
        }
    }

    private void SearchInDOUBAN(){
        final List<FilmSimple> filmSimples = new ArrayList();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    //FilmSimpleLab filmSimpleLab = FilmSimpleLab.get(getBaseContext());

                    Document doc = Jsoup.connect("https://www.douban.com/search?cat=1002&q="+mSearchkey)
                            .get();
                    Elements titleLinks = doc.select("div.result");
                    for(Element e : titleLinks){
                        String mTitle = e.select("div.title").select("a").text();//标题
                        String mUri = e. select("div.title").select("a").attr("href");//详情页面链接
                        String mPic = e.select("div.pic").select("img").attr("src");//图片链接
                        String mScoreSTR = e.select("div.title").select("span.rating_nums").text();
                        //Log.d(TAG, "run: "+mScoreSTR);
                        float mScore;
                        if(mScoreSTR.equals(""))
                            mScore = 0;
                        else
                            mScore = Float.parseFloat(mScoreSTR);//评分

                        String mInfo = e.select("div.title").select("span.subject-cast").text();//介绍
                      FilmSimple f = new FilmSimple();
                        f.setTitle(mTitle);
                        f.setUrl(mUri);
                        f.setPic(mPic);
                        f.setScore(mScore);
                        f.setInfo(mInfo);
                        f.setSource(DOUBAN);
                        filmSimples.add(f);
                        Log.d(TAG, "run: DOUBAN"+mTitle);
                    }
                    if(filmSimples.size()==0)
                        mSearchState = SearchState.NOT_EXISTENT;//搜索结果不存在
                    else
                        mSearchState = SearchState.SEARCH_SUCCESS;//搜索结果大于等于1
                    Log.d(TAG, "run: mSearchState"+mSearchState);
                    mCallback.onDataChangeDouBan(mSearchState,filmSimples);
                }catch (Exception e){
                    mSearchState = SearchState.NETWORK_ERROR;//网络错误
                    //List<FilmSimple> filmSimples = filmSimpleLab.getFilmSimples();
                    mCallback.onDataChangeDouBan(mSearchState,filmSimples);
                }
            }
        }.start();
    }

    private void SearchInMAOYAN(){
        final List<FilmSimple> filmSimples = new ArrayList();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    //FilmSimpleLab filmSimpleLab = FilmSimpleLab.get(getBaseContext());

                    Document doc = Jsoup.connect("https://maoyan.com/query?kw="+mSearchkey+"&type=0")
                            .get();
                    Elements Links1 = doc.select("dd");
                    for(Element e:Links1){
                        String mTitle = e.select("div[class$=movie-item-title]")
                                .select("a").text();//标题
                        String mUri = "https://maoyan.com"+e.select("div.movie-item")
                                .select("a")
                                .attr("href");//详情页面链接
                        String mPic = e.select("div.movie-poster")
                                .select("img")
                                .attr("data-src");//图片链接
                        String mScoreSTR = e.select("i.integer").text()
                                +e.select("i.fraction").text();
                        float mScore;
                        if(mScoreSTR.equals(""))
                            mScore = 0;
                        else
                            mScore = Float.parseFloat(mScoreSTR);//评分

                        String mInfo = e.select("div.movie-item-pub").text();//介绍
                        FilmSimple f = new FilmSimple();
                        f.setTitle(mTitle);
                        f.setUrl(mUri);
                        f.setPic(mPic);
                        f.setScore(mScore);
                        f.setInfo(mInfo);
                        f.setSource(MAOYAN);
                        Log.d(TAG, "run: MAOYAN"+mTitle);
                        //filmSimpleLab.addFilmSimleLab(f);
                        filmSimples.add(f);
                    }
                    //List<FilmSimple> filmSimples = filmSimpleLab.getFilmSimples();

                    if(filmSimples.size()==0)
                        mSearchState = SearchState.NOT_EXISTENT;//搜索结果不存在
                    else
                        mSearchState = SearchState.SEARCH_SUCCESS;//搜索结果大于等于1
                    Log.d(TAG, "run: mSearchState"+mSearchState);
                    mCallback.onDateChangeMaoYan(mSearchState,filmSimples);
                }catch (Exception e){
                    mSearchState = SearchState.NETWORK_ERROR;//网络错误
                    //List<FilmSimple> filmSimples = filmSimpleLab.getFilmSimples();
                    mCallback.onDateChangeMaoYan(mSearchState,filmSimples);
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
        void onDataChangeDouBan(SearchState newSearchState,List<FilmSimple> filmSimples);
        void onDateChangeMaoYan(SearchState newSearchState,List<FilmSimple> filmSimples);
    }
}
