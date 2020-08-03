package com.example.smartpds.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.viewHolder.BenificiaryViewHolder;
import com.example.smartpds.model.Benificiary;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BenificiaryAdapter extends FirebaseRecyclerAdapter<Benificiary, BenificiaryViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BenificiaryAdapter(@NonNull FirebaseRecyclerOptions<Benificiary> options) {
        super(options);

    }
    Context mContext;

    @Override
    protected void onBindViewHolder(@NonNull BenificiaryViewHolder holder, int position, @NonNull Benificiary model) {

        holder.bindBenificiaryProfile(mContext ,model);
    }

    @NonNull
    @Override
    public BenificiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.benificiary_item_list, parent, false);
mContext =parent.getContext();
        return new BenificiaryViewHolder(view);
    }
}
