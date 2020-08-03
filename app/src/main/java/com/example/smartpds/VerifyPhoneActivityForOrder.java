package com.example.smartpds;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpds.model.Customers;
import com.example.smartpds.model.Distributer;
import com.example.smartpds.model.Review;
import com.example.smartpds.shop.OnCustomerInfo;
import com.example.smartpds.shop.OnGetInfoListner;
import com.example.smartpds.shop.OnGetStatusListner;
import com.example.smartpds.utils.ApiClient;
import com.example.smartpds.utils.ApiInterface;
import com.example.smartpds.utils.MessageResponse;
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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhoneActivityForOrder extends AppCompatActivity {


    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    String API_KEY,sessionId;
    private EditText editText;
    private Button signIn;
    TextView otpText;
    String phonenumber, userType, mobileNo, key,isDistributor , distributerOrderId;
    int totalAmount;
    int newPrice = 0;
    RatingBar shopRating;
    EditText review;
    DatabaseReference ratingReference,reviewReference , reviewschemaReference;
    FirebaseDatabase db;

    FirebaseAuthSettings firebaseAuthSettings;
    private String distributorMobileNo;
    private DatabaseReference distributerreference;
    private DatabaseReference customerReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_verification);
        userType = getIntent().getStringExtra("usertype");
        sessionId = getIntent().getStringExtra("sessionid");
        API_KEY = getIntent().getStringExtra("apikey");
        key = getIntent().getStringExtra("key");
        distributerOrderId = getIntent().getStringExtra("Distributerkey");
        mAuth = FirebaseAuth.getInstance();
        signIn = findViewById(R.id.buttonsignin);

        progressBar = findViewById(R.id.progressbar);
        db = FirebaseDatabase.getInstance();

        firebaseAuthSettings = mAuth.getFirebaseAuthSettings();
        editText = findViewById(R.id.edittextcode);
//        otpText = findViewById(R.id.textview);
//        otpText.setText("Enter OTP for your Order");
        userType = getIntent().getStringExtra("usertype");
        mobileNo = getIntent().getStringExtra("phonenumber");
        isDistributor = getIntent().getStringExtra("isDistributor");
        distributorMobileNo = getIntent().getStringExtra("distributormobile");
        totalAmount = Integer.parseInt(getIntent().getStringExtra("totalamount"));
        phonenumber = getIntent().getStringExtra("phonenumber");
//        sendVerificationCode("+91"+phonenumber);
        ratingReference = db.getReference("DistributorRatings/" + distributorMobileNo);
        reviewReference = db.getReference("DistributorReviews/" + distributorMobileNo);
        reviewschemaReference  = db.getReference("Reviews");
         distributerreference = db.getReference("Distributors/" + distributorMobileNo);
        customerReference = db.getReference("Customers/" + mobileNo);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }


                //new otp

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);

                Call<MessageResponse> call = apiService.verifyOTP(API_KEY,sessionId, code);

                call.enqueue(new Callback<MessageResponse>() {

                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {

                        try {
                            if(response.body().getStatus().equals("Success")){
                                placeOrder(code);
                                Intent i=new Intent(VerifyPhoneActivityForOrder.this,DashBoard.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(VerifyPhoneActivityForOrder.this, "Not matched", Toast.LENGTH_SHORT).show();
                                Log.d("Failure", response.body().getDetails()+"|||"+response.body().getStatus());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Log.e("ERROR", t.toString());
                    }

                });


                placeOrder(code);





















//                verifyCode(code);

//                checkQuantityAndPrice(new OnGetStatusListner() {
//                    @Override
//                    public void onStatus(boolean status) {
//
//                        if (status) {



//
//
//                        }
//                        else {
//                            try {
//                                showErrorDialog(VerifyPhoneActivityForOrder.this , "Product Quantity Error");
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });

//                Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.putExtra("mobile", phonenumber);
//                startActivity(intent);

            }
        });

    }

    private void checkQuantityAndPrice(OnGetStatusListner onGetStatusListner) {

        final DatabaseReference distributerProduct = FirebaseDatabase.getInstance().getReference("DistributorsProducts/" + distributorMobileNo);


        distributerProduct.addListenerForSingleValueEvent(new ValueEventListener() {

            boolean status = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot product : dataSnapshot.getChildren())
                {
                    String quantity = product.child("quantity").getValue(String.class);
                    int q = Integer.parseInt(quantity);
                    if (q==0)
                    {
                        status =false ;
                        break;
                    }
                }

                if (status)
                {
                    onGetStatusListner.onStatus(true);
                }
                else {
                    onGetStatusListner.onStatus(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void placeOrder(String code) {

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Orders/" + mobileNo);
        final DatabaseReference distributerOrdersRef = FirebaseDatabase.getInstance().getReference("Orders/" + distributorMobileNo);
        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart/" + mobileNo);
        final DatabaseReference customerWallet = FirebaseDatabase.getInstance().getReference("Customers/" + mobileNo).child("walletAmmount");
        final DatabaseReference distributorWallet = FirebaseDatabase.getInstance().getReference("Distributors/" + distributorMobileNo).child("walletAmmount");

        // Exist! Do whatever.
//        rootRef.child(key).child("orderPlaced").setValue("yes");
//        Toast.makeText(VerifyPhoneActivityForOrder.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();


        customerWallet.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Exist! Do whatever.
                    int amount = snapshot.getValue(Integer.class);

                    newPrice = amount - totalAmount;

                    if(amount<totalAmount){
                        try {
                            showErrorDialog(VerifyPhoneActivityForOrder.this, "Transaction Error");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(VerifyPhoneActivityForOrder.this, "You Have insufficient Wallet Balance", Toast.LENGTH_SHORT).show();
//                        Intent intent=new Intent(getApplicationContext(),DashBoard.class);
//                        intent.putExtra("mobile",mobileNo);
//                        startActivity(intent);
                    }
                    else {
                        distributorWallet.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // Exist! Do whatever.
                                    int amount = snapshot.getValue(int.class);
                                    newPrice = amount + totalAmount;
                                    Toast.makeText(VerifyPhoneActivityForOrder.this, "Distributor Wallet Updated Successfully", Toast.LENGTH_SHORT).show();
                                }


                                distributorWallet.setValue(newPrice).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        distributerOrdersRef.child(distributerOrderId).child("orderPlaced").setValue("yes");
                                        rootRef.child(key).child("orderPlaced").setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                updateQuantityAtDistributer();

                                            }
                                        });
                                    }
                                });

