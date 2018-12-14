package com.example.sketchxu.mall.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.adapter.BaseAdapter;
import com.example.sketchxu.mall.adapter.BaseViewHolder;
import com.example.sketchxu.mall.bean.ShoppingCart;
import com.example.sketchxu.mall.utils.CartProvider;
import com.example.sketchxu.mall.widget.NumberAddSubView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

public class CartAdapter extends BaseAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener {

    private TextView textView;
    private CheckBox checkBox;

    private CartProvider mProvider;


    public CartAdapter(Context mContext, final List<ShoppingCart> mData, TextView textView, final CheckBox checkBox) {
        super(mContext, R.layout.template_cart, mData);

        this.textView = textView;
        this.checkBox = checkBox;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll_None(checkBox.isChecked());
                showTotalPrice();
            }
        });
        this.setOnItemClickListener(this);

        mProvider = new CartProvider(mContext);

        showTotalPrice();
        checkListen();
    }

    @Override
    public void bindData(BaseViewHolder h, final ShoppingCart item) {
        SimpleDraweeView draweeView = h.itemView.findViewById(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
        h.getCheckBox(R.id.checkbox).setChecked(item.isChecked());
        h.getTextView(R.id.text_title).setText(item.getName());
        h.getTextView(R.id.text_price).setText(item.getPrice() + "");
        NumberAddSubView num = h.itemView.findViewById(R.id.num_control);
        num.setValue(item.getCount());
        num.setOnButtonOnClickListener(new NumberAddSubView.OnButtonOnClickListener() {
            @Override
            public void onButtonAddClick(View v, int value) {
                item.setCount(value);
                mProvider.update(item);
                showTotalPrice();

                checkListen();
            }

            @Override
            public void onButtonSubClick(View v, int value) {
                item.setCount(value);
                mProvider.update(item);
                showTotalPrice();

                checkListen();
            }
        });

    }

    @Override
    public void onItemClick(View v, int pos) {
        ShoppingCart cart = getItem(pos);

        cart.setChecked(!cart.isChecked());
        notifyItemChanged(pos);

        mProvider.update(getItem(pos));
        showTotalPrice();
        checkListen();

    }

    private float getTotalPrice() {
        float total = 0f;
        if (mData != null && mData.size() > 0) {
            for (ShoppingCart cart : mData) {
                if (cart.isChecked()) {
                    total += cart.getPrice() * cart.getCount();
                }
            }
        }

        return total;
    }

    public void showTotalPrice() {
        float total = getTotalPrice();
        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    public void checkListen() {
        if (mData != null && mData.size() > 0) {
            for (ShoppingCart cart : mData) {
                if (!cart.isChecked()) {
                    checkBox.setChecked(false);
                    return;
                }
            }
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

    }

    public void checkAll_None(boolean isChecked) {
        for (ShoppingCart cart : mData) {
            cart.setChecked(isChecked);
            mProvider.update(cart);
        }

        notifyItemRangeChanged(0, mData.size());
    }

    public void delCart() {
        if (mData != null && mData.size() > 0) {
            for (Iterator<ShoppingCart> iterator = mData.iterator(); iterator.hasNext();) {
                ShoppingCart cart = iterator.next();
                int pos = mData.indexOf(cart);
                if (cart.isChecked()) {
                    iterator.remove();
                    mProvider.delete(cart);
                    notifyItemRemoved(pos);
                }
            }

            checkListen();

        }
    }
}
