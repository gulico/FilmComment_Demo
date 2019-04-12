package com.example.wxy.beanfilm.Bean;

/**
 * Created by WXY on 2019/4/12.
 */

public class Comment {
    private String author;
    private String date;//评价日期
    private String context;//评论内容

    public Comment() {
        author = date = context = null;
    }

    public Comment(String author, String date, String context, String level) {
        this.author = author;
        this.date = date;
        this.context = context;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
