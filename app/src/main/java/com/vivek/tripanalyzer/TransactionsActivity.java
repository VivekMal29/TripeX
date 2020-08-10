package com.vivek.tripanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vivek.tripanalyzer.adapter.RecyclerViewAdapterTransactions;
import com.vivek.tripanalyzer.models.Transactions;

import java.util.ArrayList;
import java.util.Collection;

public class TransactionsActivity extends AppCompatActivity {

    String tripKey;
    ArrayList<Transactions> transactionsArrayList;
    RecyclerView recyclerView;
    RecyclerViewAdapterTransactions recyclerViewAdapterTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transactions");


        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null){
            tripKey = (String) b.get("tripKey");
        }

        recyclerView = findViewById(R.id.transactionList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transactionsArrayList =  new ArrayList<>();



        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey).child("Transactions");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Transactions transactions = new Transactions();
                    String memName = (String) snapshot.child("memberName").getValue();
                    String description = (String) snapshot.child("description").getValue();
                    String imageurl = (String) snapshot.child("imageUrl").getValue();
                    int amount = ((Long)snapshot.child("amount").getValue()).intValue();
                    transactions.setAmount(amount);
                    transactions.setDescription(description);
                    transactions.setMemName(memName);
                    transactions.setImageUrl(imageurl);


                    transactionsArrayList.add(transactions);

                }
                recyclerViewAdapterTransactions = new RecyclerViewAdapterTransactions(TransactionsActivity.this,transactionsArrayList);
                recyclerView.setAdapter(recyclerViewAdapterTransactions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
