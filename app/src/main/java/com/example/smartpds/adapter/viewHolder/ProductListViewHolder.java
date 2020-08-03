package com.example.smartpds.adapter.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;


public class ProductListViewHolder extends RecyclerView.ViewHolder {
    public TextView productId;

    public ProductListViewHolder(@NonNull View itemView) {
        super(itemView);

        productId = itemView.findViewById(R.id.distributerId);


    }
}
