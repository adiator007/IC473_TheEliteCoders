package com.example.smartpds.recyclerex.product;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowProductDetailsActivity extends AppCompatActivity {


    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<ProductDetails> list = new ArrayList<>();
    String mobile;
    RecyclerView recyclerView;

    RecyclerView.Adapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student_details);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mobile = getIntent().getStringExtra("mobile");
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(ShowProductDetailsActivity.this));

        progressDialog = new ProgressDialog(ShowProductDetailsActivity.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

//        databaseReference = FirebaseDatabase.getInstance().getReference("Simple");
//        databaseReference = FirebaseDatabase.getInstance().getReference("DistributorsProducts").child(mobile);
        databaseReference = FirebaseDatabase.getInstance().getReference("Simple").child("rice");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ProductDetails productDetails = dataSnapshot.getValue(ProductDetails.class);

                    list.add(productDetails);
                }

                adapter = new ProductRecyclerViewAdapter(ShowProductDetailsActivity.this, list);

                recyclerView.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

    }
}