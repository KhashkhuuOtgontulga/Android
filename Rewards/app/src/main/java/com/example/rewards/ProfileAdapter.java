package com.example.rewards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rewards.Activities.LeaderboardActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends RecyclerView.Adapter <ProfileViewHolder> {

    private List<UserProfile> profileList;
    private LeaderboardActivity profileAct;

    public ProfileAdapter(List<UserProfile> profileList, LeaderboardActivity la) {
        this.profileList = profileList;
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
        //holder.imageField.setText(oneItem.getDate());
        /*if (oneItem.getFirst_name().toLowerCase().trim().equals("volkswagen")) {
            holder.make.setTextColor(getResources().getColor(R.color.colorAccent));
            holder.model.setTextColor(getResources().getColor(R.color.colorAccent));
            holder.year.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            holder.make.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            holder.model.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            holder.year.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }*/
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }
}