//                                cartRef.removeValue();  no need i fix it in  updateQuantityAtDistributer();

                                try{
                                    if (!isDistributor.equalsIgnoreCase("yes")) {
                                        showRatingDialog(VerifyPhoneActivityForOrder.this, "Rate This Distributor");
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DistributorDashBoard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("mobile", distributorMobileNo);
                                        startActivity(intent);
                                    }
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed, how to handle?

                            }
                        });
                        Toast.makeText(VerifyPhoneActivityForOrder.this, "Transaction done", Toast.LENGTH_SHORT).show();
                        customerWallet.setValue(newPrice);

                    }
                }

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

            }
        });



    }

    private void minusQuantity(String productname ,DatabaseReference productRef  , Integer quantity) {

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart/" + mobileNo);
        productRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                String currentValue = mutableData.getValue(String.class);
                if (currentValue == null) {
                 //   mutableData.setValue(1);
                } else {
                    int updatedQuantity = Integer.parseInt(currentValue) - quantity;
                    String updateQuantity = String.valueOf(updatedQuantity);
                    mutableData.setValue(updateQuantity);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                cartRef.child(productname).removeValue();

            }
        });
    }

    private void updateQuantityAtDistributer() {
        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart/" + mobileNo);
        final DatabaseReference distributerProduct = FirebaseDatabase.getInstance().getReference("DistributorsProducts/" + distributorMobileNo);


        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())

                    for (DataSnapshot cartSnapshot: dataSnapshot.getChildren()) {

                        if (cartSnapshot.getKey()!=null && !cartSnapshot.getKey().equals("status")) {
                            String quantity = Objects.requireNonNull(cartSnapshot.child("quanity").getValue(String.class));
                            String productName = cartSnapshot.getKey().toString();
                            int quantity1 = Integer.parseInt(quantity) ;
                            Log.d("TAG", "onDataChange: " + quantity +" productname : "+ productName + " productQuantity : "+ quantity1);
                            assert productName != null;
                            minusQuantity(productName, distributerProduct.child(productName).child("quantity"), quantity1);
                    }else {
                            cartRef.child("status").removeValue();
                        }


                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                        if (!task.isSuccessful()) {//for otp enable this

                            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Orders/" + mobileNo);
                            final DatabaseReference customerWallet = FirebaseDatabase.getInstance().getReference("Customers/" + mobileNo).child("walletAmmount");
                            final DatabaseReference distributorWallet = FirebaseDatabase.getInstance().getReference("Orders/" + mobileNo).child("walletAmmount");
                            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Exist! Do whatever.
                                       Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
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

                            customerWallet.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Exist! Do whatever.
                                        int amount = snapshot.getValue(int.class);


                                        if(amount<totalAmount){
                                            //DialogBox

                                            rootRef.child(key).child("orderPlaced").setValue("no");
                                            Toast.makeText(VerifyPhoneActivityForOrder.this, "Order cannot be placed", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(VerifyPhoneActivityForOrder.this, "You Have insufficient Wallet Balance", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(getApplicationContext(),DashBoard.class);
                                            intent.putExtra("mobile",mobileNo);
                                            startActivity(intent);
                                        }
                                        else{
                                            rootRef.child(key).child("orderPlaced").setValue("yes");
                                            int newPrice = amount - totalAmount;
                                            customerWallet.setValue(newPrice);
                                        Toast.makeText(VerifyPhoneActivityForOrder.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("mobile", mobileNo);
                                        startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed, how to handle?

                                }
                            });

                            distributorWallet.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Exist! Do whatever.
                                        int amount = snapshot.getValue(int.class);
                                        int newPrice = amount + totalAmount;
                                        distributorWallet.setValue(newPrice);
                                        Toast.makeText(VerifyPhoneActivityForOrder.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
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
//                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivityForOrder.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


    public void showErrorDialog(Activity activity, String msg) throws IOException {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.insufficientwallet);
//        myImage.setImageBitmap(mIcon_val);
//            myImage.setImageBitmap(myBitmap);


            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setTextSize(25);
            text.setText("Insufficient Wallet Amount");

        Button dialogButton1 = (Button) dialog.findViewById(R.id.btn1);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.btn2);
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("mobile", mobileNo);
                startActivity(intent);
            }
        });
        dialog.show();

    }

    public void showRatingDialog(Activity activity, String msg) throws IOException {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ratingdialog);
