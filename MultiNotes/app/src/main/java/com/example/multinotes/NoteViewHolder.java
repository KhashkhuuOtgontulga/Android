package com.example.multinotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    // public because we want to access the elements
    // and we are never going to touch this data
    public TextView titleField;
    public TextView textField;
    public TextView dateField;

    public NoteViewHolder(View view) {
        super(view);
        titleField = view.findViewById(R.id.titleField);
        textField = view.findViewById(R.id.textField);
        dateField = view.findViewById(R.id.dateField);
    }
}
