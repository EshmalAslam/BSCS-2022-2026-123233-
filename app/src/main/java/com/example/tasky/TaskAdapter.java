package com.example.tasky;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    ArrayList<Task> list;
    OnTaskListener listener;

    public interface OnTaskListener {
        void onEdit(int position);
        void onDelete(int position);
        void onDone(int position);
    }

    public TaskAdapter(ArrayList<Task> list, OnTaskListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = list.get(position);
        holder.name.setText(task.getName());
        holder.dateTime.setText(task.getDate() + " | " + task.getTime());

        // Color coding for priority
        if (task.getPriority() == 0) holder.indicator.setBackgroundColor(Color.parseColor("#FF5252")); // Red
        else if (task.getPriority() == 1) holder.indicator.setBackgroundColor(Color.parseColor("#FFD740")); // Yellow
        else holder.indicator.setBackgroundColor(Color.parseColor("#69F0AE")); // Green

        holder.btnDelete.setOnClickListener(v -> listener.onDelete(position));
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(position));
        holder.btnDone.setOnClickListener(v -> listener.onDone(position));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, dateTime;
        View indicator;
        ImageButton btnEdit, btnDelete, btnDone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtTaskName);
            dateTime = itemView.findViewById(R.id.txtDateTime);
            indicator = itemView.findViewById(R.id.priorityIndicator);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDone = itemView.findViewById(R.id.btnDone);
        }
    }
}