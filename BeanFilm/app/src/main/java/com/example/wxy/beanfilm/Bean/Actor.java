package com.example.wxy.beanfilm.Bean;

/**
 * Created by WXY on 2019/4/12.
 */

public class Actor {
    private String name;
    private String role;
    private String pic;

    public Actor() {
        name = role = pic = null;
    }

    public Actor(String name, String role,String pic) {
        this.name = name;
        this.role = role;
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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
