package com.example.wxy.beanfilm.Bean;

import java.io.Serializable;

public class Score  implements Serializable {

    private String mTitle;//电影名
    private float mScore;//评分
    private float[] mStars = new float[5];
    private int mNum;//评价人数
    private FilmSimple.Source mSource;// 来源分类

    public Score(float score, float[] stars, int num, FilmSimple.Source source) {
        mScore = score;
        mStars = stars;
        mNum = num;
        mSource = source;
    }

    public Score() {
        mTitle = null;
        mScore = 0;
        for(int i = 0 ; i < 5;i++)
            mStars[i] = 0;
        mNum = 0;
        mSource = FilmSimple.Source.DOUBAN;
    }

    public Score(String title, float score, float[] stars, int num, FilmSimple.Source source) {
        mTitle = title;
        mScore = score;
        mStars = stars;
        mNum = num;
        mSource = source;
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

    public float[] getStars() {
        return mStars;
    }

    public void setStars(float[] stars) {
        mStars = stars;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public FilmSimple.Source getSource() {
        return mSource;
    }

    public void setSource(FilmSimple.Source source) {
        mSource = source;
    }
}
