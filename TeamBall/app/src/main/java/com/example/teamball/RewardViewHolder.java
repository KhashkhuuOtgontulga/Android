package com.example.teamball;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RewardViewHolder  extends RecyclerView.ViewHolder{
    public TextView dateField;
    public TextView nameField;
    public TextView commentField;
    public TextView pointField;

    public RewardViewHolder(View view) {
        super(view);
        dateField = view.findViewById(R.id.dateField);
        nameField = view.findViewById(R.id.nameField);
        commentField = view.findViewById(R.id.commentField);
        pointField = view.findViewById(R.id.pointField);
    }
}
