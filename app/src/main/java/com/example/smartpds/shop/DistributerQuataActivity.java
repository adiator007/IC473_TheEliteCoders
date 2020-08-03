package com.example.smartpds.shop;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.ProductListAdapter;
import com.example.smartpds.model.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class DistributerQuataActivity extends AppCompatActivity {

    private ProductListAdapter productListAdapter;
    private RecyclerView recyclerView;

    private  final  static String DISTRIBUTER_ID ="DistributerID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributer_quata);

        recyclerView=findViewById(R.id.recyclerView_Products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query DistributerList =firebaseDatabase.getReference("DistributorsProducts").child(getIntent().getStringExtra(DISTRIBUTER_ID));
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(DistributerList, Product.class)
                .build();
        productListAdapter = new ProductListAdapter(options);
        recyclerView.setAdapter(productListAdapter);
        Toast.makeText(this, "activity_product_list",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (productListAdapter!=null)
            productListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //   chatAdapter.stopListening();
    }


}
