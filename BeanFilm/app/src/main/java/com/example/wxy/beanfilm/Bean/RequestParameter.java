package com.example.wxy.beanfilm.Bean;

import java.io.Serializable;

public class RequestParameter implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private  String name = "";
    private  String value = "";
    public RequestParameter(String name,String value){
        this.name=name;
        this.value=value;
    }
}
