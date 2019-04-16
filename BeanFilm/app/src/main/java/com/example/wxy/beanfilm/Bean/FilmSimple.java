package com.example.wxy.beanfilm.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by WXY on 2019/1/24.
 */

public class FilmSimple implements Parcelable {

    protected FilmSimple(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mPic = in.readString();
        mInfo = in.readString();
        mBreif = in.readString();
        mUrl = in.readString();
        mScore = in.readFloat();
        mNum = in.readInt();
        mDate = in.readString();
        mLasting = in.readString();
        mClassify = in.createStringArrayList();
    }

    public static final Creator<FilmSimple> CREATOR = new Creator<FilmSimple>() {
        @Override
        public FilmSimple createFromParcel(Parcel in) {
            return new FilmSimple(in);
        }

        @Override
        public FilmSimple[] newArray(int size) {
            return new FilmSimple[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mPic);
        dest.writeString(mInfo);
        dest.writeString(mBreif);
        dest.writeString(mUrl);
        dest.writeFloat(mScore);
        dest.writeInt(mNum);
        dest.writeString(mDate);
        dest.writeString(mLasting);
        dest.writeStringList(mClassify);
    }

    public enum Source{//来源分类
        NULL,
        DOUBAN,
        MAOYAN
    }

    private String mId;
    private String mTitle;//电影名
    private String mPic;//海报
    private String mInfo;//简介
    private String mBreif;//详细简介
    private String mUrl;//详情页链接
    private Source mSource;// 来源分类
    private float mScore;//评分
    private int mNum;//评价人数
    private String mDate;//上映时间
    private String mLasting;//片长
    private List<Actor> mActors;//影人列表
    private List<String> mClassify;//电影分类

    public FilmSimple(){
        mId = mTitle  =  mPic = mInfo = mUrl = mDate = mLasting = null;
        mSource = Source.NULL;
        mScore = mNum = 0;
    }

    public List<String> getClassify() {
        return mClassify;
    }

    public void setClassify(List<String> classify) {
        mClassify = classify;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getLasting() {
        return mLasting;
    }

    public void setLasting(String lasting) {
        mLasting = lasting;
    }

    public List<Actor> getActors() {
        return mActors;
    }

    public void setActors(List<Actor> actors) {
        mActors = actors;
    }

    public String getBreif() {
        return mBreif;
    }

    public void setBreif(String breif) {
        mBreif = breif;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public String getPic() {
        return mPic;
    }

    public void setPic(String pic) {
        mPic = pic;
    }

    public String getInfo() {
        return mInfo;
    }

    public void setInfo(String minfo) {
        this.mInfo = minfo;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Source getSource() {
        return mSource;
    }

    public void setSource(Source source) {
        mSource = source;
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
}
