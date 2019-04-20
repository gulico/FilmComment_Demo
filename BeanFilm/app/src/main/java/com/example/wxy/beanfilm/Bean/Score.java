package com.example.wxy.beanfilm.Bean;

import java.io.Serializable;

public class Score  implements Serializable {

    private float mScore;//评分
    private float[] mStars = new float[5];
    private int mNum;//评价人数

    public Score(float score, float[] stars, int num) {
        mScore = score;
        mStars = stars;
        mNum = num;
    }

    public Score() {
        mScore = 0;
        for(int i = 0 ; i < 5;i++)
            mStars[i] = 0;
        mNum = 0;
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
}
