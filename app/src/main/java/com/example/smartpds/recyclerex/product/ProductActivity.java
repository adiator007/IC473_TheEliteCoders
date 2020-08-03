package com.example.smartpds.recyclerex.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpds.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductActivity extends AppCompatActivity {

    Button SubmitButton ;
    String mobile;
    Button DisplayButton;


    EditText NameEditText, PhoneNumberEditText;

    // Declaring String variable ( In which we are storing firebase server URL ).
//    public static final String Firebase_Server_URL = "https://insertdata-android-examples.firebaseio.com/";

    // Declaring String variables to store name & phone number get from EditText.
    String NameHolder, NumberHolder;

//    Firebase firebase;

    DatabaseReference databaseReference;

    // Root Database Name for Firebase Database.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add_activity);

//        Firebase.setAndroidContext(MainActivity.this);

//        firebase = new Firebase(Firebase_Server_URL);

        databaseReference = FirebaseDatabase.getInstance().getReference("Simple").child("rice");
        mobile = getIntent().getStringExtra("mobile");
        SubmitButton = (Button)findViewById(R.id.submit);

        NameEditText = (EditText)findViewById(R.id.name);

        PhoneNumberEditText = (EditText)findViewById(R.id.phone_number);

        DisplayButton = (Button)findViewById(R.id.DisplayButton);

        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductDetails productDetails = new ProductDetails();

                GetDataFromEditText();

                // Adding name into class function object.
                productDetails.setQuantity(NameHolder);

                // Adding phone number into class function object.
                productDetails.setPrice(NumberHolder);

                // Getting the ID from firebase database.
                String productRecordIDFromServer = databaseReference.push().getKey();

                // Adding the both name and number values using student details class object using ID.
                databaseReference.child(productRecordIDFromServer).setValue(productDetails);

                // Showing Toast message after successfully data submit.
                Toast.makeText(ProductActivity.this,"Data Inserted Successfully into Firebase Database", Toast.LENGTH_LONG).show();

            }
        });

        DisplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProductActivity.this, ShowProductDetailsActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);

            }
        });

    }

    public void GetDataFromEditText(){

        NameHolder = NameEditText.getText().toString().trim();

        NumberHolder = PhoneNumberEditText.getText().toString().trim();

    }
}
