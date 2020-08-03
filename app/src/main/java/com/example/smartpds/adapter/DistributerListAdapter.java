package com.example.smartpds.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.viewHolder.DistributerListViewHolder;
import com.example.smartpds.model.Distributer;
import com.example.smartpds.shop.DistributerQuataActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class DistributerListAdapter extends FirebaseRecyclerAdapter<Distributer, DistributerListViewHolder> {


    Context mContext;
    private  final  static String DISTRIBUTER_ID ="DistributerID";
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DistributerListAdapter(@NonNull FirebaseRecyclerOptions<Distributer> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DistributerListViewHolder holder, int position, @NonNull final Distributer model) {


      final String DistributerID=getSnapshots().getSnapshot(position).getKey().toString();
        holder.distributerId.setText(DistributerID);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent distributerlist=new Intent(mContext, DistributerQuataActivity.class);
                distributerlist.putExtra(DISTRIBUTER_ID , DistributerID) ;
                mContext.startActivity(distributerlist);
            }
        });
    }

    @NonNull
    @Override
    public DistributerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

     View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_item_list, parent, false);
     mContext=parent.getContext();

        Toast.makeText(mContext,"onbind" ,Toast.LENGTH_SHORT).show();
        return new DistributerListViewHolder(view);
    }
}
