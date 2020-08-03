package com.example.smartpds.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductItemAdapter  extends FirebaseRecyclerAdapter<Product, ProductItemAdapter.ProductViewHolder> {

    private ArrayList<Product> mExampleList;
    private OnItemClickListener mlistener;
    static Context contextView;
    String mobile;
    private RecyclerView.OnItemTouchListener mlistner;
    private DatabaseReference documentReference;
    private FirebaseDatabase db;


    public ProductItemAdapter(@NonNull FirebaseRecyclerOptions<Product> options , String userId) {
        super(options);
        this.mobile=userId;
    }

    public  interface OnItemClickListener{
        void OnItemClick(int position);
        void addQuantityClick(Product product, String productName, TextView v);
        void removeQuantityClick(Product product, String productName, TextView v);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener=listener;
    }


    public static class ProductViewHolder extends  RecyclerView.ViewHolder{
        public ImageView mimageView;
        public TextView mtextView1,mtextView2;
        public ImageButton quantiy_add , quantity_remove ;
        TextView quntity ;




        public ProductViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
//            contextView=itemView;
             mimageView=itemView.findViewById(R.id.imgproducticon);
            mtextView1=itemView.findViewById(R.id.txtproductname);
            mtextView2=itemView.findViewById(R.id.txtproductcost);
            quantity_remove=itemView.findViewById(R.id.quantity_remove_btn);
            quantiy_add=itemView.findViewById(R.id.quantity_add_btn);
            quntity=itemView.findViewById(R.id.cartitemQuantity);


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
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item,parent,false);
        ProductViewHolder evh=new ProductViewHolder(v,mlistener);
        return evh;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Product model) {


        final String productId = getSnapshots().getSnapshot(position).getKey().toString();

        if (!productId.equals("status")) {
            Log.d("OnBind", productId);
            //holder.mimageView.setImageResource(currentItem.getmImageResource());
            holder.mtextView1.setText(productId);
            String price = String.valueOf(model.getPrice());
            holder.mtextView2.setText(price);

            db = FirebaseDatabase.getInstance();
            documentReference = db.getReference("Cart/" + mobile).child(productId);
            // put FirebaseAuth.getInstance().getCurrentUser().getUid();  instead of uniqueUerId
            documentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String quantity = dataSnapshot.child("quanity").getValue(String.class);
                        model.setCartUserQuntity(quantity);
                        holder.quntity.setText(quantity);
                        if (productId.equalsIgnoreCase("rava")){
                            holder.mimageView.setImageResource(R.drawable.rava);
                        }
                        else if(productId.equalsIgnoreCase("wheat")){
                            holder.mimageView.setImageResource(R.drawable.wheat);
                        }
                        else if (productId.equalsIgnoreCase("peanuts")){
                            holder.mimageView.setImageResource(R.drawable.peanuts);
                        }
                        else if(productId.equalsIgnoreCase("oil")){
                            holder.mimageView.setImageResource(R.drawable.oil);
                        }
                        else if(productId.equalsIgnoreCase("rice")){
                            holder.mimageView.setImageResource(R.drawable.rice);
                        }
                        int price = Integer.parseInt(String.valueOf(model.getPrice())) * Integer.parseInt(quantity);
                        String newPrice = String.valueOf(price);
                        model.setCartPriceQuantity(newPrice);
                    } else {
                        model.setCartUserQuntity("0");
                        model.setCartPriceQuantity("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.quantiy_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.addQuantityClick(model, productId, holder.quntity);
                }
            });

            holder.quantity_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.removeQuantityClick(model, productId, holder.quntity);
                }
            });

        }
    }

    public  void refreshQuantity(String Quantity , int position)
    {

    }

}