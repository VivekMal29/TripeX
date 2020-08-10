package com.vivek.tripanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vivek.tripanalyzer.adapter.RecyclerViewAdapterChat;
import com.vivek.tripanalyzer.models.Chat;
import com.vivek.tripanalyzer.models.Trips;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapterChat recyclerViewAdapterChat;
    ArrayList<Chat> chatArrayList;
    EditText message_text;
    ImageButton send;
    String memName;
    String message;
    String tripKey;
    String imageUrl;
    public static int memberId;
    Chat chat;
    ConstraintLayout constraintLayout;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            tripKey = (String) b.get("tripKey");
            memName = (String) b.get("memberName");
            imageUrl = (String) b.get("imageUrl");
            memberId = (int) b.get("memberId");
        }
        Log.d("hello", String.valueOf(imageUrl));
        Log.d("hello", String.valueOf(memName));
        Log.d("hello", String.valueOf(memberId));
        Log.d("hello", String.valueOf(tripKey));
        constraintLayout = findViewById(R.id.cons_layout);


        chat = new Chat();
        chatArrayList = new ArrayList<>();
        readMessage(message,memberId);

        DatabaseReference referenceToTrip = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey);

        referenceToTrip.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("Name").getValue();
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        reference = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey).child("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String message = (String) snapshot.child("Message").getValue();
                    String memName = (String) snapshot.child("memberName").getValue();
                    String imageUrl = (String) snapshot.child("imageUrl").getValue();
                    int memberId = Integer.parseInt(snapshot.child("memberId").getValue().toString()) ;
                    Chat mChat = new Chat();
                    mChat.setMessage(message);
                    mChat.setMemName(memName);
                    mChat.setImageUrl(imageUrl);
                    mChat.setMemberId(memberId);
                    chatArrayList.add(mChat);
                    recyclerViewAdapterChat = new RecyclerViewAdapterChat(ChatActivity.this,chatArrayList);
                    recyclerView.setAdapter(recyclerViewAdapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.Chats);
        message_text = findViewById(R.id.edit_messege);
        send = findViewById(R.id.imageButton);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!message_text.getText().toString().equals("")) {
                    final HashMap<String, String> dataMap = new HashMap<>();
                    dataMap.put("Message", message_text.getText().toString());
                    dataMap.put("memberId", String.valueOf(memberId));
                    dataMap.put("memberName", String.valueOf(memName));
                    dataMap.put("imageUrl", String.valueOf(imageUrl));

                    message = message_text.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey).child("chats").push().setValue(dataMap);
                    readMessage(message,memberId);
                    message_text.setText(null);

                } else {
                    Toast.makeText(ChatActivity.this, "empty message", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void readMessage(String message,int memberId){
        reference = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey).child("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String Mmessage = (String) snapshot.child("Message").getValue();
                    int MmemberId = Integer.parseInt(snapshot.child("memberId").getValue().toString());
                    String MimageUrl =  snapshot.child("imageUrl").getValue().toString();
                    String MemName =  snapshot.child("memberName").getValue().toString();
                    Chat mChat = new Chat();
                    mChat.setMessage(Mmessage);
                    mChat.setMemberId(MmemberId);
                    mChat.setImageUrl(MimageUrl);
                    mChat.setMemName(MemName);
                    chatArrayList.add(mChat);
                    recyclerViewAdapterChat = new RecyclerViewAdapterChat(ChatActivity.this,chatArrayList);
                    recyclerView.setAdapter(recyclerViewAdapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
