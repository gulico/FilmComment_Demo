package com.example.wxy.beanfilm.Model;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.wxy.beanfilm.R;

public class StarTools {

    public static ImageView star[]  = new ImageView[5];
    public static void init(LinearLayout linearLayout){
        star[0] = (ImageView) linearLayout.findViewById(R.id.star_n_score_star1);
        star[1] = (ImageView) linearLayout.findViewById(R.id.star_n_score_star2);
        star[2] = (ImageView) linearLayout.findViewById(R.id.star_n_score_star3);
        star[3] = (ImageView) linearLayout.findViewById(R.id.star_n_score_star4);
        star[4] = (ImageView) linearLayout.findViewById(R.id.star_n_score_star5);
    }
    public static void setStars(float score, LinearLayout linearLayout){
        init(linearLayout);
        setStars(score);
    }
/*
    public static void setStarsListener(void view, LinearLayout linearLayout){
        init(linearLayout);
        for(int i=0;i<5;i++){
            star[i].setOnClickListener(view);
        }
    }*/

    private static void setStars(float score){
        for(int i=0;i<5;i++){//全灰
            star[i].setColorFilter(Color.parseColor("#969696"));
        }
        for(int i=0;i<5;i++){
            if(score>(i*2+1)){
                star[i].setColorFilter(Color.parseColor("#FFAD2E"));
            }
        }
    }
/*
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.star_n_score_star1:
                setStars(2);
                break;
            case R.id.star_n_score_star2:
                setStars(4);
                break;
            case R.id.star_n_score_star3:
                setStars(6);
                break;
            case R.id.star_n_score_star4:
                setStars(8);
                break;
            case R.id.star_n_score_star5:
                setStars(10);
                break;
        }
    }*/
}
