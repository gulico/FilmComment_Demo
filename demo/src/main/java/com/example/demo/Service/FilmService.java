package com.example.demo.Service;

import com.example.demo.entity.MarkFilmSimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<MarkFilmSimple> getFilmbyUserID(int userid){
        String sql = "select * from films where userid = ?";
        RowMapper<MarkFilmSimple> rowMapper
                = new BeanPropertyRowMapper<MarkFilmSimple>(MarkFilmSimple.class);
        List<MarkFilmSimple> films = new ArrayList<>();
        try{
            films = jdbcTemplate.query(sql, rowMapper,userid);
        }catch (Exception e){
            System.out.print(e);
        }
        return films;
    }

    public boolean setFilm(MarkFilmSimple film){
        int temp2 = 0;
        if(film.getState().equals("看过")) {
            String sqldelete = "delete from films where url = ? and userid = ?";
            temp2 = jdbcTemplate.update(sqldelete,film.getUrl(),film.getUserid());
        }

        String sql = "insert into films (title,score,state,date,info,myscore,userid,pic,source,url) " +
                "value (?,?,?,?,?,?,?,?,?,?)";
        Object args[] = {film.getTitle(),film.getScore()
                ,film.getState(),film.getDate()
                ,film.getInfo(),film.getMyscore()
                ,film.getUserid(),film.getPic()
                ,film.getSource(),film.getUrl()};
        int temp = jdbcTemplate.update(sql, args);

        if(temp > 0 && temp2>=0) {
            return true;//成功
        }
        return false;//失败
    }
}
