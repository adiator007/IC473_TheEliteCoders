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


public class BenificiaryViewHolder extends RecyclerView.ViewHolder{

    ImageView benificiaryImageView;
    TextView benificiaryName , benificiaryDob ,bBenificiaryStatus;
    public BenificiaryViewHolder(@NonNull View itemView) {
        super(itemView);

        benificiaryImageView = itemView.findViewById(R.id.benificiaryImage);
        benificiaryName = itemView.findViewById(R.id.benificiaryName);
        benificiaryDob = itemView.findViewById(R.id.benificiaryDob);
        bBenificiaryStatus = itemView.findViewById(R.id.benificiaryStatus);

    }

    public void bindBenificiaryProfile(Context context , Benificiary model) {

        Glide.with(context).load(model.getPhotouri()).into(benificiaryImageView);
        benificiaryName.setText(model.getBname());
        benificiaryDob.setText(model.getBdob());

        if (model.isStatus()) {
            bBenificiaryStatus.setText("Approved");
        }
        else
        {
            bBenificiaryStatus.setText("Pending");
        }

    }

}
