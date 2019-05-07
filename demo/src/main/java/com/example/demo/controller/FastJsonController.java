package com.example.demo.controller;

import com.example.demo.Service.FilmService;
import com.example.demo.Service.FontService;
import com.example.demo.Service.UserService;
import com.example.demo.entity.FontScore;
import com.example.demo.entity.MarkFilmSimple;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class FastJsonController {

    @Autowired
    private UserService userService;

    @RequestMapping("/Login")
    public User Login(@RequestParam(required=true) Map<String,Object> map) {
        String email = map.get("email").toString().trim();
        User user = userService.getUserbyEmail(email);
        System.out.println("登陆");
        return user;
    }

    @RequestMapping("/Register")
    public String Register(@RequestParam(required=true) Map<String,Object> map) {
        String email = map.get("email").toString().trim();
        String name = map.get("name").toString().trim();
        String password = map.get("password").toString().trim();
        return userService.Register(name,email,password);
    }



    @Autowired
    private FilmService filmService;

    @RequestMapping("/GetMarkFilm")
    public List<MarkFilmSimple> GetMarkFilm(@RequestParam(required=true) Map<String,Object> map) {
        int userid = Integer.parseInt(map.get("userid").toString());
        List<MarkFilmSimple> films  = filmService.getFilmbyUserID(userid);
        System.out.println("获取电影");
        return films;
    }

    @RequestMapping("/SetMarkFilm")
    public boolean SetMarkFilm(@RequestParam(required=true) Map<String,Object> map) {
        MarkFilmSimple filmSimple = new MarkFilmSimple(-1,map.get("title").toString(),
                Float.parseFloat(map.get("score").toString()),
                map.get("state").toString(),
                map.get("date").toString(),
                map.get("info").toString(),
                Integer.parseInt(map.get("myscore").toString()),
                Integer.parseInt(map.get("userid").toString()),
                map.get("pic").toString(),
                map.get("source").toString(),
                map.get("url").toString());
        return filmService.setFilm(filmSimple);
    }

    @Autowired
    private FontService fontService;

    @RequestMapping("/GetMaoYanScore")
    public FontScore GetMaoYanScore(@RequestParam(required=true) Map<String,Object> map) {
        String filmid = map.get("filmid").toString().trim();
        return fontService.getScore(filmid);
    }

}
