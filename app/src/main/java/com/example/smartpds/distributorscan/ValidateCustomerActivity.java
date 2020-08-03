package com.example.smartpds.distributorscan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpds.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class ValidateCustomerActivity extends AppCompatActivity {

    Button verification_code_btn_verify;
    final static String DISTRIBUTER_MOBILE_NUMBER="distributer_mobile_number";
    private static final String USER_MOBILE_NUMBER = "user_mobile_number";


    EditText mobileno ;
    EditText otpCode;


    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private Button signIn;
    String phonenumber,userType,mobileNo;
    FirebaseAuthSettings firebaseAuthSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_customer);

        final String distributer = getIntent().getStringExtra(DISTRIBUTER_MOBILE_NUMBER);
        final String customer = getIntent().getStringExtra(USER_MOBILE_NUMBER);


        mAuth = FirebaseAuth.getInstance();
        mobileno =findViewById(R.id.verification_code_phone_number);
        otpCode = findViewById(R.id.edittextcode);

        mobileno.setText(customer);

     //   sendVerificationCode("+91"+customer);

        Log.d("TAG", "onCreate: customer " + customer);

        verification_code_btn_verify = findViewById(R.id.verification_code_btn_verify);
        verification_code_btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpCode.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {
                    otpCode.setError("Enter code...");
                    otpCode.requestFocus();
                    return;
                }
                Intent customerDetailActivity=new Intent(getApplicationContext(), CustomerDetailActivity.class);
                customerDetailActivity.putExtra(DISTRIBUTER_MOBILE_NUMBER ,distributer);
                customerDetailActivity.putExtra(USER_MOBILE_NUMBER ,customer );
                startActivity(customerDetailActivity);
            }
        });
    }



    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {



                        } else {



                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
       // progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(ValidateCustomerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    
}
