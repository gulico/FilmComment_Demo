package com.example.demo.Service;

import com.example.demo.entity.FontScore;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.*;

@Service
public class FontService {
    public FontScore getScore(String filmID){
        FontScore fontScore = new FontScore();
        try{
            String exe = "python";
            String command = "C:\\Users\\Administrator\\Desktop\\python-maoyan-spider\\Maoyan\\com\\sider\\main.py";

            String[] cmdArr = new String[] {exe,command,filmID};
            Process pr = Runtime.getRuntime().exec(cmdArr);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    pr.getInputStream()));
            String line = "";

            String strnum = "";
            String strscore = "";
            int i=0;
            while ((line = in.readLine())!=null){
                if(i==0)
                    strnum = line;
                else if(i==1)
                    strscore = line;
                i++;
            }
            in.close();
            pr.waitFor();

            int num = 0;
            if(strnum.indexOf("万")!=-1) {//以万为的单位
                num = (int)(10000*Float.parseFloat(strnum.substring(0,strnum.indexOf("万"))));
            }
            else{
                num = Integer.parseInt(strnum);
            }
            fontScore.setNum(num);
            //System.out.print(num);
            float score = 0;
            score = Float.parseFloat(strscore);
            //System.out.print(score);
            fontScore.setScore(score);

        }catch (Exception ex){
            System.out.print(ex);
        }
        return fontScore;
    }
}
