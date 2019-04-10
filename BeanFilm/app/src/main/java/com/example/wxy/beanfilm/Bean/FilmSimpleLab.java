package com.example.wxy.beanfilm.Bean;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WXY on 2019/1/24.
 */

public class FilmSimpleLab {//存储 一组 简单电影资料
    private static FilmSimpleLab sFilmSimpleLab;
    private List<FilmSimple> mFilmSimples;

    public static FilmSimpleLab get(Context context){//想要构造函数必须调用get
        if(sFilmSimpleLab == null){
            sFilmSimpleLab = new FilmSimpleLab(context);
        }
        return sFilmSimpleLab;
    }
    private FilmSimpleLab(Context context){//私有构造函数
        mFilmSimples = new ArrayList<>();
    }

    public void addFilmSimleLab(FilmSimple f){
        mFilmSimples.add(f);
    }

    public List<FilmSimple> getFilmSimples(){//获取整组电影资料
        return  mFilmSimples;
    }

    public FilmSimple getFilmSimple(String id){//获取特定id电影资料
        for(FilmSimple filmSimple : mFilmSimples){
            if(filmSimple.getId().equals(id))
                return filmSimple;
        }
        return null;
    }

    public void clear(){
        mFilmSimples.clear();
    }

}
