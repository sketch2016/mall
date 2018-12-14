package com.example.sketchxu.mall.bean;

import android.content.Context;

import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.adapter.BaseAdapter;
import com.example.sketchxu.mall.adapter.BaseViewHolder;

import java.util.List;

public class CategoryAdapter extends BaseAdapter<Category> {

    public CategoryAdapter(Context mContext, List<Category> mData) {
        super(mContext, R.layout.template_single_text, mData);
    }

    @Override
    public void bindData(BaseViewHolder h, Category item) {
        h.getTextView(R.id.textView).setText(item.getName());
    }
}
