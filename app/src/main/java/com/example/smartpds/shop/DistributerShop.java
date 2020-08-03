package com.example.smartpds.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.ProductItemAdapter;
import com.example.smartpds.model.Cart;
import com.example.smartpds.model.Distributer;
import com.example.smartpds.model.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;


public class DistributerShop extends AppCompatActivity {

    private static final String TOTAL_PRICE = "total_price";
    private RecyclerView mrecyclerView;
   // private ShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase db;
    DatabaseReference documentReference,ratingReference,quantityReference;
    final static String DISTRIBUTER_MOBILE_NUMBER="distributer_mobile_number";
    String ShopId;
    TextView distributerName,shopLocation,shopPinCode,shopContact,shopName,quantity,price;
    Button proceedtocart;
    ImageView shopImage;
    String  strdistributerName;
    String strshopLocation;
    String strshopPinCode;
    String strshopContact;
    String customerMobile,distributorMobile;
    ProductItemAdapter productItemAdapter;
    RatingBar distributorRatingBar ;
    Button submitDistributorRating;
    private DatabaseReference DistributerReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributer_shop);
        Intent intent = getIntent();
        customerMobile=intent.getStringExtra("customermobile");
        ShopId = intent.getStringExtra("mobileno");
        distributorRatingBar= (RatingBar) findViewById(R.id.shoprating);
        distributerName=findViewById(R.id.Distributername);
        shopLocation=findViewById(R.id.Location);
        shopContact=findViewById(R.id.shopcontact);
        quantity=findViewById(R.id.cartitemQuantity);
        price=findViewById(R.id.txtproductcost);
        shopImage=findViewById(R.id.shop_image);
        shopPinCode=findViewById(R.id.Pincode);
        shopName=findViewById(R.id.shopName);
        proceedtocart=findViewById(R.id.proceedtocart);


        db = FirebaseDatabase.getInstance();


        //isuues fix distributer info not shown --raj
        DistributerReference = db.getReference("Distributors/" + ShopId);
        documentReference = db.getReference("Cart/" + ShopId);
        ratingReference = db.getReference("DistributorRatings/" + ShopId);
        quantityReference = db.getReference("DistributorsProducts").child(ShopId);
//        ratingReference = db.getReference("DistributorRatings/" + "0000077667");

        DistributerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Distributer shop = dataSnapshot.getValue(Distributer.class);

//                    strdistributerName=shop.getFname()+
//                            shop.getLname();
                    strshopLocation=shop.getAddress();
                    strshopPinCode= String.valueOf(shop.getPincode());
                    strshopContact= String.valueOf(shop.getMobile());
                    distributerName.setText(strdistributerName);
                    shopLocation.setText("Location:  "+strshopLocation);
                    shopContact.setText( "+91 "+strshopContact);
                    shopPinCode.setText("Pincode: "+strshopPinCode);
                    distributerName.setText(shop.getShopname());

                    Picasso.with(DistributerShop.this).load(shop.getShopImage()).into(shopImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ratingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float total=0,count=0,average;
                if (dataSnapshot.exists()) {
                    Map<String, String> value = (Map<String, String>)dataSnapshot.getValue();
                    for (Map.Entry<String, String> entry : value.entrySet()) {
                        count++;
                        total=total+Float.parseFloat(""+entry.getValue());

//                       Toast.makeText(DistributerShop.this, "Average Rating== "+(count-1)+" Total "+total, Toast.LENGTH_SHORT).show();
                    }
                    count--;
                    average=total/count;
                   // Toast.makeText(DistributerShop.this, "Average Rating== "+(count)+"Total " +total+" Average  "+average, Toast.LENGTH_SHORT).show();
//                    float rating = Float.parseFloat(dataSnapshot.child(dataSnapshot.getKey()).getValue(String.class));
//                    total = total + rating;
//                    count = count + 1;
//                    average = total / count;
//                    Toast.makeText(DistributerShop.this, "Average Rating== "+average, Toast.LENGTH_SHORT).show();
                    //                    Picasso.with(DistributerShop.this).load(shop.getShopImage()).into(shopImage);
                    try {
                        if (dataSnapshot.exists()) {
                            distributorRatingBar.setRating(average);///
                        }
                        else {
                            Toast.makeText(DistributerShop.this, "Rating not Found Please Rate", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mrecyclerView=findViewById(R.id.productsListRecyclerView);
        mrecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query DistributerList =firebaseDatabase.getReference("DistributorsProducts").child(ShopId);
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(DistributerList, Product.class)
                .build();
        productItemAdapter = new ProductItemAdapter(options,customerMobile);
        mrecyclerView.setAdapter(productItemAdapter);


        initlistner();  //productAdapter  Listners


        proceedtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                documentReference = db.getReference("Cart/"+ customerMobile);
                Cart cart= new Cart(ShopId , true );
                documentReference.child("status").setValue(cart);

                Intent intent = new Intent(DistributerShop.this, CartActivity.class);
                //        intent.putExtra(TOTAL_PRICE , totalPriceOfCart);
                intent.putExtra("customerMobile" ,customerMobile );
                intent.putExtra("distributorMobile",ShopId);
                intent.putExtra("isDistributor","no");
                startActivity(intent);
            }

        });

    }

    private void initlistner() {

        documentReference = db.getReference("Cart/"+ customerMobile);

        productItemAdapter.setOnItemClickListener(new ProductItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
//                Toast.makeText(getApplicationContext() , FirebaseAuth.getInstance().getUid(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void addQuantityClick(Product product, String productName, TextView v) {
            quantityReference.child(productName).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String productQuantityRemaining=dataSnapshot.getValue(String.class);
        if (product!=null)
        if (Integer.parseInt(productQuantityRemaining)>0){
            int newQuantity1 = Integer.parseInt(product.getCartUserQuntity())+1;
            String newQuantity = String.valueOf(newQuantity1);
            product.setCartUserQuntity(newQuantity);
            v.setText(newQuantity);
            int price = Integer.parseInt(product.getCartPriceQuantity()) +  Integer.parseInt(String.valueOf(product.getPrice()));
            long newPrice = price;

            documentReference.child(productName).child("quanity").setValue(newQuantity);
            documentReference.child(productName).child("price").setValue(newPrice);

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

            }

            @Override
            public void removeQuantityClick(Product product , String productName  , TextView v) {

                int newQuantity1 = Integer.parseInt(product.getCartUserQuntity())-1;
                if(newQuantity1>=0) {
                    String newQuantity = String.valueOf(newQuantity1);
                    product.setCartUserQuntity(newQuantity);
                    v.setText(newQuantity);
                    int price = Integer.parseInt(product.getCartPriceQuantity()) - Integer.parseInt(String.valueOf(product.getPrice()));
                    long newPrice = price;
                    documentReference.child(productName).child("quanity").setValue(newQuantity);
                    documentReference.child(productName).child("price").setValue(newPrice);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (productItemAdapter!=null)
            productItemAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productItemAdapter.stopListening();
    }
}
