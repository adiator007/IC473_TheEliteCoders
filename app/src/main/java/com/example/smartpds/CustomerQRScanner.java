package com.example.smartpds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpds.distributorscan.ValidateCustomerActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CustomerQRScanner extends AppCompatActivity implements View.OnClickListener {


    private Button buttonScan,showProducts;
    private TextView textViewName, textViewAddress;
String distributorMobile;
    final static String DISTRIBUTER_MOBILE_NUMBER="distributer_mobile_number";
    private static final String USER_MOBILE_NUMBER = "user_mobile_number";
    //qr code scanner object
    private IntentIntegrator qrScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributorqrscanner);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        distributorMobile =getIntent().getStringExtra("distributormobile");
        textViewName = (TextView) findViewById(R.id.textViewName);
        qrScan = new IntentIntegrator(this);

        qrScan.initiateScan();
        buttonScan.setOnClickListener(this);


    }
    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                String customerMobile=result.getContents();
                //if qr contains data
                try {
                    FirebaseApp.initializeApp(this);
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    //converting the data to json
//                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    textViewName.setVisibility(View.VISIBLE);
//                    textViewName.setText(obj.getString("name")+" Ration Shop");
//                    textViewAddress.setText(obj.getString("address"));

                    validateCustomer(customerMobile);
                    Query DistributerList =firebaseDatabase.getReference("DistributorsProducts").child(distributorMobile);
                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    validateCustomer(customerMobile);
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void validateCustomer(String customerMobile) {
        Intent distributerShop=new Intent(CustomerQRScanner.this, ValidateCustomerActivity.class);
        distributerShop.putExtra(DISTRIBUTER_MOBILE_NUMBER , distributorMobile);
        distributerShop.putExtra(USER_MOBILE_NUMBER , customerMobile);
        startActivity(distributerShop);
    }

    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }
}
