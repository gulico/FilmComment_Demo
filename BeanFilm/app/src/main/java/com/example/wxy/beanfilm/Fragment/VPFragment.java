package com.example.wxy.beanfilm.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wxy.beanfilm.Bean.MarkFilmSimple;
import com.example.wxy.beanfilm.Bean.MarkFilmSimpleLab;
import com.example.wxy.beanfilm.Model.Adapter.MineFilmAdapter;
import com.example.wxy.beanfilm.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VPFragment extends Fragment {

    private String TAG = "VPFragment";
    private RecyclerView mFilmSimplesRecyclerView;
    private MineFilmAdapter mAdapter;
    private String mState = "";
    public List<MarkFilmSimple> mFilms = new ArrayList<>();

    public VPFragment(String state){
        mState = state;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_vpfragment, container, false);
        mFilmSimplesRecyclerView = (RecyclerView) v.findViewById(R.id.MineFilm_recycler_view);
        mFilmSimplesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateFilmListUI();
        return v;
    }

    public void onTypeClick(List<MarkFilmSimple> filmSimples) {
        mFilms.clear();
        for(MarkFilmSimple e:filmSimples){
            if(e.getState().equals(mState)){
                mFilms.add(e);
            }
        }
        Collections.reverse(mFilms);
        updateFilmListUI();
    }

    public void getLocalData(){
        mFilms.clear();
        List<MarkFilmSimple> films = DataSupport.findAll(MarkFilmSimple.class);//从而本地数据库读取
        for(MarkFilmSimple e:films){
            if(e.getState().equals(mState)){
                mFilms.add(e);
            }
        }
        Collections.reverse(mFilms);
        updateFilmListUI();
    }

    private void updateFilmListUI() {
        mAdapter = new MineFilmAdapter(mFilms);
        mFilmSimplesRecyclerView.setAdapter(mAdapter);
    }

}
