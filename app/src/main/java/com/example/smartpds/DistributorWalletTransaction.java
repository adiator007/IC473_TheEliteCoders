package com.example.smartpds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DistributorWalletTransaction extends AppCompatActivity {
    private DatabaseReference mDatabase;
    String email,name,mobile;
    TextView wallet;
    long walletAmount=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallettransaction);
        wallet=findViewById(R.id.amount);
        mobile=getIntent().getStringExtra("mobile");
        mDatabase = FirebaseDatabase.getInstance().getReference("Distributors").child(mobile);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String walletAmount="";
                email = dataSnapshot.child("email").getValue(String.class);
                name  = dataSnapshot.child("fname").getValue(String.class);

                    walletAmount=  dataSnapshot.child("walletAmmount").getValue().toString();
                    wallet.setText(""+walletAmount);



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
