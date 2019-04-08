package com.example.multinotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter <NoteViewHolder> {

    private List<Note> noteList;
    private MainActivity mainAct;

    public NoteAdapter(List<Note> noteList, MainActivity ma) {
        this.noteList = noteList;
        mainAct = ma;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_entry, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note oneItem = noteList.get(position);
        holder.titleField.setText(oneItem.getTitle());
        holder.textField.setText(oneItem.getText());
        holder.dateField.setText(oneItem.getDate());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
