package com.example.smartpds.recyclerex.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;

import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<ProductDetails> MainImageUploadInfoList;

    public ProductRecyclerViewAdapter(Context context, List<ProductDetails> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductDetails productDetails = MainImageUploadInfoList.get(position);

        holder.StudentNameTextView.setText(productDetails.getQuantity());

        holder.StudentNumberTextView.setText(productDetails.getPrice());

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView StudentNameTextView;
        public TextView StudentNumberTextView;

        public ViewHolder(View itemView) {

            super(itemView);

            StudentNameTextView = (TextView) itemView.findViewById(R.id.ShowStudentNameTextView);

            StudentNumberTextView = (TextView) itemView.findViewById(R.id.ShowStudentNumberTextView);
        }
    }
}
