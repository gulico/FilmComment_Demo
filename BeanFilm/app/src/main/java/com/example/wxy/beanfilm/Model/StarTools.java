package com.example.wxy.beanfilm.Model;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StarTools {
    public static void setStars(float score,ImageView star1,ImageView star2,ImageView star3,ImageView star4,ImageView star5){
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
