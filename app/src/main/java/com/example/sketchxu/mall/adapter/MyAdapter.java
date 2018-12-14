package com.example.sketchxu.mall.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sketchxu.mall.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> data;

    private OnItemClickListener onItemClickListener;

    public MyAdapter(List<String> data) {
       this.data = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.text.setText(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(int pos, String city) {
        data.add(pos, city);
        notifyItemInserted(pos);

    }

    public void removeData(int pos) {
        data.remove(pos);
        notifyItemRemoved(pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(v, getLayoutPosition(), data.get(getLayoutPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {

        void onClick(View v, int pos, String city);

    }
}
