package com.example.wxy.beanfilm.Model.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wxy.beanfilm.Bean.FilmSimple;
import com.example.wxy.beanfilm.FilmDetailsActivity;
import com.example.wxy.beanfilm.R;
import com.example.wxy.beanfilm.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WXY on 2019/4/16.
 */

public class SearchFilmAdapter extends RecyclerView.Adapter<SearchFilmAdapter.ViewHolder> {
    public List<FilmSimple> mFilmSimples = new ArrayList<FilmSimple>();
    public List<CheckedState> mChecked = new ArrayList<CheckedState>();
    FilmSimple.Source TYPE;
    SearchActivity searchActivity;
    private String TAG = "SearchFilmAdapter";
    public enum CheckedState{
        CHECKED,//被选择
        UNCHECKED,//未被选择
        BAN,//禁止
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private String TAG = "SearchFilmAdapter.ViewHolder";
        View FilmView;
        private FilmSimple mFilmSimple;

        private TextView mTitleTextView;
        private ImageView mPicImageView;
        private TextView mInfoTextView;
        private ViewGroup mParent;
        private CheckBox mCheckBox;

        public ViewHolder(View view, ViewGroup parent) {
            super(view);
            FilmView = view;
            mParent = parent;
            //实例化视图对象
            Log.d(TAG, "ViewHolder: 数据");
            mTitleTextView = (TextView)itemView.findViewById(R.id.searchresult_recycler_view_Title);
            mPicImageView = (ImageView)itemView.findViewById(R.id.searchresult_bk);
            mInfoTextView = (TextView)itemView.findViewById(R.id.searchresult_recycler_view_info);
            mCheckBox = (CheckBox)itemView.findViewById(R.id.searchresult_check_box);
        }

        public void bind(FilmSimple filmSimple,CheckedState checkedState) {
            mFilmSimple = filmSimple;
            Log.d(TAG, "bind: 绑定数据"+mFilmSimple.getTitle());
            //绑定数据
            mTitleTextView.setText(mFilmSimple.getTitle());
            mInfoTextView.setText(mFilmSimple.getInfo());
            Glide.with(mParent.getContext())
                    .load(mFilmSimple.getPic())
                    .centerCrop()
                    .into(mPicImageView);
            if(checkedState == CheckedState.CHECKED) {
                mCheckBox.setChecked(true);
                Log.d(TAG, "bind: 更新CHECKD"+mFilmSimple.getTitle());
            }
            else if(checkedState == CheckedState.UNCHECKED ) {
                mCheckBox.setChecked(false);
                Log.d(TAG, "bind: 更新UNCHECKED"+mFilmSimple.getTitle());
            }
            else if(checkedState == CheckedState.BAN) {
                mCheckBox.setEnabled(false);
                Log.d(TAG, "bind: 更新BAN"+mFilmSimple.getTitle());
            }
        }
    }

    public SearchFilmAdapter(List<FilmSimple> filmSimples, FilmSimple.Source type, SearchActivity searchActivity) {
        mFilmSimples = filmSimples;
        for(int i=0;i<mFilmSimples.size();i++)//初始化checkbox状态记录
            mChecked.add(CheckedState.UNCHECKED);
        TYPE = type;
        this.searchActivity = searchActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_searchresult,parent,false);
        final ViewHolder holder = new ViewHolder(view,parent);
        holder.FilmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mParent.getContext().startActivity(FilmDetailsActivity.newIntent(v.getContext(),holder.mFilmSimple.getUrl(),TYPE));
            }
        });
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int position = holder.getAdapterPosition();
                Toast.makeText(parent.getContext(),
                        " clicked!"+position, Toast.LENGTH_SHORT)
                        .show();
                if(b&&searchActivity.mCheckedFilmSiple.size()<2){
                    holder.mFilmSimple.setSource(TYPE);
                    searchActivity.mCheckedFilmSiple.add(holder.mFilmSimple);

                    mChecked.set(position,CheckedState.CHECKED);
                    if(searchActivity.mCheckedFilmSiple.size()>1){//选择之后大于1
                        searchActivity.mSearchFilmFragmentStatePagerAdapter.getF1().setSearchListBan();
                        searchActivity.mSearchFilmFragmentStatePagerAdapter.getF2().setSearchListBan();
                    }
                }else if(!b){
                    searchActivity.mCheckedFilmSiple.remove(holder.mFilmSimple);
                    mChecked.set(position,CheckedState.UNCHECKED);
                    if(searchActivity.mCheckedFilmSiple.size()==1){//取消之后==1
                        searchActivity.mSearchFilmFragmentStatePagerAdapter.getF1().setSearchListUnBan();
                        searchActivity.mSearchFilmFragmentStatePagerAdapter.getF2().setSearchListUnBan();
                    }
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FilmSimple filmSimple = mFilmSimples.get(position);
        holder.bind(filmSimple,mChecked.get(position));
        if(mChecked.get(position) == CheckedState.CHECKED)
            holder.mCheckBox.setChecked(true);
        else if(mChecked.get(position) == CheckedState.UNCHECKED )
            holder.mCheckBox.setChecked(false);
        else if(mChecked.get(position) == CheckedState.BAN)
            holder.mCheckBox.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return mFilmSimples.size();
    }
    @Override
    public long getItemId(int position)

    { return position;}
}
