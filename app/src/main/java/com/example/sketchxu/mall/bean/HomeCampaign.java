package com.example.sketchxu.mall.bean;

public class HomeCampaign {

    private int id;
    private String title;
    Compaign cpOne;
    Compaign cpTwo;
    Compaign cpThree;

    public HomeCampaign(int id, String title, Compaign cpOne, Compaign cpTwo, Compaign cpThree) {
        this.id = id;
        this.title = title;
        this.cpOne = cpOne;
        this.cpTwo = cpTwo;
        this.cpThree = cpThree;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Compaign getCpOne() {
        return cpOne;
    }

    public void setCpOne(Compaign cpOne) {
        this.cpOne = cpOne;
    }

    public Compaign getCpTwo() {
        return cpTwo;
    }

    public void setCpTwo(Compaign cpTwo) {
        this.cpTwo = cpTwo;
    }

    public Compaign getCpThree() {
        return cpThree;
    }

    public void setCpThree(Compaign cpThree) {
        this.cpThree = cpThree;
    }
}
