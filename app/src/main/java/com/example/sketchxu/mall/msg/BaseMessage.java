package com.example.sketchxu.mall.msg;

import java.io.Serializable;

public class BaseMessage implements Serializable {

    public static final int STATUS_ERROR = 0;
    public static final int STATUS_SUCCESS = 1;

    protected int status = STATUS_SUCCESS;
    protected String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
