package com.example.wxy.beanfilm.Model;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wxy.beanfilm.FilmDetailsActivity;
import com.example.wxy.beanfilm.R;

public class InfoDialog extends Dialog{

    public int score = 0;
    private InfoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public static class Builder {
        private String TAG = "InfoDialog.Builder";
        private View mLayout;

        //private ImageView mIcon;
        private LinearLayout mStars;
        private TextView mTitle;
        private TextView mMessage;
        private Button mButton;

        private View.OnClickListener mButtonClickListener;
        private View.OnTouchListener mStarsTouchListener;

        private InfoDialog mDialog;
        FilmDetailsActivity context;

        public Builder(Context context) {
            mDialog = new InfoDialog(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = (FilmDetailsActivity) context;

            mLayout = inflater.inflate(R.layout.dialog_info, null);

            //mIcon = mLayout.findViewById(R.id.dialog_icon);
            mStars = mLayout.findViewById(R.id.dialog_star);
            mTitle = mLayout.findViewById(R.id.dialog_title);
            mMessage = mLayout.findViewById(R.id.dialog_message);
            mButton = mLayout.findViewById(R.id.dialog_button);
        }

        /**
         * 使用资源ID方式引入icon
         */
        public Builder setIcon(int resId) {
            //mIcon.setImageResource(resId);
            return this;
        }

        /**
         * 使用bitmap方式引入icom
         */
        public Builder setIcon(Bitmap bitmap) {
            //mIcon.setImageBitmap(bitmap);
            return this;
        }

        public Builder setTitle(@NonNull String title) {
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
            return this;
        }

        public Builder setMessage(@NonNull String message) {
            mMessage.setText(message);
            return this;
        }
        /**
         * 设置选择星级的触摸事件监听
         */
        public Builder setStars(View.OnTouchListener listener){
            mStarsTouchListener = listener;
            return this;
        }

        /**
         * 设置确定按钮的点击事件监听
         */
       public Builder setButton(@NonNull String text, View.OnClickListener listener) {
            mButton.setText(text);
            mButtonClickListener = listener;
            return this;
        }

        @SuppressLint("ClickableViewAccessibility")
        public InfoDialog create() {
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mButtonClickListener != null)
                        mButtonClickListener.onClick(view);
                    (context).setMark("看过",context.MyStars);
                    mDialog.dismiss();
                }
            });
            mStars.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(mStarsTouchListener!=null)
                        mStarsTouchListener.onTouch(view,motionEvent);
                    float x = motionEvent.getX();
                    float width = mStars.getWidth();
                    int score = (int)((x/width)*10)+1;
                    StarTools.setStars(score,mStars);
                    context.MyStars = score;
                    return false;
                }
            });
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);                //允许用户点击返回键关闭dialog
            mDialog.setCanceledOnTouchOutside(false);   //禁止用户点击dialog之外的区域关闭dialog
            return mDialog;
        }
    }
}