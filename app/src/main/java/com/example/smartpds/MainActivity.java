package com.example.smartpds;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpds.utils.CheckPermissions;

public class MainActivity extends AppCompatActivity {

String userType;
    private Spinner spinner;
    private EditText editText;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userType=getIntent().getStringExtra("usertype");
        Toast.makeText(this, "User type= "+userType, Toast.LENGTH_SHORT).show();
        spinner = findViewById(R.id.spinnercountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));



        if (CheckPermissions.checkWriteExternalPermission(getApplicationContext() ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
            }

        }


        editText = findViewById(R.id.edittextphone);

        findViewById(R.id.buttoncontinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

                String number = editText.getText().toString().trim();

                if (number.isEmpty() || number.length() != 10) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }

                String phoneNumber = "+" +code+ number;
//                Toast.makeText(MainActivity.this, "TimemStamp= "+ ServerValue.TIMESTAMP, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("phonenumber", phoneNumber);
                intent.putExtra("mobile", number);
                intent.putExtra("usertype",userType);
                Toast.makeText(MainActivity.this, "Welcome "+userType, Toast.LENGTH_SHORT).show();
                    startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            Intent intent = new Intent(this, ProfileActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            startActivity(intent);
//        }
    }
}
