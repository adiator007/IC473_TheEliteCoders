package com.example.smartpds.adapter.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.model.Orders;


public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView shopName;
    public TextView timestamp;
    public TextView amount;
    public TextView shopAddress;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        shopName = itemView.findViewById(R.id.shopName);
        timestamp = itemView.findViewById(R.id.timestamp);
        amount = itemView.findViewById(R.id.totalAmount);
        shopAddress =itemView.findViewById(R.id.shopAddress);
    }

    public void bindOrder(Orders model) {


    }


}
