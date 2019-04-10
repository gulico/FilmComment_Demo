package com.example.wxy.beanfilm.Bean;

import java.io.Serializable;

/**
 * Created by WXY on 2019/1/19.
 */

public class User implements Serializable {
    private int id;//用户账号
    private String name;//用户名
    private String password;//密码
    private String email;
    private String ico;

    public User(int id, String name, String password, String email, String ico) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.ico = ico;
    }

    public User() {
        id = 0;
        name = null;
        password = null;
        email = null;
        ico = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }
}
