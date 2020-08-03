package com.example.smartpds;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ShowQuotaDistributor extends AppCompatActivity {
    private DatabaseReference mDatabaseProducts;
    private String mobile;
    int childrenCount;
    static int i=0;
    private String name,allQuota,price;
    ProductData[] myListData;
    DataSnapshot dataSnapshotMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_quota);
        mobile = getIntent().getStringExtra("mobile");

        mDatabaseProducts = FirebaseDatabase.getInstance().getReference("DistributorsProducts").child(mobile);


        mDatabaseProducts.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               childrenCount= (int) dataSnapshot.getChildrenCount();
                myListData=new ProductData[childrenCount];
                dataSnapshotMain=dataSnapshot;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    HashMap<String,Object> players= new HashMap();
//                    players.put(ds.getKey(), ds.getValue());
                    String name = ds.getKey();
                    Toast.makeText(ShowQuotaDistributor.this, "Child = "+name  , Toast.LENGTH_SHORT).show();
                    String quantity= (String) ds.child("quantity").getValue(String.class);
                    Toast.makeText(ShowQuotaDistributor.this, "Child = "+name +" Quantity "+quantity , Toast.LENGTH_SHORT).show();

                    String price=(String) ds.child("price").getValue(String.class);
                    myListData[i]=new ProductData(name, quantity, price);
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
  /*      for(DataSnapshot ds : dataSnapshotMain.getChildren()) {
            String name = ds.getKey();
            String quantity= (String) dataSnapshotMain.child(name).child("quantity").getValue();
                    String price=(String) dataSnapshotMain.child(name).child("price").getValue();
                    myListData[i]=new ProductData(name, quantity, price);
                    i++;
        }
*/         myListData = new ProductData[] {
                new ProductData("AA","Email", "0"),
                new ProductData("AA","Info", "0"),
                new ProductData("AA","Delete", "0"),
                new ProductData("AA","Dialer", "0"),
                new ProductData("AA","Alert", "0"),
                new ProductData("AA","Map", "0"),

        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ProductListAdapter adapter = new ProductListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
