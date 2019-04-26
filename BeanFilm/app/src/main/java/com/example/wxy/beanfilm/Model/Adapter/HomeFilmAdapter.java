package com.example.wxy.beanfilm.Model.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.FilmDetailsActivity;
import com.example.wxy.beanfilm.Model.StarTools;
import com.example.wxy.beanfilm.R;

import java.util.List;

public class HomeFilmAdapter extends RecyclerView.Adapter<HomeFilmAdapter.ViewHolder> {
    public List<FilmSimple> mFilmSimples;
    private String TAG = "HomeFilmAdapter";

    static class ViewHolder extends RecyclerView.ViewHolder{
        View FilmView;
        private FilmSimple mFilmSimple;

        private TextView mTitleTextView;
        private ImageView mPicImageView;
        private TextView mScoreTextView;
        private ViewGroup mParent;
        private LinearLayout mStarLinearLayout;

        public ViewHolder(View view, ViewGroup parent) {
            super(view);
            FilmView = view;
            mParent = parent;
            //实例化视图对象
            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_homefilm_name);
            mPicImageView = (ImageView)itemView.findViewById(R.id.list_item_homefilm_pic);
            mScoreTextView = (TextView)itemView.findViewById(R.id.list_item_homefilm_score);
            mStarLinearLayout = (LinearLayout)itemView.findViewById(R.id.list_item_homefilm_star);
        }

        public void bind(FilmSimple filmSimple) {
            mFilmSimple = filmSimple;
            //绑定数据
            mTitleTextView.setText(mFilmSimple.getTitle());
            Glide.with(mParent.getContext())
                    .load(mFilmSimple.getPic())
                    .centerCrop()
                    .into(mPicImageView);
            mScoreTextView.setText(mFilmSimple.getScore()+"");
            StarTools.setStars(mFilmSimple.getScore(),mStarLinearLayout);
        }
    }

    public HomeFilmAdapter(List<FilmSimple> filmSimples) {
        mFilmSimples = filmSimples;
        Log.d(TAG, "HomeFilmAdapter: "+mFilmSimples.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_homefilm,parent,false);
        final ViewHolder holder = new ViewHolder(view,parent);
        holder.FilmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position = holder.getAdapterPosition();
                holder.mParent.getContext().startActivity(FilmDetailsActivity.newIntent(v.getContext(),holder.mFilmSimple.getUrl(), FilmSimple.Source.DOUBAN));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FilmSimple filmSimple = mFilmSimples.get(position);
        holder.bind(filmSimple);
    }

    @Override
    public int getItemCount() {
        return mFilmSimples.size();
    }
    @Override
    public long getItemId(int position)

    { return position;}
}
