package com.example.sketchxu.mall.bean;

public class ShoppingCart extends Ware{

    private int count;
    private boolean checked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
