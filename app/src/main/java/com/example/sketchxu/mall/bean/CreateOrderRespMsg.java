package com.example.sketchxu.mall.bean;


import com.example.sketchxu.mall.msg.BaseMessage;

public class CreateOrderRespMsg extends BaseMessage {



    private OrderRespMsg data;

    public OrderRespMsg getData() {
        return data;
    }

    public void setData(OrderRespMsg data) {
        this.data = data;
    }



}


