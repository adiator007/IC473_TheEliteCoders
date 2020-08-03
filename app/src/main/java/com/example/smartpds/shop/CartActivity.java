package com.example.smartpds.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.VerifyPhoneActivityForOrder;
import com.example.smartpds.model.Product;
import com.example.smartpds.utils.ApiClient;
import com.example.smartpds.utils.ApiInterface;
import com.example.smartpds.utils.MessageResponse;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity {
    private final static String API_KEY = "fd77b9e1-d47a-11ea-9fa5-0200cd936042";
    private RecyclerView mrecyclerView;
    private CartAdapter productItemAdapter;
    private TextView totalPrice;
    Button chekoutButton;
    private static final String TOTAL_PRICE = "total_price";
    private static final String USER_MOBILE_NUMBER = "user_mobile_number";
    int totalPriceOfCart = 0;
    public static String uniqueUserId = "8668283745";
    String sessionId;
    public static String userId, distributorId;
    String isDistributor;

    TextView emptymsg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        chekoutButton = findViewById(R.id.checkout);
        mrecyclerView = findViewById(R.id.cartListRecyclerView);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalPrice = findViewById(R.id.totalamountvalue);
        emptymsg = findViewById(R.id.emptymsg);
        userId = getIntent().getStringExtra("customerMobile");
        distributorId = getIntent().getStringExtra("distributorMobile");
        isDistributor = getIntent().getStringExtra("isDistributor");


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Query cartList = firebaseDatabase.getReference("Cart").child(userId);
        final FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(cartList, Product.class)
                .build();
        productItemAdapter = new CartAdapter(options);
        mrecyclerView.setAdapter(productItemAdapter);


        final DatabaseReference checkoutReference = FirebaseDatabase.getInstance().getReference("Orders").child(userId);
        final DatabaseReference distributerOrdersRef = FirebaseDatabase.getInstance().getReference("Orders/" + distributorId);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cart").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.exists()) {
                        Product product = snapshot.getValue(Product.class);

                        long price = product.getPrice();
                        if (price != 0) {
                            long modelPricce = price;
                            totalPriceOfCart += modelPricce;
                        }
                    }
                    else {
                        emptymsg.setVisibility(View.VISIBLE);
                    }
                }

                String totalprice = String.valueOf(totalPriceOfCart);
                totalPrice.setText(totalprice);

                int total= Integer.parseInt(totalprice);
                if (total==0)
                {
                    emptymsg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        chekoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String total = totalPrice.getText().toString();
                int t = Integer.parseInt(total);

                if (t!=0)
                {
                    proceedForOrder();
                }
                else {
                    Toast.makeText(getApplicationContext() , "Your Cart is Empty ! " ,Toast.LENGTH_LONG ).show();
                }

    }


  void  proceedForOrder()
    {
        //                Order order=new Order("001",""+totalPriceOfCart,userId,distributorId,"no");
        String orderId = checkoutReference.push().getKey();
        String distributerOrderId =   distributerOrdersRef.push().getKey();

        String uniqueOrder = UUID.randomUUID().toString();

        checkoutReference.child(orderId).child("orderId").setValue(uniqueOrder);
        checkoutReference.child(orderId).child("totalAmount").setValue("" + totalPriceOfCart);
        checkoutReference.child(orderId).child("customer").setValue(userId);
        checkoutReference.child(orderId).child("distributor").setValue(distributorId);
        checkoutReference.child(orderId).child("timestamp").setValue(ServerValue.TIMESTAMP);
        checkoutReference.child(orderId).child("orderPlaced").setValue("no");

        distributerOrdersRef.child(distributerOrderId).child("orderId").setValue(uniqueOrder);
        distributerOrdersRef.child(distributerOrderId).child("totalAmount").setValue("" + totalPriceOfCart);
        distributerOrdersRef.child(distributerOrderId).child("customer").setValue(userId);
        distributerOrdersRef.child(distributerOrderId).child("distributor").setValue(distributorId);
        distributerOrdersRef.child(distributerOrderId).child("timestamp").setValue(ServerValue.TIMESTAMP);
        distributerOrdersRef.child(distributerOrderId).child("orderPlaced").setValue("no");

        Date date = new Date();
        //otp


                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);

                Call<MessageResponse> call = apiService.sentOTP(API_KEY, userId);
                call.enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        sessionId = response.body().getDetails();
                        Log.d("SenderID", sessionId);
                        Toast.makeText(CartActivity.this, "Otp sent"+sessionId, Toast.LENGTH_SHORT).show();
                        //you may add code to automatically fetch OTP from messages.
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Log.e("ERROR", t.toString());
                    }

                });




        //  Toast.makeText(CartActivity.this, "" + date.getTime(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CartActivity.this, VerifyPhoneActivityForOrder.class);
        intent.putExtra("phonenumber", userId);
        intent.putExtra("distributormobile", distributorId);
        intent.putExtra("key",orderId);
        intent.putExtra("Distributerkey",distributerOrderId);
        intent.putExtra("isDistributor",isDistributor);
        intent.putExtra("totalamount", "" + totalPriceOfCart);
        intent.putExtra("apikey", "" + API_KEY);
        intent.putExtra("sessionid", "" + sessionId);

        startActivity(intent);
        //    Toast.makeText(CartActivity.this, "Items will be added into firebase " + distributorId, Toast.LENGTH_SHORT).show();
    }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (productItemAdapter != null)
            productItemAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productItemAdapter.stopListening();
    }
}
