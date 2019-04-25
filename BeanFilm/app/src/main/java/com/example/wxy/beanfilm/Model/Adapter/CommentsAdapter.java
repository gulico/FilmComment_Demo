package com.example.wxy.beanfilm.Model.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxy.beanfilm.Bean.Comment;
import com.example.wxy.beanfilm.R;

import java.util.List;

/**
 * Created by WXY on 2019/4/16.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsHolder> {
    private List<Comment> mComments;

    static class CommentsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Comment mComment;

        private TextView mNameTextView;
        private TextView mContextTextView;
        private TextView mDateTextView;
        private ViewGroup mParent;

        public CommentsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_comment, parent, false));
            itemView.setOnClickListener(this);
            mParent = parent;
            //实例化视图对象
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_comment_author);
            mContextTextView = (TextView) itemView.findViewById(R.id.list_item_comment_context);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_comment_Date);
        }

        public void bind(Comment comment) {
            mComment = comment;
            //绑定数据
            mNameTextView.setText(mComment.getAuthor());
            mContextTextView.setText(mComment.getContext());
            mDateTextView.setText(mComment.getDate());
        }

        @Override
        public void onClick(View v) {
            //列表点击事件
            Toast.makeText(mParent.getContext(),
                    mComment.getAuthor() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public CommentsAdapter(List<Comment> Comments) {
        mComments = Comments;
    }

    @Override
    public CommentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CommentsHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(CommentsHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }
}