//        myImage.setImageBitmap(mIcon_val);
//            myImage.setImageBitmap(myBitmap);
        RatingBar shopRating= (RatingBar) dialog.findViewById(R.id.rating);
        EditText review=(EditText) dialog.findViewById(R.id.reviewtext);

        shopRating.setVisibility(View.VISIBLE);
        Button dialogButton1 = (Button) dialog.findViewById(R.id.btn1);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.btn2);
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rating = "" +shopRating.getRating();
                String reviewText=review.getText().toString();

                Review review1= new Review();
                review1.setRating(rating);
                review1.setReview(reviewText);
                review1.setShopid(distributorMobileNo);

                getDisributerInfo(distributorMobileNo, new OnGetInfoListner() {
                    @Override
                    public void onDistributerLoaded(Distributer distributor) {
                        review1.setShopname(distributor.getShopname());
                         Date date = Calendar.getInstance().getTime();
                        String formateddate = new SimpleDateFormat("dd/MM/yyyy").format(date);
                        review1.setDate(formateddate);
                        review1.setCity(distributor.getCity());
                        getCustomerName(mobileNo , new OnCustomerInfo() {
                            @Override
                            public void onCustomerInfoLoaded(Customers customer) {
                                review1.setCustomername(customer.getFname() + customer.getLname());
                                if (reviewText!="" && reviewText.length()!=2 && rating.length()!=0) {
                                        reviewschemaReference.child(mobileNo).setValue(review1);
                                    Toast.makeText(VerifyPhoneActivityForOrder.this, "Review Submitted Successfully", Toast.LENGTH_SHORT).show();
                                }
                                if (reviewText!="" && reviewText.length()!=2){
                                    reviewReference.child(mobileNo).setValue(reviewText);
                                    Toast.makeText(VerifyPhoneActivityForOrder.this, "Review Submitted Successfully", Toast.LENGTH_SHORT).show();
                                }
                                if (rating.length()!=0) {
                                    ratingReference.child(mobileNo).setValue(rating);
                                    Toast.makeText(VerifyPhoneActivityForOrder.this, "Rating Submitted Successfully", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                                Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("mobile", mobileNo);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
        dialog.show();

    }

    private void getCustomerName(String mobileNo , OnCustomerInfo onCustomerInfo) {
        customerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customers customer = dataSnapshot.getValue(Customers.class);
                onCustomerInfo.onCustomerInfoLoaded(customer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getDisributerInfo(String distributorMobileNo , OnGetInfoListner onGetInfoListner) {

        distributerreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             Distributer distributor = dataSnapshot.getValue(Distributer.class);
             onGetInfoListner.onDistributerLoaded(distributor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
