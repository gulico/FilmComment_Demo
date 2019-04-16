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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FilmDetailService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_DOUBAN = "com.example.wxy.beanfilm.Model.action.DOUBAN";
    private static final String ACTION_MAOYAN = "com.example.wxy.beanfilm.Model.action.MAOYAN";

    // TODO: Rename parameters
    private static final String EXTRA_URL = "com.example.wxy.beanfilm.Model.extra.URL";

    public enum State{
        NULL,
        SUCCESS,
        NETWORK_ERROR
    }

    public State mState = State.NULL;

    public FilmDetailService() {
        super("FilmDetailService");
    }

    private FilmDetailBinder mBinder = new FilmDetailBinder();
    public class FilmDetailBinder extends Binder {
        public FilmDetailService getService(){
            return FilmDetailService.this;
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
    public static void startActionDouban(Context context, String param1,ServiceConnection mConnection) {
        Intent intent = new Intent(context, FilmDetailService.class);
        intent.setAction(ACTION_DOUBAN);
        intent.putExtra(EXTRA_URL, param1);
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
    public static void startActionMaoYan(Context context, String param1, ServiceConnection mConnection) {
        Intent intent = new Intent(context, FilmDetailService.class);
        intent.setAction(ACTION_MAOYAN);
        intent.putExtra(EXTRA_URL, param1);
        context.startService(intent);
        context.bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOUBAN.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_URL);
                handleActionDouban(param1);
            } else if (ACTION_MAOYAN.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_URL);
                handleActionMaoYan(param1);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDouban(final String URL) {
        // TODO: Handle action Foo
        //throw new UnsupportedOperationException("Not yet implemented");
        final FilmSimple filmSimple = new FilmSimple();
        final List<Comment> comments = new ArrayList<Comment>();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    Document doc = Jsoup.connect(URL)
                            .get();
                    String mTitle = doc.select("div[id=mainpic]").select("img").attr("alt");//标题
                    String mUri = URL;//详情页面链接
                    String mPic = doc.select("div[id=mainpic]").select("img").attr("src");//图片链接
                    String mBreif = doc.select("div.related-info").select("span").first().text();//简介
                    String mScoreSTR = doc.select("div[class^=rating_self]").select("strong").text();//分数
                    float mScore;
                    if(mScoreSTR.equals(""))
                        mScore = 0;
                    else
                        mScore = Float.parseFloat(mScoreSTR);//评分

                    String mNumSTR = doc.select("div.rating_sum").select("span").text();//评价人数
                    int mNum;
                    if(mNumSTR.equals(""))
                        mNum = 0;
                    else
                        mNum = Integer.parseInt(mNumSTR);

                    String mDate = doc.select("div[id=info]").select("span[property=v:initialReleaseDate]").text();//上映时间
                    String mLasting = doc.select("div[id=info]").select("span[property=v:runtime]").text();//片长
                    Elements ClassifyLinks = doc.select("div[id=info]").select("span[property=v:genre]");
                    List<String> classifies = new ArrayList<String>();//分类集合
                    for(Element e:ClassifyLinks){
                        String classidfy = e.text();
                        classifies.add(classidfy);
                    }
                    List<Actor> actors = new ArrayList<Actor>();
                    Elements ActorLinks = doc.select("li.celebrity");
                    for(Element e:ActorLinks){//影人列表
                        Actor a = new Actor();
                        String name = e.select("a.name").text();
                        String role = e.select("span.role").text();
                        String picSTR = e.select("div.avatar").attr("style");
                        String[] strarray = picSTR.split("\\(|\\)");
                        String pic = new String();
                        if(strarray.length>1)
                            pic = strarray[1];
                        a.setName(name);
                        a.setRole(role);
                        a.setPic(pic);
                        actors.add(a);
                    }
                    Elements CommitLinks = doc.select("div.comment");
                    for(Element e:CommitLinks){//评论列表
                        Comment c = new Comment();
                        String author = e.select("span.comment-info").select("a").text();
                        String date = e.select("span.comment-time").text();
                        String commitcontext = e.select("span.short").text();
                        c.setAuthor(author);
                        c.setDate(date);
                        c.setContext(commitcontext);
                        comments.add(c);
                    }

                    filmSimple.setTitle(mTitle);
                    filmSimple.setUrl(mUri);
                    filmSimple.setPic(mPic);
                    filmSimple.setScore(mScore);
                    filmSimple.setBreif(mBreif );
                    filmSimple.setNum(mNum);
                    filmSimple.setDate(mDate);
                    filmSimple.setLasting(mLasting);
                    filmSimple.setClassify(classifies);
                    filmSimple.setActors(actors);

                    mState = FilmDetailService.State.SUCCESS;
                    mCallback.onDataChange(mState,filmSimple,comments);
                }catch (Exception e){
                    mState = FilmDetailService.State.NETWORK_ERROR;//网络错误
                    mCallback.onDataChange(mState,filmSimple,comments);
                }
            }
        }.start();
    }

    /**
     * Handle action Baz in the provided background thread wit the provided
     * parameters.
     */
    private void handleActionMaoYan(String param1) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
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
        void onDataChange(State state,FilmSimple filmSimple,List<Comment> comments);
    }
}
