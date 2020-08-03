package com.example.smartpds.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.viewHolder.ProductListViewHolder;
import com.example.smartpds.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class ProductListAdapter extends FirebaseRecyclerAdapter<Product, ProductListViewHolder> {


    Context mContext;
    private  final  static String PRODUCTID ="productID";
    private  final  static String DISTRIBUTER_ID ="DistributerID";
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductListAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductListViewHolder holder, int position, @NonNull final Product model) {


        final String productId=getSnapshots().getSnapshot(position).getKey().toString();
        holder.productId.setText(productId);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent distributerlist=new Intent(mContext, QuantityAndPriceActivity.class);
                Intent intent = ((Activity) mContext).getIntent();
                distributerlist.putExtra(DISTRIBUTER_ID , intent.getStringExtra(DISTRIBUTER_ID));
                distributerlist.putExtra(PRODUCTID , productId) ;
                mContext.startActivity(distributerlist);
            }
        });
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_item_list, parent, false);
        mContext=parent.getContext();

        Toast.makeText(mContext,"onbind" , Toast.LENGTH_SHORT).show();
        return new ProductListViewHolder(view);
    }
}
