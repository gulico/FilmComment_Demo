package com.example.wxy.beanfilm.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxy.beanfilm.LoginActivity;
import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.Bean.FilmSimpleLab;
import com.example.wxy.beanfilm.R;

import java.util.List;

/**
 * Created by WXY on 2019/1/24.
 */

public class MineFragment extends Fragment {

    private RecyclerView mFilmSimplesRecyclerView;
    private MineFilmAdapter mAdapter;
    private AppCompatActivity mAppCompatActivity;
    private LinearLayout mLinearLayoutUser;
    private TextView mTextViewUserName;
    private ImageView mImageViewUserIcon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_mine, container, false);
        mAppCompatActivity = ((AppCompatActivity) getActivity());

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.mine_toolbar);//工具栏
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)v.findViewById(R.id.mine_collapsing_toolbar);
        mAppCompatActivity = ((AppCompatActivity) getActivity());

        //将ToolBar设置为ActionBar
        setHasOptionsMenu(true);
        mAppCompatActivity.setSupportActionBar(toolbar);

        mTextViewUserName = (TextView)v.findViewById(R.id.mine_user_neme);
        mImageViewUserIcon = (ImageView)v.findViewById(R.id.mine_user_icon);
        //查看本地是否有可默认登录用户
        SharedPreferences userinfo = mAppCompatActivity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String useremail = userinfo.getString("email","");
        if(!useremail.equals("")){//有用户
            String username = userinfo.getString("name","");
            mTextViewUserName.setText(username);
            //mImageViewUserIcon设置头像
        }else{//无用户，需要登录
            mLinearLayoutUser = (LinearLayout)v.findViewById(R.id.mine_user);
            mLinearLayoutUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToLoginActivity = new Intent(mAppCompatActivity, LoginActivity.class);
                    startActivity(intentToLoginActivity);
                }
            });
        }

        mFilmSimplesRecyclerView = (RecyclerView) v.findViewById(R.id.MineFilm_recycler_view);
        mFilmSimplesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        return v;
    }

    /*工具栏选择*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.mine_toolbar,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Toast.makeText(mAppCompatActivity,"点击设置按钮！",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        FilmSimpleLab filmSimpleLab = FilmSimpleLab.get(getActivity());
        List<FilmSimple> filmSimples = filmSimpleLab.getFilmSimples();
        mAdapter = new MineFilmAdapter(filmSimples);
        mFilmSimplesRecyclerView.setAdapter(mAdapter);
    }

    private class MineFilmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private FilmSimple mFilmSimple;

        private TextView mTitleTextView;
        private TextView mDirectorTextView;
        private TextView mLeadActorsTextView;

        public MineFilmHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_minefilm, parent, false));
            itemView.setOnClickListener(this);
            //实例化视图对象
            mTitleTextView = (TextView)itemView.findViewById(R.id.MineFilm_recycler_view_Title);
            mDirectorTextView = (TextView)itemView.findViewById(R.id.MineFilm_recycler_view_directors);
            mLeadActorsTextView = (TextView)itemView.findViewById(R.id.MineFilm_recycler_view_actors);
        }

        public void bind(FilmSimple filmSimple) {
            mFilmSimple = filmSimple;
            //绑定数据
            mTitleTextView.setText(mFilmSimple.getTitle());
            //mScoreTextView.setText(Float.toString(mFilmSimple.getScore()));
            //mDirectorTextView.setText("导演："+mFilmSimple.getDirector());
            //mLeadActorsTextView.setText("主演："+mFilmSimple.getActor1()+"/"+mFilmSimple.getActor2()+"/"+mFilmSimple.getActor3());
        }

        @Override
        public void onClick(View v) {
            //列表点击事件
            Toast.makeText(getActivity(),
                    mFilmSimple.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class MineFilmAdapter extends RecyclerView.Adapter<MineFilmHolder> {
        private List<FilmSimple> mFilmSimples;

        public MineFilmAdapter(List<FilmSimple> filmSimples) {
            mFilmSimples = filmSimples;
        }

        @Override
        public MineFilmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MineFilmHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(MineFilmHolder holder, int position) {
            FilmSimple filmSimple = mFilmSimples.get(position);
            holder.bind(filmSimple);
        }

        @Override
        public int getItemCount() {
            return mFilmSimples.size();
        }
    }
}
