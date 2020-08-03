package com.example.smartpds.distributorscan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartpds.R;
import com.example.smartpds.adapter.BenififcaryDetailAdapter;
import com.example.smartpds.model.Benificiary;
import com.example.smartpds.model.Customers;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;


public class CustomerDetailActivity extends AppCompatActivity {

    final static String DISTRIBUTER_MOBILE_NUMBER="distributer_mobile_number";
    private static final String USER_MOBILE_NUMBER = "user_mobile_number";

    TextView customerName  ,customerMobileNo , customerAddress ;
    CircularImageView customerImage;
    FloatingActionButton askForProfuctButton;
    RecyclerView benificiaryRecyclerView ;

    FirebaseDatabase db;
    DatabaseReference documentReference , customerReference , customerKYCReference;
    private FirebaseDatabase firebaseDatabase;
    private BenififcaryDetailAdapter benificiaryadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        final String distributer = getIntent().getStringExtra(DISTRIBUTER_MOBILE_NUMBER);
        final String customer = getIntent().getStringExtra(USER_MOBILE_NUMBER);


        customerName = findViewById(R.id.customerName);
        customerMobileNo = findViewById(R.id.customerMobileNo);
        customerAddress = findViewById(R.id.customerAddress);
        customerImage = findViewById(R.id.customerImage);
        askForProfuctButton = findViewById(R.id.gotocart);
        benificiaryRecyclerView = findViewById(R.id.benificiaryRecyclerView);



        db = FirebaseDatabase.getInstance();
        customerReference = db.getReference("Customers/" + customer);
        customerKYCReference = db.getReference("CustomerKYC/" + customer);

        pupulateuserDetail(customer);
        initBenificiaryRecyclerView(customer);

        askForProfuctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent askforProduct=new Intent(getApplicationContext(), AskForProductActivity.class);
                askforProduct.putExtra(DISTRIBUTER_MOBILE_NUMBER ,distributer);
                askforProduct.putExtra(USER_MOBILE_NUMBER ,customer );
                startActivity(askforProduct);
            }
        });

    }

    private void initBenificiaryRecyclerView(String customer) {

        Query benificiaryList =db.getReference("Customers").child(customer).child("benificiary");
        FirebaseRecyclerOptions<Benificiary> options = new FirebaseRecyclerOptions.Builder<Benificiary>()
                .setQuery(benificiaryList, Benificiary.class)
                .build();

        benificiaryadapter = new BenififcaryDetailAdapter(options);
        benificiaryRecyclerView.setAdapter(benificiaryadapter);

    }

    private void pupulateuserDetail(final String customer) {

        customerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Customers customers = dataSnapshot.getValue(Customers.class);
                    customerName.setText(customers.getFname()+" "+customers.getLname());
                    customerAddress.setText(customers.getAddress());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        customerKYCReference.child("profilepic").child("url").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String url = dataSnapshot.getValue(String.class);
                    Glide.with(getApplicationContext()).load(url).into(customerImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (benificiaryadapter!=null)
            benificiaryadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        benificiaryadapter.stopListening();
    }


}
