package com.example.demo.entity;

import com.alibaba.fastjson.JSON;

public class FontScore {
    private float score ;//官方评分
    private int num;

    public FontScore(){
        score = 0;
        num=0;
    }

    public FontScore(float score, int num) {
        this.score = score;
        this.num = num;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
