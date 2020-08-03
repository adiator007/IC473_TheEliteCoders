package com.example.smartpds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_verify_phone);

        userType=getIntent().getStringExtra("usertype");
        Toast.makeText(this, "User type= "+userType, Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        signIn = findViewById(R.id.buttonsignin);
        progressBar = findViewById(R.id.progressbar);
        firebaseAuthSettings = mAuth.getFirebaseAuthSettings();
        editText = findViewById(R.id.edittextcode);
        userType=getIntent().getStringExtra("usertype");
        mobileNo=getIntent().getStringExtra("mobile");
        phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
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
                            if (userType.equalsIgnoreCase("distributor")){
                                Toast.makeText(getApplicationContext() , "Distributer" ,Toast.LENGTH_LONG).show();

                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Distributors/" + mobileNo);
                                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // Exist! Do whatever.
                                            Intent intent = new Intent(VerifyPhoneActivity.this, DistributorDashBoard.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("mobile", mobileNo);
                                            startActivity(intent);

                                        } else {
                                            // Don't exist! Do something.
                                            Intent intent = new Intent(VerifyPhoneActivity.this, DistributorRegister.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("mobile", mobileNo);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed, how to handle?

                                    }

                                });

                            }
                            else
                            {
                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Customers/" + mobileNo);
                                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // Exist! Do whatever.
                                            Intent intent = new Intent(VerifyPhoneActivity.this, DashBoard.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("mobile", mobileNo);
                                            startActivity(intent);

                                        } else {
                                            // Don't exist! Do something.
                                            Intent intent = new Intent(VerifyPhoneActivity.this, CustomerRegister.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("mobile", mobileNo);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed, how to handle?

                                    }

                                });

                            }


                        } else {
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
                progressBar.setVisibility(View.VISIBLE);
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
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
