package com.example.wxy.beanfilm.Model.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.wxy.beanfilm.Bean.MarkFilmSimple;
import com.example.wxy.beanfilm.Fragment.VPFragment;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private String[] tabTilte;

    public MyFragmentStatePagerAdapter(FragmentManager fm, String[] tabTitle) {
        super(fm);
        this.tabTilte = tabTitle;
    }

    private VPFragment f1 ;
    private VPFragment f2;
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                f1 = new VPFragment("想看");
                return f1;
            case 1:
                f2 = new VPFragment("看过");
                return f2;
        }
        return null;
    }

    public VPFragment getF1(){
        return f1;
    }
    public VPFragment getF2(){
        return f2;
    }

    @Override
    public int getCount() {
        return tabTilte.length;
    }
}

