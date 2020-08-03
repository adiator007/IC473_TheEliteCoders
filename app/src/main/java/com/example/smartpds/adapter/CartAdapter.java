package com.example.smartpds.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class CartAdapter extends FirebaseRecyclerAdapter<Product, CartAdapter.CartViewHolder> {


    private OnItemClickListener mlistener;


    private RecyclerView.OnItemTouchListener mlistner;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CartAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    public  interface OnItemClickListener{
        void OnItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener=listener;
    }


    public static class CartViewHolder extends  RecyclerView.ViewHolder{
        public ImageView mItemIcon;
        public TextView mItemName,mItemPrice,mItemQuantity;




        public CartViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mItemIcon=itemView.findViewById(R.id.cartitemproducticon);
            mItemName=itemView.findViewById(R.id.cartitemproductname);
            mItemPrice=itemView.findViewById(R.id.cartitemproductprice);
            mItemQuantity=itemView.findViewById(R.id.cartitemQuantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.OnItemClick(position);

                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitem_layout,parent,false);
        CartViewHolder evh=new CartViewHolder(v,mlistener);
        return evh;
    }


    @Override
    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Product model) {

        final String productId=getSnapshots().getSnapshot(position).getKey().toString();
    if (productId.equalsIgnoreCase("rava")){
        holder.mItemIcon.setImageResource(R.drawable.rava);
    }
    else if(productId.equalsIgnoreCase("wheat")){
        holder.mItemIcon.setImageResource(R.drawable.wheat);
    }
    else if (productId.equalsIgnoreCase("peanuts")){
        holder.mItemIcon.setImageResource(R.drawable.peanuts);
    }
    else if (productId.equalsIgnoreCase("rice")){
        holder.mItemIcon.setImageResource(R.drawable.rice);
    }
    else if (productId.equalsIgnoreCase("oil")){
        holder.mItemIcon.setImageResource(R.drawable.oil);
    }

        holder.mItemName.setText(productId);
        holder.mItemPrice.setText((int) model.getPrice());
        holder.mItemQuantity.setText(model.getQuanity());
    }

}
