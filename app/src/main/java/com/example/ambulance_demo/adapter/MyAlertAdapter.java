package com.example.ambulance_demo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ambulance_demo.MainActivity;
import com.example.ambulance_demo.R;
import com.example.ambulance_demo.entity.AlertTable;
import com.example.ambulance_demo.service.Services;

public class MyAlertAdapter extends RecyclerView.Adapter<MyAlertAdapter.ViewHolder> {
    public AlertTable[] alertTables;
    public Context context;
    public MyAlertAdapter(AlertTable[] alertTables, Context context) {
        this.alertTables = alertTables;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAlertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.alert_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyAlertAdapter.ViewHolder holder, int position) {
        AlertTable alertTable = alertTables[position];
        holder.textView.setText(Integer.toString(alertTable.getAlert_id()));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof MainActivity){
                    ((MainActivity)context).acceptAmbulance(alertTable.getAlert_id(), alertTable.getAlert_user_id());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alertTables.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.alert_text);
            button = itemView.findViewById(R.id.accept_button);
        }
    }
}
