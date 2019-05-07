package com.example.demo.entity;

import com.alibaba.fastjson.JSON;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String ico;

    public User() {
        id = 0;
        name = null;
        email = null;
        password = null;
        ico = null;
    }

    public User(int id, String name, String email, String password, String ico) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.ico = ico;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
