package com.vivek.tripanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vivek.tripanalyzer.adapter.RecyclerViewAdapterMemberList;
import com.vivek.tripanalyzer.models.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberList extends AppCompatActivity {


    String tripKey;
    ArrayList<Member> memberList;
    RecyclerView recyclerView;
    RecyclerViewAdapterMemberList recyclerViewAdapterMemberList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Members");

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            tripKey = (String) b.get("tripKey");
        }

        memberList = new ArrayList<>();
        recyclerView = findViewById(R.id.memberList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey).child("Members");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Member member = new Member();
                    member.setEmail((String) snapshot.child("email").getValue());
                    member.setName((String) snapshot.child("name").getValue());
                    member.setPhoneNumber((String) snapshot.child("phone").getValue());
                    member.setImageUrl((String) snapshot.child("imageUrl").getValue());

                    memberList.add(member);
                }

                recyclerViewAdapterMemberList = new RecyclerViewAdapterMemberList(MemberList.this,memberList);
                recyclerView.setAdapter(recyclerViewAdapterMemberList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
