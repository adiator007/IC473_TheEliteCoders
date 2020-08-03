package com.example.smartpds.benificiary;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.BenificiaryAdapter;
import com.example.smartpds.model.Benificiary;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ViewBenificiaryActivity extends AppCompatActivity {

    private static final String USERID = "userid";
    RecyclerView benificiaryRecycleView;
    FirebaseDatabase firebaseDatabase;
    private BenificiaryAdapter benificiaryadapter;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_benificiary);
        benificiaryRecycleView=findViewById(R.id.benificiaryRecycleView);
        benificiaryRecycleView.setLayoutManager(new LinearLayoutManager(this));
        userId= getIntent().getStringExtra(USERID);

                firebaseDatabase= FirebaseDatabase.getInstance();
        Query benificiaryList =firebaseDatabase.getReference("Customers").child(userId).child("benificiary");
        FirebaseRecyclerOptions<Benificiary> options = new FirebaseRecyclerOptions.Builder<Benificiary>()
                .setQuery(benificiaryList, Benificiary.class)
                .build();

        benificiaryadapter = new BenificiaryAdapter(options);
        benificiaryRecycleView.setAdapter(benificiaryadapter);


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (benificiaryadapter!=null)
            benificiaryadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        benificiaryadapter.stopListening();
    }

}
