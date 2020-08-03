package com.example.smartpds.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpds.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.HashMap;

public class QuantityAndPriceActivity extends AppCompatActivity {

    TextInputEditText quantity , price ;
    Button submit;
    private  final  static String PRODUCTID ="productID";
    private  final  static String DISTRIBUTER_ID ="DistributerID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity_and_price);

        quantity=findViewById(R.id.quantity_txt);
        price=findViewById(R.id.price_txt);
        submit=findViewById(R.id.submitbtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strquantity , strprice;
                strquantity =quantity.getText().toString();
                strprice=price.getText().toString();
                final DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("DistributorsProducts").child(getIntent().getStringExtra(DISTRIBUTER_ID)).child(getIntent().getStringExtra(PRODUCTID));


                final HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("quanity", strquantity);
                hashMap.put("price", strprice);

                productRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent distributerlist=new Intent(getApplicationContext(),DistributerQuataActivity.class);
                        distributerlist.putExtra(DISTRIBUTER_ID , getIntent().getStringExtra(DISTRIBUTER_ID));
                        startActivity(distributerlist);

                    }
                });
                Toast.makeText(getApplicationContext(),getIntent().getStringExtra(DISTRIBUTER_ID) +getIntent().getStringExtra(PRODUCTID) ,Toast.LENGTH_SHORT).show();

            }
        });



    }
}
