package com.example.smartpds.distributorscan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.ProductItemAdapter;
import com.example.smartpds.model.Cart;
import com.example.smartpds.model.Product;
import com.example.smartpds.shop.CartActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class AskForProductActivity extends AppCompatActivity {

    ProductItemAdapter productItemAdapter;
    RecyclerView recyclerView;
    Button proceedtocart;
    final static String DISTRIBUTER_MOBILE_NUMBER="distributer_mobile_number";
    private static final String USER_MOBILE_NUMBER = "user_mobile_number";
    FirebaseDatabase db;
    DatabaseReference documentReference , customerReference , customerKYCReference,quantityReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_product);

        recyclerView = findViewById(R.id.productsListRecyclerView);
        proceedtocart=findViewById(R.id.proceedtocart);

        db = FirebaseDatabase.getInstance();

        final String distributer = getIntent().getStringExtra(DISTRIBUTER_MOBILE_NUMBER);
        final String customer = getIntent().getStringExtra(USER_MOBILE_NUMBER);
        quantityReference = db.getReference("DistributorsProducts").child(distributer);
        recyclerView=findViewById(R.id.productsListRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        Query DistributerList =firebaseDatabase.getReference("DistributorsProducts").child(distributer);
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(DistributerList, Product.class)
                .build();
        productItemAdapter = new ProductItemAdapter(options,customer);
        recyclerView.setAdapter(productItemAdapter);


        initlistner(customer);  //productAdapter  Listners


        proceedtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                documentReference = db.getReference("Cart/"+ customer);
                Cart cart= new Cart(distributer , true );
                documentReference.child("status").setValue(cart);

                Intent intent = new Intent(AskForProductActivity.this, CartActivity.class);
                //        intent.putExtra(TOTAL_PRICE , totalPriceOfCart);
                intent.putExtra(USER_MOBILE_NUMBER ,customer );
                intent.putExtra("customerMobile" ,customer );
                intent.putExtra("distributorMobile",distributer);
                intent.putExtra("isDistributor","yes");
                startActivity(intent);
            }

        });


    }



    private void initlistner(String customer) {

        documentReference = db.getReference("Cart/"+ customer);

        productItemAdapter.setOnItemClickListener(new ProductItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {

               // Toast.makeText(getApplicationContext() , FirebaseAuth.getInstance().getUid(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void addQuantityClick(Product product, String productName, TextView v) {
                quantityReference.child(productName).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String productQuantityRemaining = dataSnapshot.getValue(String.class);
                        if (product!=null)
                        if (Integer.parseInt(productQuantityRemaining) > 0) {
                            int newQuantity1 = Integer.parseInt(product.getCartUserQuntity()) + 1;
                            String newQuantity = String.valueOf(newQuantity1);
                            product.setCartUserQuntity(newQuantity);
                            v.setText(newQuantity);
                            int price = Integer.parseInt(product.getCartPriceQuantity()) + Integer.parseInt(String.valueOf(product.getPrice()));
                            long newPrice = price;

                            documentReference.child(productName).child("quanity").setValue(newQuantity);
                            documentReference.child(productName).child("price").setValue(newPrice);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void removeQuantityClick(Product product , String productName  , TextView v) {

                int newQuantity1 = Integer.parseInt(product.getCartUserQuntity()) - 1;
                if (newQuantity1 >= 0) {
                    String newQuantity = String.valueOf(newQuantity1);
                    product.setCartUserQuntity(newQuantity);
                    v.setText(newQuantity);
                    int price = Integer.parseInt(product.getCartPriceQuantity()) - Integer.parseInt(String.valueOf(product.getPrice()));
                    long newPrice = price;
                    documentReference.child(productName).child("quanity").setValue(newQuantity);
                    documentReference.child(productName).child("price").setValue(newPrice);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (productItemAdapter!=null)
            productItemAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productItemAdapter.stopListening();
    }
}
