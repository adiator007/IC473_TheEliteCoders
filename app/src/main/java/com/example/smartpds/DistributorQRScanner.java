package com.example.smartpds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.smartpds.shop.DistributerShop;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class DistributorQRScanner extends AppCompatActivity implements View.OnClickListener {


    private Button buttonScan,showProducts;
    private TextView textViewName, textViewAddress;
String customerMobile;
    //qr code scanner object
    private IntentIntegrator qrScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributorqrscanner);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        customerMobile=getIntent().getStringExtra("customermobile");
        textViewName = (TextView) findViewById(R.id.textViewName);
        qrScan = new IntentIntegrator(this);
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
                    String mobile=result.getContents();
                    openShop(mobile);
                    Query DistributerList =firebaseDatabase.getReference("DistributorsProducts").child(mobile);
                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openShop(String mobile) {
        Toast.makeText(this, "Try block "+ mobile, Toast.LENGTH_LONG).show();

        Intent distributerShop=new Intent(DistributorQRScanner.this, DistributerShop.class);
        distributerShop.putExtra("mobileno" ,mobile );
        distributerShop.putExtra("customermobile" ,customerMobile );
        startActivity(distributerShop);
    }

    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }
}
