package com.example.demo.Service;

import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User getUserbyEmail(String email){
        String sql = "select * from users where email = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
        User user = new User();
        try{
            user = jdbcTemplate.queryForObject(sql, rowMapper,email);
        }catch (Exception e){
            System.out.print(e);
        }
        return user;
    }

    public String Register(String name,String email,String password){
        String ans = "";
        String sql = "select * from users where email = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
        User user = new User();
        try{
            user = jdbcTemplate.queryForObject(sql, rowMapper,email);
            if(user.getEmail().equals(email))
                return "该邮箱已被注册";
        }catch (Exception e){
            System.out.print(e);
        }

        try{
            String sql2 = "insert into users (name,email,password) value (?,?,?)";
            Object args[] = {name,email,password};
            int temp = jdbcTemplate.update(sql2, args);
            if(temp > 0) {
                return "注册成功";//成功
            }
        }catch (Exception e) {
            System.out.print(e);
        }
        return "注册失败";
    }
}
