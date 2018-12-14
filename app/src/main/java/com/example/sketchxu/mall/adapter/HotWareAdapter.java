package com.example.sketchxu.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.bean.Banner;
import com.example.sketchxu.mall.bean.HomeCampaign;
import com.example.sketchxu.mall.bean.HomeCategory;
import com.example.sketchxu.mall.bean.Page;
import com.example.sketchxu.mall.bean.Ware;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HotWareAdapter extends RecyclerView.Adapter<HotWareAdapter.ViewHolder> {

    private static final int BIG_LEFT = 0;
    private static final int BIG_RIGHT = 1;

    private Context mContext;

    private OnItemClickListener onItemClickListener;

    private LayoutInflater mInflator;

    private List<Ware> mData;

    public HotWareAdapter(List<Ware> data, Context context) {
        this.mData = data;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mInflator = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(mInflator.inflate(R.layout.hotware_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Ware ware = mData.get(i);

        Picasso.with(mContext).load(ware.getImgUrl()).into(viewHolder.img);
        viewHolder.title.setText(ware.getName());
        viewHolder.price.setText(ware.getPrice() + "");


    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return BIG_LEFT;
        }

        return BIG_RIGHT;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void clearData() {
        mData.clear();
        notifyItemRangeRemoved(0, mData.size());
    }

    public void addData(List<Ware> datas) {
        addData(0, datas);
    }

    public void addData(int pos, List<Ware> datas) {
        if (datas != null && datas.size() > 0) {
            mData.addAll(datas);
            notifyItemRangeInserted(pos, datas.size());
        }

    }

    public List<Ware> getDatas() {
        return mData;
    }

    public void removeData(int pos) {
        //mData.remove(pos);
        //notifyItemRemoved(pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView title;
        private TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.text_title);
            price = itemView.findViewById(R.id.text_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(v, getLayoutPosition(),mData.get(getLayoutPosition()).getName());
                    }
                }
            });
        }

    }

    public interface OnItemClickListener {

        void onClick(View v, int pos, String info);

    }



}
