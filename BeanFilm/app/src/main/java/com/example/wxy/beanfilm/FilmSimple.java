package com.example.wxy.beanfilm;

/**
 * Created by WXY on 2019/1/24.
 */

public class FilmSimple {
    private String mId;
    private String mTitle;//电影名
    private float mScore;//评分
    private String mDirector;//导演
    private String mActor1;//主演1
    private String mActor2;//主演2
    private String mActor3;//主演3

    public void FilmSimple(){
        mId = mTitle = mDirector = mActor1 = mActor2 = mActor3 = null;
        mScore = 0;
    }

    public FilmSimple(String id, String title, float score, String director, String actor1) {
        mId = id;
        mTitle = title;
        mScore = score;
        mDirector = director;
        mActor1 = actor1;
    }

    public FilmSimple(String id, String title, float score, String director, String actor1, String actor2) {
        mId = id;
        mTitle = title;
        mScore = score;
        mDirector = director;
        mActor1 = actor1;
        mActor2 = actor2;
    }

    public FilmSimple(String id, String title, float score, String director, String actor1, String actor2, String actor3) {
        mId = id;
        mTitle = title;
        mScore = score;
        mDirector = director;
        mActor1 = actor1;
        mActor2 = actor2;
        mActor3 = actor3;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public float getScore() {
        return mScore;
    }

    public void setScore(float score) {
        mScore = score;
    }

    public String getDirector() {
        return mDirector;
    }

    public void setDirector(String director) {
        mDirector = director;
    }

    public String getActor1() {
        return mActor1;
    }

    public void setActor1(String actor1) {
        mActor1 = actor1;
    }

    public String getActor2() {
        return mActor2;
    }

    public void setActor2(String actor2) {
        mActor2 = actor2;
    }

    public String getActor3() {
        return mActor3;
    }

    public void setActor3(String actor3) {
        mActor3 = actor3;
    }
}
