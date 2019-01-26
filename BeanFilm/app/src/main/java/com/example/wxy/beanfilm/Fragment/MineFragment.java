package com.example.wxy.beanfilm.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxy.beanfilm.Model.FilmSimple;
import com.example.wxy.beanfilm.Model.FilmSimpleLab;
import com.example.wxy.beanfilm.R;

import java.util.List;

/**
 * Created by WXY on 2019/1/24.
 */

public class MineFragment extends Fragment {

    private RecyclerView mFilmSimplesRecyclerView;
    private MineFilmAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_mine, container, false);

        mFilmSimplesRecyclerView = (RecyclerView) v.findViewById(R.id.MineFilm_recycler_view);
        mFilmSimplesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
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
        private TextView mScoreTextView;
        private TextView mDirectorTextView;
        private TextView mLeadActorsTextView;

        public MineFilmHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_minefilm, parent, false));
            itemView.setOnClickListener(this);
            //实例化视图对象
            mTitleTextView = (TextView)itemView.findViewById(R.id.MineFilm_recycler_view_Title);
            mScoreTextView = (TextView)itemView.findViewById(R.id.star_n_score_score);
            mDirectorTextView = (TextView)itemView.findViewById(R.id.MineFilm_recycler_view_directors);
            mLeadActorsTextView = (TextView)itemView.findViewById(R.id.MineFilm_recycler_view_actors);
        }

        public void bind(FilmSimple filmSimple) {
            mFilmSimple = filmSimple;
            //绑定数据
            mTitleTextView.setText(mFilmSimple.getTitle());
            mScoreTextView.setText(Float.toString(mFilmSimple.getScore()));
            mDirectorTextView.setText("导演："+mFilmSimple.getDirector());
            mLeadActorsTextView.setText("主演："+mFilmSimple.getActor1()+"/"+mFilmSimple.getActor2()+"/"+mFilmSimple.getActor3());
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
