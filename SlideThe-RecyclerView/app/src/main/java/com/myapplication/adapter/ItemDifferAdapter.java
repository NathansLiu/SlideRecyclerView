package com.myapplication.adapter;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 作者: Nathans'Liu
 * 邮箱: a1053128464@qq.com
 * 时间: 2017/12/26 15:47
 * 描述:
 */

public class ItemDifferAdapter extends RecyclerView.Adapter<ItemDifferAdapter.LinearLayoutHolder> {
    private Random mRandom = new Random();
    private List<Integer> mBeanList = new ArrayList<>();


    @Override
    public LinearLayoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < 20; i++) {
            if (mRandom.nextBoolean()) {
                mBeanList.add(R.mipmap.ic_tk);
            } else {
                mBeanList.add(R.mipmap.ic_mm);
            }
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new LinearLayoutHolder(view);
    }

    @Override
    public void onBindViewHolder(LinearLayoutHolder holder, int position) {
        //加个动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0.5f, 1f);
        scaleX.start();
        holder.mImageView.setImageResource(mBeanList.get(position));
        holder.mTextView.setText(position + "");
    }

    @Override
    public int getItemCount() {
        return 20;
    }


    class LinearLayoutHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;

        public LinearLayoutHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv);
            mTextView = itemView.findViewById(R.id.tv_position);
        }
    }
}
