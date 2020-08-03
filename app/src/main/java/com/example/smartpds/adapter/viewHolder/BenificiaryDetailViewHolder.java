package com.example.smartpds.adapter.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartpds.R;
import com.example.smartpds.model.Benificiary;

public class BenificiaryDetailViewHolder extends RecyclerView.ViewHolder {

    ImageView benificiaryImageview ;
    TextView benificiaryName ;
    public BenificiaryDetailViewHolder(@NonNull View itemView) {
        super(itemView);

        benificiaryImageview = itemView.findViewById(R.id.benificiary_photo_imageview);
        benificiaryName = itemView.findViewById(R.id.benificiary_name_textview);

    }


    public  void bind(Context context , Benificiary benificiary){

        benificiaryName.setText(benificiary.getBname());
        Glide.with(context).load(benificiary.getPhotouri()).into(benificiaryImageview);

    }
}
