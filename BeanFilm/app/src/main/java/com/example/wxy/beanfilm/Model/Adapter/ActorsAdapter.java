package com.example.wxy.beanfilm.Model.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wxy.beanfilm.Bean.Actor;
import com.example.wxy.beanfilm.R;

import java.util.List;

/**
 * Created by WXY on 2019/4/16.
 */

public class ActorsAdapter extends RecyclerView.Adapter<ActorsAdapter.ActorsHolder> {
    private List<Actor> mActors;

    static class ActorsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Actor mActor;

        private TextView mNameTextView;
        private TextView mRoleTextView;
        private ImageView mPicImageView;
        private ViewGroup mParent;

        public ActorsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_actor, parent, false));
            itemView.setOnClickListener(this);
            mParent = parent;
            //实例化视图对象
            mNameTextView = (TextView)itemView.findViewById(R.id.list_item_actor_name);
            mRoleTextView = (TextView)itemView.findViewById(R.id.list_item_actor_role);
            mPicImageView = (ImageView)itemView.findViewById(R.id.list_item_actor_pic);
        }

        public void bind(Actor actor) {
            mActor = actor;
            //绑定数据
            mNameTextView.setText(mActor.getName());
            mRoleTextView.setText(mActor.getRole());
            Glide.with(mParent.getContext())
                    .load(mActor.getPic())
                    .centerCrop()
                    .into(mPicImageView);
        }

        @Override
        public void onClick(View v) {
            //列表点击事件
            Toast.makeText(mParent.getContext(),
                    mActor.getName() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public ActorsAdapter(List<Actor> Actors) {
        mActors = Actors;
    }

    @Override
    public ActorsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ActorsHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(ActorsHolder holder, int position) {
        Actor actor = mActors.get(position);
        holder.bind(actor);
    }

    @Override
    public int getItemCount() {
        return mActors.size();
    }
}
