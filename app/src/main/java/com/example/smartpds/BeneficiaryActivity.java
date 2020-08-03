package com.example.smartpds;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.smartpds.benificiary.AddBenificiaryActivity;
import com.example.smartpds.benificiary.ViewBenificiaryActivity;

public class BeneficiaryActivity extends AppCompatActivity {
    private static final String USERID = "userid";
    CardView addBeneficiary, viewBeneficiary;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary);
        mobile=getIntent().getStringExtra("mobile");
        addBeneficiary = findViewById(R.id.addbeneficiary);
        viewBeneficiary = findViewById(R.id.viewbeneficiary);
        addBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BeneficiaryActivity.this, "Add Benificiary", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BeneficiaryActivity.this, AddBenificiaryActivity.class);
                intent.putExtra(USERID , mobile);
                startActivity(intent);
            }
        });
        viewBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BeneficiaryActivity.this, "View Beneficiary", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BeneficiaryActivity.this, ViewBenificiaryActivity.class);
                intent.putExtra(USERID,mobile);
                startActivity(intent);
            }
        });

    }
}
