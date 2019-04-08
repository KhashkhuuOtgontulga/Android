package com.example.rewards;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ProfileViewHolder extends RecyclerView.ViewHolder{

    public TextView nameField;
    public TextView jobField;
    public TextView pointField;
    public ImageView imageField;

    public ProfileViewHolder(View view) {
        super(view);
        nameField = view.findViewById(R.id.nameField);
        jobField = view.findViewById(R.id.jobField);
        pointField = view.findViewById(R.id.pointField);
        imageField = view.findViewById(R.id.imageField);
    }

}
