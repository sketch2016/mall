package com.example.sketchxu.mall.bean;

public class Category extends BaseBean {

    protected String name;

    public Category(){

    }

    public Category(String name){
        this.name = name;
    }

    public Category(int id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
