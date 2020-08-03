package com.example.smartpds.orderview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.OrderAdapter;
import com.example.smartpds.adapter.OrderAdapterForDistributor;
import com.example.smartpds.model.Orders;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class DisplayOrdersActivityForDistributor extends AppCompatActivity {

    private static final String CUSTOMER_MOBILE_NUMBER = "customerMobileNumber";
    FirebaseDatabase db;
    DatabaseReference documentReference;
    private String orderId;
    private OrderAdapterForDistributor orderAdapter;
    private RecyclerView mrecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_orders);
        Intent intent = getIntent();

        orderId = intent.getStringExtra(CUSTOMER_MOBILE_NUMBER);
        db = FirebaseDatabase.getInstance();

        mrecyclerView=findViewById(R.id.recyclerView);

        documentReference = db.getReference("Orders/" + orderId);
        Toast.makeText(this, "orderID : "+orderId, Toast.LENGTH_SHORT).show();

        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        Query orderList =firebaseDatabase.getReference("Orders").child(orderId).orderByChild("orderPlaced").equalTo("yes");
        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(orderList, Orders.class)
                .build();
        orderAdapter = new OrderAdapterForDistributor(options);
        mrecyclerView.setAdapter(orderAdapter);



    }


    @Override
    protected void onStart() {
        super.onStart();
        if (orderAdapter!=null)
            orderAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        orderAdapter.stopListening();
    }
}
