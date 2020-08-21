package com.vivek.tripanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vivek.tripanalyzer.db_handler.DB_Handler;
import com.vivek.tripanalyzer.models.Trips;

import java.util.Collections;
import java.util.List;

public class CreateActivity extends AppCompatActivity {

    EditText name;
    EditText phoneNumber;
    EditText email;
    EditText tripName;
    EditText tripKey;

    Button create;


    String memName;
    String memPhone;
    String memEmail;
    String tripname;
    String tripkey;
    Trips trips;
    String userId;

    DB_Handler db = new DB_Handler(this);
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Trip");

        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.emailAddress);
        tripName = findViewById(R.id.tripName);
        tripKey = findViewById(R.id.tripKey);
        create = findViewById(R.id.create);

        final Intent intent = new Intent(this,TripActivity.class);

        userId = fUser.getUid();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                memName = name.getText().toString();
                memPhone = phoneNumber.getText().toString();
                memEmail = email.getText().toString();
                tripname = tripName.getText().toString();
                tripkey = tripKey.getText().toString().toLowerCase().trim();

                Log.d("check",tripkey);
                Log.d("check",tripname);
                Log.d("check",memName);
                Log.d("check",memPhone);
                Log.d("check",memPhone);

                trips = new Trips();
                trips.setTrip_name(tripname);
                trips.setTrip_key(tripkey);
                trips.setMemberName(memName);
                trips.setMemberId(1);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean isKeyExist = false;

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.d("snapkey", String.valueOf(snapshot.getKey()));
                            if(String.valueOf(snapshot.getKey()).toLowerCase().trim().equals(tripkey.toLowerCase().trim())){
                                isKeyExist =true;
                            }
                        }
                        if(isKeyExist){
                            Toast.makeText(CreateActivity.this, "Key Already Exist", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(CreateActivity.this, "Congrats !! \n Trip is been created ", Toast.LENGTH_SHORT).show();
                            db.addTrip(trips);
                            FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Name").setValue(tripname);
                            FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("tripImageUrl").setValue("default");
                            FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Total Expenditure").setValue(0);
                            FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child("member1").child("id").setValue(1);
                            FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child("member1").child("name").setValue(memName);
                            FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child("member1").child("phone").setValue(memPhone);
                            FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child("member1").child("email").setValue(memEmail);
                            FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child("member1").child("expenditure by this member").setValue(0);
                            FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child("member1").child("imageUrl").setValue("default");
                            FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child("member1").child("userId").setValue(userId);

                            intent.putExtra("tripKey",tripkey);
                            intent.putExtra("memberName",memName);
                            intent.putExtra("memberId",1);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }
        });


    }
}
