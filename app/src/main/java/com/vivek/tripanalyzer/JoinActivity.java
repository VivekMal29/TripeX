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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vivek.tripanalyzer.db_handler.DB_Handler;
import com.vivek.tripanalyzer.models.Trips;

import java.util.List;
import java.util.TreeMap;

public class JoinActivity extends AppCompatActivity {


    EditText name;
    EditText phoneNumber;
    EditText email;
    EditText tripName;
    EditText tripKey;

    Button join;

    String memName;
    String memPhone;
    String memEmail;
    String tripname;
    String tripkey;
    int memberCount;
    String memberWithId;
    Trips trips;

    DB_Handler db = new DB_Handler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Join Trip");


        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.emailAddress);
        tripName = findViewById(R.id.tripName);
        tripKey = findViewById(R.id.tripKey);
        join = findViewById(R.id.create);

        final Intent intent = new Intent(this, TripActivity.class);


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memName = name.getText().toString();
                memPhone = phoneNumber.getText().toString();
                memEmail = email.getText().toString();

                tripkey = tripKey.getText().toString().toLowerCase().trim();

                Boolean isTripExist = false;


                trips = new Trips();

                List<Trips> myTripsList = db.getAllTrips();
                for (Trips myTrips : myTripsList) {
                    if (tripkey.equals(myTrips.getTrip_key().trim().toLowerCase())) {
                        Toast.makeText(JoinActivity.this, "You are Already Part of this Trip", Toast.LENGTH_SHORT).show();
                        isTripExist = true;
                    }
                }

                if (!isTripExist) {
                    Toast.makeText(JoinActivity.this, "Welcome to this Trip ", Toast.LENGTH_SHORT).show();

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        tripname = (String) dataSnapshot.getValue();

                                        trips.setTrip_name(tripname);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                trips.setTrip_key(tripkey);
                                trips.setMemberName(memName);
                                memberCount = (int) dataSnapshot.getChildrenCount();
                                Log.d("count", String.valueOf(memberCount + 1));
                                memberWithId = "member" + (memberCount + 1);
                                int memId = memberCount + 1;
                                trips.setMemberId(memId);
                                db.addTrip(trips);
                                intent.putExtra("memberId", memId);
                                intent.putExtra("tripKey", tripkey);
                                intent.putExtra("memberName", memName);
                                startActivity(intent);
                                FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child(memberWithId).child("id").setValue(memberCount + 1);
                                FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child(memberWithId).child("name").setValue(memName);
                                FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child(memberWithId).child("phone").setValue(memPhone);
                                FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child(memberWithId).child("email").setValue(memEmail);
                                FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child(memberWithId).child("expenditure by this member").setValue(0);
                                FirebaseDatabase.getInstance().getReference().child("Trips").child(tripkey).child("Members").child(memberWithId).child("imageUrl").setValue("default");




                            } else {
                                Toast.makeText(JoinActivity.this, "Trip Do Not Exist !!1 \n  Please verify KEY", Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }


            }
        });


    }

}
