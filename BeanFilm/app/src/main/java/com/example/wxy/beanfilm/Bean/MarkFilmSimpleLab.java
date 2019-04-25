package com.example.wxy.beanfilm.Bean;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class MarkFilmSimpleLab {
    private static MarkFilmSimpleLab sFilmSimpleLab;
    private List<MarkFilmSimple> mFilmSimples;

    public static MarkFilmSimpleLab get(Context context){//想要构造函数必须调用get
        if(sFilmSimpleLab == null){
            sFilmSimpleLab = new MarkFilmSimpleLab(context);
        }
        return sFilmSimpleLab;
    }
    private MarkFilmSimpleLab(Context context){//私有构造函数
        mFilmSimples = new ArrayList<>();
    }

    public void addMarkFilmSimpleLab(MarkFilmSimple f){
        mFilmSimples.add(f);
    }

    public List<MarkFilmSimple> getMarkFilmSimples(){//获取整组电影资料
        return  mFilmSimples;
    }

    public void clear(){
        mFilmSimples.clear();
    }
}
