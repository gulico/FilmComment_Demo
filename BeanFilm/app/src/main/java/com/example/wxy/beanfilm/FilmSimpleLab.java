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

    public static FilmSimpleLab get(Context context){
        if(sFilmSimpleLab == null){
            sFilmSimpleLab = new FilmSimpleLab(context);
        }
        return sFilmSimpleLab;
    }
    private FilmSimpleLab(Context context){
        mFilmSimples = new ArrayList<>();
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
