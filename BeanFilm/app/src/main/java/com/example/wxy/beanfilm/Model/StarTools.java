package com.example.wxy.beanfilm.Model;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.wxy.beanfilm.R;

public class StarTools {


    public static void setStars(float score,LinearLayout linearLayout){
        ImageView star1 = (ImageView) linearLayout.findViewById(R.id.star_n_score_star1);
        ImageView star2 = (ImageView) linearLayout.findViewById(R.id.star_n_score_star2);
        ImageView star3 = (ImageView) linearLayout.findViewById(R.id.star_n_score_star3);
        ImageView star4 = (ImageView) linearLayout.findViewById(R.id.star_n_score_star4);
        ImageView star5 = (ImageView) linearLayout.findViewById(R.id.star_n_score_star5);
        if(score>1){
            star1.setColorFilter(Color.parseColor("#FFAD2E"));
        }
        if(score>3){
            star2.setColorFilter(Color.parseColor("#FFAD2E"));
        }
        if(score>5){
            star3.setColorFilter(Color.parseColor("#FFAD2E"));
        }
        if(score>7){
            star4.setColorFilter(Color.parseColor("#FFAD2E"));
        }
        if(score>9){
            star5.setColorFilter(Color.parseColor("#FFAD2E"));
        }
    }
}
