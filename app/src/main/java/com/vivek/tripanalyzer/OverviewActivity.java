package com.vivek.tripanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vivek.tripanalyzer.adapter.RecyclerViewAdapterMemberTransactions;
import com.vivek.tripanalyzer.adapter.RecyclerViewAdapterTransactions;
import com.vivek.tripanalyzer.models.MemberTransactions;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapterMemberTransactions recyclerViewAdapterMemberTransactions;
    ArrayList<MemberTransactions> memberTransactionsArrayList;

    String tripKey;
    TextView totalExpend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Overview");

        Intent intent =  getIntent();
        Bundle b= intent.getExtras();

        if(b!=null){
            tripKey = (String) b.get("tripKey");
        }

        recyclerView  = findViewById(R.id.memberExpendList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalExpend = findViewById(R.id.totalExpend);

        memberTransactionsArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey).child("Total Expenditure");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    totalExpend.setText(String.valueOf( dataSnapshot.getValue()));
                }
                else {
                    Toast.makeText(OverviewActivity.this, "snapshot dunno exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey).child("Members");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        MemberTransactions memberTransactions = new MemberTransactions();

                        String name = (String) snapshot.child("name").getValue();
                        String image = (String) snapshot.child("imageUrl").getValue();
                        int amount = ((Long) snapshot.child("expenditure by this member").getValue()).intValue();
                        memberTransactions.setMemName(name);
                        memberTransactions.setAmount(amount);
                        memberTransactions.setImageUrl(image);

                        memberTransactionsArrayList.add(memberTransactions);


                    }
                    recyclerViewAdapterMemberTransactions = new RecyclerViewAdapterMemberTransactions(OverviewActivity.this,memberTransactionsArrayList);
                    recyclerView.setAdapter(recyclerViewAdapterMemberTransactions);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
