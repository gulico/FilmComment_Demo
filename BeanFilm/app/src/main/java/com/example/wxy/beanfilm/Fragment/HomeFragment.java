package com.example.wxy.beanfilm.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Bean.User;
import com.example.wxy.beanfilm.Model.HomeFilmAdapter;
import com.example.wxy.beanfilm.Model.LoginService;
import com.example.wxy.beanfilm.Model.SearchService;
import com.example.wxy.beanfilm.R;
import com.example.wxy.beanfilm.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by WXY on 2019/1/24.
 */

public class HomeFragment extends Fragment {

    private String  TAG = "HomeFragment";
    private AppCompatActivity mAppCompatActivity;

    private RecyclerView mHomeFilmRecyclerView;
    private HomeFilmAdapter mHomeFilmAdapter;

    private List<FilmSimple> mFilmSimples = new ArrayList<>();

    public HomeFragment(List<FilmSimple> newFilmSimples){
        mFilmSimples = newFilmSimples;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_home, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toobar_frame_home);

        mAppCompatActivity = ((AppCompatActivity) getActivity());

        //将ToolBar设置为ActionBar
        setHasOptionsMenu(true);
        mAppCompatActivity.setSupportActionBar(toolbar);

        mHomeFilmRecyclerView = (RecyclerView) v.findViewById(R.id.frame_home_hotnow_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);//两行
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);//横向滚动
        mHomeFilmRecyclerView.setLayoutManager(gridLayoutManager);
        upDateHotnowList(mFilmSimples);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_toolbar,menu);

        MenuItem searchItem = menu.findItem(R.id.home_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        //响应查询指令。
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {//提交查询内容
                //startSearchSercive(query);
                Intent intent = new Intent(mAppCompatActivity, SearchActivity.class);
                intent.putExtra("search_key",query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {//查询文字变化
                Log.d(TAG, "QueryTextChange: " + newText);
                return false;
            }
        });
    }

    public void upDateHotnowList(List<FilmSimple> f){
        mHomeFilmAdapter = new HomeFilmAdapter(f);
        mHomeFilmRecyclerView.setAdapter(mHomeFilmAdapter);
    }


}
