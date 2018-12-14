package com.example.sketchxu.mall.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.bean.HomeCampaign;
import com.example.sketchxu.mall.bean.HomeCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<HomeCampaign> mData;

    private static final int BIG_LEFT = 0;
    private static final int BIG_RIGHT = 1;

    private Context mContext;

    private OnItemClickListener onItemClickListener;

    private LayoutInflater mInflator;

    public HomeAdapter(List<HomeCampaign> data, Context context) {
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
        if (i == BIG_LEFT) {
            return new ViewHolder(mInflator.inflate(R.layout.home_card2, viewGroup, false));
        }

        return new ViewHolder(mInflator.inflate(R.layout.home_card1, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HomeCampaign campaign = mData.get(i);
        viewHolder.text.setText(campaign.getTitle());

        Picasso.with(mContext).load(campaign.getCpOne().getImgUrl()).into(viewHolder.imgBig);
        Picasso.with(mContext).load(campaign.getCpTwo().getImgUrl()).into(viewHolder.imgSmallTop);
        Picasso.with(mContext).load(campaign.getCpThree().getImgUrl()).into(viewHolder.imgSmallBottom);
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

    public void addData(int pos, String city) {
        //mData.add(pos, city);
        //notifyItemInserted(pos);

    }

    public void removeData(int pos) {
        //mData.remove(pos);
        //notifyItemRemoved(pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView text = null;
        private ImageView imgBig;
        private ImageView imgSmallTop;
        private ImageView imgSmallBottom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.name);
            imgBig = itemView.findViewById(R.id.imgview_big);
            imgSmallTop = itemView.findViewById(R.id.imgview_small_top);
            imgSmallBottom = itemView.findViewById(R.id.imgview_small_bottom);

            imgBig.setOnClickListener(this);
            imgSmallTop.setOnClickListener(this);
            imgSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                anim(v);
            }
        }

        private void anim(final View v) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotationX", 0.0F, 360F);
            animator.setDuration(200);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    HomeCampaign homeCampaign = mData.get(getLayoutPosition());
                    switch (v.getId()) {
                        case R.id.imgview_big:
                            onItemClickListener.onClick(v, getLayoutPosition(), homeCampaign.getCpOne().getTitle());
                            break;
                        case R.id.imgview_small_top:
                            onItemClickListener.onClick(v, getLayoutPosition(), homeCampaign.getCpTwo().getTitle());
                            break;
                        case R.id.imgview_small_bottom:
                            onItemClickListener.onClick(v, getLayoutPosition(), homeCampaign.getCpThree().getTitle());
                            break;
                    }
                }
            });
            animator.start();

        }
    }


    public interface OnItemClickListener {

        void onClick(View v, int pos, String info);

    }
}
