package com.example.smartpds.shop;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.DistributerListAdapter;
import com.example.smartpds.model.Distributer;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class DistributerListActivity extends AppCompatActivity {

    private DistributerListAdapter DistributerListAdapterAdapter;
    private RecyclerView recyclerView;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributer_list);
        mobile=getIntent().getStringExtra("mobile");
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query DistributerList =firebaseDatabase.getReference("Distributors").child(mobile);
        FirebaseRecyclerOptions<Distributer> options = new FirebaseRecyclerOptions.Builder<Distributer>()
                .setQuery(DistributerList, Distributer.class)
                .build();
        DistributerListAdapterAdapter = new DistributerListAdapter(options);
        recyclerView.setAdapter(DistributerListAdapterAdapter);
        Toast.makeText(this, "activity_distributer_list",Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onStart() {
        super.onStart();
        if (DistributerListAdapterAdapter!=null)
            DistributerListAdapterAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //   chatAdapter.stopListening();
    }


}
