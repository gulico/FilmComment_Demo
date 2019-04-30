package com.example.wxy.beanfilm.Bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class MarkFilmSimple extends DataSupport implements Serializable {
    private int id = 0;//电影id
    private String title = "";//标题
    private float score ;//官方评分
    private String state = "";//想看或者看过
    private String date = "";//标记日期
    private String info = "";//简介
    private int myscore;//我的评分
    private int userid;//用户id
    private String pic = "";//海报链接
    private String source = "";//来源：豆瓣，猫眼
    private String url = "";

    public MarkFilmSimple(int id, String title, float score, String state, String date, String info, int myscore, int userid, String pic, String source,String url) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.state = state;
        this.date = date;
        this.info = info;
        this.myscore = myscore;
        this.userid = userid;
        this.pic = pic;
        this.source = source;
        this.url = url;
    }

    public MarkFilmSimple() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getMyscore() {
        return myscore;
    }

    public void setMyscore(int myscore) {
        this.myscore = myscore;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
