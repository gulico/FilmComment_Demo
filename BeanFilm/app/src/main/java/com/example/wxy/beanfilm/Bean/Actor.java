package com.example.wxy.beanfilm.Bean;

/**
 * Created by WXY on 2019/4/12.
 */

public class Actor {
    private String name;
    private String role;

    public Actor() {
        name = role = null;
    }

    public Actor(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
