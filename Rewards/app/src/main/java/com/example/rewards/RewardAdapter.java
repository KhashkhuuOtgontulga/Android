package com.example.rewards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rewards.Activities.ProfileActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RewardAdapter extends RecyclerView.Adapter <RewardViewHolder> {
    private List<Reward> rewardList;

    public RewardAdapter(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }


    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reward_list_entry, parent, false);
        return new RewardViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        Reward oneItem = rewardList.get(position);
        holder.dateField.setText(oneItem.getDate().toString());
        holder.nameField.setText(oneItem.getName());
        holder.commentField.setText(oneItem.getComment());
        holder.pointField.setText(String.valueOf(oneItem.getAward_points()));
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }
}
