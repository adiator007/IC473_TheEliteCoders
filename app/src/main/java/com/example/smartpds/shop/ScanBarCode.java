package com.example.smartpds.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpds.R;

public class ScanBarCode extends AppCompatActivity {

    EditText mobileNo;
    Button ScanShop;
    final static String DISTRIBUTER_MOBILE_NUMBER="distributer_mobile_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bar_code);
        mobileNo=findViewById(R.id.shopMobileNo);
        ScanShop=findViewById(R.id.scanShop);

        ScanShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strmobileno= mobileNo.getText().toString();

                if (!TextUtils.isEmpty(strmobileno))
                {
                    Intent distributerShop=new Intent(getApplicationContext(),DistributerShop.class);
                    distributerShop.putExtra(DISTRIBUTER_MOBILE_NUMBER ,strmobileno );
                    startActivity(distributerShop);
                }
            }
        });


    }
}
