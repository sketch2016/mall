package com.example.sketchxu.mall.utils;

import android.content.Context;
import android.util.SparseArray;

import com.example.sketchxu.mall.bean.ShoppingCart;
import com.example.sketchxu.mall.bean.Ware;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class CartProvider {

    private static final String CART_NAME = "json_cart";

    private SparseArray<ShoppingCart> datas;

    private PreferenceUtil preferenceUtil;

    public CartProvider(Context context) {

        preferenceUtil = PreferenceUtil.getInstance(context);
        datas = ListToSparse(getAll());
    }

    public void add(ShoppingCart cart) {
        ShoppingCart tmp = datas.get(cart.getId());
        if (tmp != null) {
            tmp.setCount(tmp.getCount() + 1);
        } else {
            cart.setCount(1);
            datas.put(cart.getId(), cart);
        }

        store();
    }


    public void add(Ware cart) {
        add(wareToShoppingCart(cart));
    }


    public void delete(ShoppingCart cart) {
        datas.delete(cart.getId());

        store();
    }

    public void update(ShoppingCart cart) {
        datas.put(cart.getId(), cart);
        store();
    }

    private void store() {
        List<ShoppingCart> carts = sparseToList();
        String json = JsonUtil.toJson(carts);
        preferenceUtil.putString(CART_NAME, json);
    }

    public List<ShoppingCart> getAll() {
        String json = preferenceUtil.getString(CART_NAME);
        List<ShoppingCart> carts = JsonUtil.fromJson(json, new TypeToken<List<ShoppingCart>>(){}.getType());

        return carts;
    }

    public void clear(){
        datas.clear();
        preferenceUtil.putString(CART_NAME, null);
    }

    private SparseArray<ShoppingCart> ListToSparse(List<ShoppingCart> carts) {
        if (carts == null) {
            carts = new ArrayList<>();
        }

        SparseArray<ShoppingCart> data = new SparseArray<ShoppingCart>(carts.size());

        for (ShoppingCart cart : carts) {
            data.put(cart.getId(), cart);
        }

        return data;
    }

    private List<ShoppingCart> sparseToList() {
        List<ShoppingCart> carts = new ArrayList<>(datas.size());

        for (int i = 0; i < datas.size(); i++) {
            carts.add(datas.valueAt(i));
        }

        return carts;
    }

    private ShoppingCart wareToShoppingCart(Ware ware) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(ware.getId());
        cart.setImgUrl(ware.getImgUrl());
        cart.setName(ware.getName());
        cart.setPrice(ware.getPrice());
        cart.setSales(ware.getSales());
        cart.setCount(0);

        return cart;
    }

}
