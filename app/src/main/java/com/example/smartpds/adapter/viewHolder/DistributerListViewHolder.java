package com.example.smartpds.adapter.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;


public class DistributerListViewHolder extends RecyclerView.ViewHolder {
public TextView distributerId;
    public DistributerListViewHolder(@NonNull View itemView) {
        super(itemView);

        distributerId = itemView.findViewById(R.id.distributerId);


    }



}
