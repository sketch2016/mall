package com.example.sketchxu.mall.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.SimpleAdapter;

import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.bean.ShoppingCart;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class WareOrderAdapter extends BaseAdapter<ShoppingCart> {




    public WareOrderAdapter(Context context, List<ShoppingCart> datas) {
        super(context, R.layout.template_order_wares, datas);

    }

    @Override
    public void bindData(BaseViewHolder viewHoder, final ShoppingCart item) {

//        viewHoder.getTextView(R.id.text_title).setText(item.getName());
//        viewHoder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

    }


    public float getTotalPrice(){

        float sum=0;
        if(!isNull())
            return sum;

        for (ShoppingCart cart:
                mData) {

                sum += cart.getCount()*cart.getPrice();
        }

        return sum;

    }


    private boolean isNull(){

        return (mData !=null && mData.size()>0);
    }






}
