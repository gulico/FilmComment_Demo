package com.example.wxy.beanfilm.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Model.Adapter.SearchFilmAdapter;
import com.example.wxy.beanfilm.R;
import com.example.wxy.beanfilm.SearchActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by WXY on 2019/4/8.
 */

public class SearchResultFragment extends Fragment {

    private String TAG = "SearchResultFragment" ;

    private FilmSimple.Source TYPE = FilmSimple.Source.NULL;
    private RecyclerView mFilmSimplesRecyclerView;
    public SearchFilmAdapter mAdapter;
    List<FilmSimple> mFilmSimples = new ArrayList<>();

    public SearchResultFragment(FilmSimple.Source type) {
        this.TYPE = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_result, container, false);
        mFilmSimplesRecyclerView = (RecyclerView) v.findViewById(R.id.searchresult_recycler_view);
        mFilmSimplesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    public void setSaerchList(List<FilmSimple> filmSimples) {
        mFilmSimples.clear();
        mFilmSimples = filmSimples;
        Log.d(TAG, "setSaerchList: 调用更新");
        updateUI();
    }

    public void setSearchListBan(){
        for(int i=0;i<mAdapter.mChecked.size();i++){
            SearchFilmAdapter.CheckedState c = mAdapter.mChecked.get(i);
            if(c== SearchFilmAdapter.CheckedState.UNCHECKED) {
                mAdapter.mChecked.set(i, SearchFilmAdapter.CheckedState.BAN);
                Handler handler = new Handler();
                final int finalI = i;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyItemChanged(finalI);
                    }
                });
            }
        }
    }

    public void setSearchListUnBan(){
        for(int i=0;i<mAdapter.mChecked.size();i++){
            SearchFilmAdapter.CheckedState c = mAdapter.mChecked.get(i);
            if(c== SearchFilmAdapter.CheckedState.BAN) {
                mAdapter.mChecked.set(i, SearchFilmAdapter.CheckedState.UNCHECKED);
                Handler handler = new Handler();
                final int finalI = i;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyItemChanged(finalI);
                    }
                });
            }
        }
    }
    /*列表*/
    private void updateUI() {
        mAdapter = new SearchFilmAdapter(mFilmSimples,TYPE,(SearchActivity) getActivity());
        mAdapter.setHasStableIds(true);
        mFilmSimplesRecyclerView.setAdapter(mAdapter);
    }
}
