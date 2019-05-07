package com.example.wxy.beanfilm.Model.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Fragment.SearchResultFragment;

public class SearchFilmFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabTilte;

    public SearchFilmFragmentStatePagerAdapter(FragmentManager fm, String[] tabTitle) {
        super(fm);
        this.tabTilte = tabTitle;
    }

    private SearchResultFragment f1 ;
    private SearchResultFragment f2;


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                f1 = new SearchResultFragment(FilmSimple.Source.DOUBAN);
                return f1;
            case 1:
                f2 = new SearchResultFragment(FilmSimple.Source.MAOYAN);
                return f2;
        }
        return null;
    }

    public SearchResultFragment getF1(){
        return f1;
    }
    public SearchResultFragment getF2(){
        return f2;
    }

    @Override
    public int getCount() {
        return tabTilte.length;
    }
}
