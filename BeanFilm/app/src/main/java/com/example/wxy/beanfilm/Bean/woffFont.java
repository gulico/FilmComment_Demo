package com.example.wxy.beanfilm.Bean;

public class woffFont {
    private String mUrl;//字体地址
    private String mScoreInteger;//评分整数
    private String mScoreDecimal;//评分小数
    private String mNumInteger;//人数整数
    private String mNumDecimal;//人数小数

    public woffFont(String url, String scoreInteger, String scoreDecimal, String numInteger, String numDecimal) {
        mUrl = url;
        mScoreInteger = scoreInteger;
        mScoreDecimal = scoreDecimal;
        mNumInteger = numInteger;
        mNumDecimal = numDecimal;
    }

    public woffFont() {
        mUrl = mScoreInteger = mScoreDecimal = mNumInteger = mNumDecimal = null;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getScoreInteger() {
        return mScoreInteger;
    }

    public void setScoreInteger(String scoreInteger) {
        mScoreInteger = scoreInteger;
    }

    public String getScoreDecimal() {
        return mScoreDecimal;
    }

    public void setScoreDecimal(String scoreDecimal) {
        mScoreDecimal = scoreDecimal;
    }

    public String getNumInteger() {
        return mNumInteger;
    }

    public void setNumInteger(String numInteger) {
        mNumInteger = numInteger;
    }

    public String getNumDecimal() {
        return mNumDecimal;
    }

    public void setNumDecimal(String numDecimal) {
        mNumDecimal = numDecimal;
    }
}
