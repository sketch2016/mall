package com.example.sketchxu.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.activity.DetailActivity;
import com.example.sketchxu.mall.bean.ShoppingCart;
import com.example.sketchxu.mall.bean.Ware;
import com.example.sketchxu.mall.utils.CartProvider;
import com.example.sketchxu.mall.utils.ToastUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HWAdapter extends BaseAdapter<Ware> {

    private CartProvider mProvider;

    public HWAdapter(final Context mContext, List<Ware> mData) {
        super(mContext, R.layout.hotware_item, mData);

        mProvider = new CartProvider(mContext);
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                Ware ware = getItem(pos);
                intent.putExtra(Contants.WARE, ware);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void bindData(BaseViewHolder h, final Ware ware) {

        Picasso.with(mContext).load(ware.getImgUrl()).into(h.getImageView((R.id.image_view)));
        h.getTextView(R.id.text_title).setText(ware.getName());
        h.getTextView(R.id.text_price).setText(ware.getPrice() + "");
        Button btnBuy = h.getButton(R.id.buy);
        if (btnBuy != null) {
            h.getButton(R.id.buy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProvider.add(ware);
                    ToastUtils.show(mContext, "已加入购物车");
                }
            });
        }

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

    public void resetLayout(int layoutId) {
        this.mLayoutResId = layoutId;

        notifyItemRangeChanged(0, mData.size());
    }
}
