package com.example.smartpds.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.viewHolder.BenificiaryDetailViewHolder;
import com.example.smartpds.model.Benificiary;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BenififcaryDetailAdapter extends FirebaseRecyclerAdapter<Benificiary, BenificiaryDetailViewHolder> {
    private Context mContext;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BenififcaryDetailAdapter(@NonNull FirebaseRecyclerOptions<Benificiary> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BenificiaryDetailViewHolder holder, int position, @NonNull Benificiary model) {
        holder.bind(mContext ,model);
    }


    @NonNull
    @Override
    public BenificiaryDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.benificiary_item_layout, parent, false);
        mContext =parent.getContext();
        return new BenificiaryDetailViewHolder(view);
    }
}
