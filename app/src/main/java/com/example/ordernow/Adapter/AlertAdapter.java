package com.example.ordernow.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ordernow.R;
import com.example.ordernow.Models.AlertItem;
import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {

    private List<AlertItem> alertList;

    public AlertAdapter(List<AlertItem> alertList) {
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_item, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        AlertItem alertItem = alertList.get(position);
        holder.alertMessage.setText(alertItem.getMessage());
        holder.markAsReadButton.setOnClickListener(v -> {
            // Handle mark as read functionality
            alertItem.setRead(true);
            holder.alertMessage.setAlpha(0.5f); // grey out
        });
        holder.deleteButton.setOnClickListener(v -> {
            // delete functionality
            alertList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, alertList.size());
        });
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    static class AlertViewHolder extends RecyclerView.ViewHolder {
        TextView alertMessage;
        Button markAsReadButton;
        Button deleteButton;

        public AlertViewHolder(@NonNull View itemView) {
            super(itemView);
            alertMessage = itemView.findViewById(R.id.textViewAlertMessage);
            markAsReadButton = itemView.findViewById(R.id.buttonMarkAsRead);
            deleteButton = itemView.findViewById(R.id.buttonDelete);

        }
    }
}
