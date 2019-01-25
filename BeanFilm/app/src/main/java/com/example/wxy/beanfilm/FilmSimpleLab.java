package com.example.wxy.beanfilm;

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
        //获取数据
        for(int i=0;i<100;i++){
            FilmSimple filmSimple = new FilmSimple();
            filmSimple.setId(Integer.toString(i));
            filmSimple.setTitle("电影名"+i);
            filmSimple.setDirector("某某某"+i);
            filmSimple.setActor1("张三三"+i);
            filmSimple.setActor2("李四四"+i);
            filmSimple.setActor3("王五五"+i);
            mFilmSimples.add(filmSimple);
        }

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

}
