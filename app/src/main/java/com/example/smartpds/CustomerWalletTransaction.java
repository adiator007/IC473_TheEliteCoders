package com.example.smartpds;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerWalletTransaction extends AppCompatActivity {
    private DatabaseReference mDatabase;
    String email,name,mobile;
    TextView wallet;
    long walletAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallettransaction);
        wallet=findViewById(R.id.amount);
        mobile=getIntent().getStringExtra("mobile");
        mDatabase = FirebaseDatabase.getInstance().getReference("Customers").child(mobile);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email = dataSnapshot.child("email").getValue(String.class);
                name  = dataSnapshot.child("fname").getValue(String.class);
                walletAmount=dataSnapshot.child("walletAmmount").getValue(long.class);
                wallet.setText(""+walletAmount);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
