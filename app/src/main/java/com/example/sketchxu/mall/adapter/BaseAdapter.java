package com.example.sketchxu.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.bean.Address;
import com.example.sketchxu.mall.bean.Ware;

import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected Context mContext;

    protected int mLayoutResId;

    protected List<T> mData;

    protected LayoutInflater mInflator;

    protected OnItemClickListener listener;

    public void refreshData(List<T> datas) {

            if(datas !=null && datas.size()>0){

                clearData();
                int size = datas.size();
                for (int i=0;i<size;i++){
                    datas.add(i,datas.get(i));
                    notifyItemInserted(i);
                }

            }
    }


    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BaseAdapter(Context mContext, int mLayoutResId, List<T> mData) {
        this.mContext = mContext;
        this.mLayoutResId = mLayoutResId;
        this.mData = mData;

        mInflator = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new BaseViewHolder(mInflator.inflate(mLayoutResId, viewGroup, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {

        bindData(h, getItem(i));
    }

    public abstract void bindData(BaseViewHolder h, T item);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public T getItem(int pos) {
        return mData.get(pos);
    }

    public void clearData() {
        mData.clear();
        notifyItemRangeRemoved(0, mData.size());
    }

    public void addData(List<T> datas) {
        addData(0, datas);
    }

    public void addData(int pos, List<T> datas) {
        if (datas != null && datas.size() > 0) {
            mData.addAll(datas);
            notifyItemRangeInserted(pos, datas.size());
        }

    }

    public List<T> getDatas() {
        return mData;
    }
}
