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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vivek.tripanalyzer.Notification.Clent;
import com.vivek.tripanalyzer.Notification.Data;
import com.vivek.tripanalyzer.Notification.MyResponse;
import com.vivek.tripanalyzer.Notification.Sender;
import com.vivek.tripanalyzer.Notification.Token;
import com.vivek.tripanalyzer.adapter.RecyclerViewAdapterChat;
import com.vivek.tripanalyzer.models.Chat;
import com.vivek.tripanalyzer.models.Trips;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    APIService apiService;
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    boolean notify = false  ;


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

        apiService = Clent.getClient("https://fcm.googleapis.com/").create(APIService.class);

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
                notify=true;
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

                    FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey).child("Members").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                                String receiver = (String) snapshot.child("userId").getValue();
                                if(notify){
                                    sendNotification(receiver,memName,message_text.getText().toString());
                                }

                                notify=false;

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(ChatActivity.this, "empty message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final String msg = message_text.getText().toString();




    }

    private void sendNotification(final String receiver, final String memName, String toString) {
        DatabaseReference tokens  = FirebaseDatabase.getInstance().getReference("Tokens");
        Query   query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token =  snapshot.getValue(Token.class);
                    Data data = new Data(fUser.getUid(),R.mipmap.ic_launcher,memName+":"+message,tripKey,receiver);

                    Sender sender = new Sender(data , token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code()==200){
                                        if(response.body().success!=1){
                                            Toast.makeText(ChatActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
