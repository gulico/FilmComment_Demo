package com.example.wxy.beanfilm.Model;

/**
 * Created by WXY on 2019/1/19.
 */

public class User {
    private String mId;//用户账号
    private String mName;//用户名
    private String mPassword;//密码

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
