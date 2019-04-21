package com.example.wxy.beanfilm.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.FilmDetailsActivity;
import com.example.wxy.beanfilm.Model.SearchFilmAdapter;
import com.example.wxy.beanfilm.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WXY on 2019/4/8.
 */

public class SearchResultFragment extends Fragment {

    private FilmSimple.Source TYPE = FilmSimple.Source.NULL;
    //private TextView mTextView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mFilmSimplesRecyclerView;
    public SearchFilmAdapter mAdapter;
    List<FilmSimple> mFilmSimples;
    private AppCompatActivity mAppCompatActivity;

    public SearchResultFragment(FilmSimple.Source type, List<FilmSimple> filmSimples) {
        this.TYPE = type;
        mFilmSimples = filmSimples;
    }

    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance(FilmSimple.Source type, List<FilmSimple> filmSimples) {
        Bundle args = new Bundle();
        SearchResultFragment fragment = new SearchResultFragment();
        args.putSerializable(ARG_PARAM1,type);
        args.putSerializable(ARG_PARAM2,(Serializable)filmSimples);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.TYPE = (FilmSimple.Source)getArguments().getSerializable(ARG_PARAM1);
            mFilmSimples = (List<FilmSimple>)getArguments().getSerializable(ARG_PARAM2);
        }
        mAppCompatActivity = (AppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_result, container, false);
        //mTextView = (TextView) v.findViewById(R.id.text);
        //mTextView.setText(TYPE+"");
        Bundle bundle =getArguments();
        String message = null;
        if(bundle!=null&&this.TYPE== FilmSimple.Source.NULL){
            this.TYPE = (FilmSimple.Source)getArguments().getSerializable(ARG_PARAM1);
            mFilmSimples = (List<FilmSimple>)getArguments().getSerializable(ARG_PARAM2);
        }

        mFilmSimplesRecyclerView = (RecyclerView) v.findViewById(R.id.searchresult_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFilmSimplesRecyclerView.setLayoutManager(layoutManager);
        updateUI();
        return v;
    }


    /*列表*/

    private void updateUI() {
        //FilmSimpleLab filmSimpleLab = FilmSimpleLab.get(this);
        //List<FilmSimple> filmSimples = filmSimpleLab.getFilmSimples();
        mAdapter = new SearchFilmAdapter(mFilmSimples,TYPE);
        mAdapter.setHasStableIds(true);
        mFilmSimplesRecyclerView.setAdapter(mAdapter);
    }
}
