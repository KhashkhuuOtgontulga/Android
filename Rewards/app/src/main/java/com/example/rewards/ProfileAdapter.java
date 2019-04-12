package com.example.rewards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rewards.Activities.LeaderboardActivity;
import com.example.rewards.Activities.ProfileActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends RecyclerView.Adapter <ProfileViewHolder> {

    private List<UserProfile> profileList;
    private UserProfile sourceProfile;
    private LeaderboardActivity profileAct;

    public ProfileAdapter(List<UserProfile> profileList, UserProfile sourceProfile, LeaderboardActivity la) {
        this.profileList = profileList;
        this.sourceProfile = sourceProfile;
        profileAct = la;
    }


    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_list_entry, parent, false);
        itemView.setOnClickListener(profileAct);
        return new ProfileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        UserProfile oneItem = profileList.get(position);
        holder.nameField.setText(oneItem.getLast_name() + ", " + oneItem.getFirst_name());
        holder.jobField.setText(oneItem.getPosition() + ", " + oneItem.getDepartment());
        holder.pointField.setText(String.valueOf(oneItem.getPoints_awarded()));
        byte[] imageBytes = Base64.decode(oneItem.getImage(),  Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.imageField.setImageBitmap(bitmap);
        if (oneItem.getFirst_name().toLowerCase().trim().equals(sourceProfile.getFirst_name().toLowerCase())) {
            holder.nameField.setTextColor(Color.parseColor("#008577"));
            holder.jobField.setTextColor(Color.parseColor("#008577"));
            holder.pointField.setTextColor(Color.parseColor("#008577"));
        }
        else {
            holder.nameField.setTextColor(Color.GRAY);
            holder.jobField.setTextColor(Color.GRAY);
            holder.pointField.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }
}
