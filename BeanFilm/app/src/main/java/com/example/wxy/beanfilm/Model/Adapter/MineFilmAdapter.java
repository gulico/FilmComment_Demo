package com.example.wxy.beanfilm.Model.Adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wxy.beanfilm.Bean.MarkFilmSimple;
import com.example.wxy.beanfilm.Model.StarTools;
import com.example.wxy.beanfilm.R;

import java.util.List;

public class MineFilmAdapter extends Adapter<MineFilmAdapter.MineFilmHolder> {
    private String TAG = "MineFilmAdapter";
    private List<MarkFilmSimple> mFilmSimples;

    class MineFilmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewGroup mParent;
        private MarkFilmSimple mFilmSimple;

        private TextView mTitleTextView;
        private ImageView mPicImageView;
        private TextView mInfoTextView;
        private TextView mDateTextView;

        private LinearLayout mStarLinearLayout;
        private LinearLayout mMyStarLinearLayout;

        public MineFilmHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_minefilm, parent, false));
            itemView.setOnClickListener(this);
            mParent = parent;
            //实例化视图对象
            mTitleTextView = (TextView)itemView.findViewById(R.id.MineFilm_recycler_view_Title);
            mPicImageView = (ImageView)itemView.findViewById(R.id.MineFilm_recycler_view_Pic);
            mDateTextView = (TextView)itemView.findViewById(R.id.MineFilm_recycler_view_date);
            mInfoTextView = (TextView) itemView.findViewById(R.id.MineFilm_recycler_view_info);

            mStarLinearLayout = (LinearLayout)itemView.findViewById(R.id.MineFilm_recycler_view_star_n_score);
            mMyStarLinearLayout = (LinearLayout)itemView.findViewById(R.id.MineFilm_recycler_view_my_star_n_score);
        }

        public void bind(MarkFilmSimple filmSimple) {
            mFilmSimple = filmSimple;
            //绑定数据
            mTitleTextView.setText(mFilmSimple.getTitle());
            mDateTextView.setText(mFilmSimple.getDate());
            Glide.with(mParent.getContext())
                    .load(mFilmSimple.getPic())
                    .centerCrop()
                    .into(mPicImageView);
            StarTools.setStars(mFilmSimple.getScore(),mStarLinearLayout);
            if(mFilmSimple.getState().equals("想看")){
                mMyStarLinearLayout.setVisibility(View.GONE);
            }else {
                StarTools.setStars(mFilmSimple.getMyscore(), mMyStarLinearLayout);
            }
            mInfoTextView.setText("简介："+mFilmSimple.getInfo());
        }

        @Override
        public void onClick(View v) {
            //列表点击事件
            Toast.makeText(mParent.getContext(),
                    mFilmSimple.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public MineFilmAdapter(List<MarkFilmSimple> filmSimples) {
        mFilmSimples = filmSimples;
        Log.d(TAG, "MineFilmAdapter: 大小"+mFilmSimples.size());
    }

    @Override
    public MineFilmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MineFilmHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MineFilmHolder holder, int position) {
        MarkFilmSimple filmSimple = mFilmSimples.get(position);
        holder.bind(filmSimple);
    }

    @Override
    public int getItemCount() {
        return mFilmSimples.size();
    }
}
