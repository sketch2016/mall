package com.example.sketchxu.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.activity.DetailActivity;
import com.example.sketchxu.mall.bean.Ware;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class WaresAdapter extends BaseAdapter<Ware> {

    public WaresAdapter(final Context mContext, List<Ware> mData) {
        super(mContext, R.layout.template_grid_wares, mData);
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
    public void bindData(BaseViewHolder h, Ware item) {
        SimpleDraweeView draweeView = h.itemView.findViewById(R.id.image_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
        h.getTextView(R.id.text_title).setText(item.getName());
        h.getTextView(R.id.text_price).setText(item.getPrice() + "");
    }
}
