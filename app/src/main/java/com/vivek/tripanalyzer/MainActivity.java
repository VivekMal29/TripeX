package com.vivek.tripanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.vivek.tripanalyzer.db_handler.DB_Handler;
import com.vivek.tripanalyzer.models.Trips;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    Button create;
    Button join;
    Button myTrips;


    DB_Handler db = new DB_Handler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Welcome To TripeX");

        create = findViewById(R.id.create);
        join = findViewById(R.id.join);
        myTrips = findViewById(R.id.myTrips);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateActivity.class));
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,JoinActivity.class));
            }
        });
//        for(int i=1;i<=5;i++){
//            db.deleteTrip(i);
//        }
//
//        db.deleteTrip(1);
        List<Trips> myTripsList = db.getAllTrips();
        for (Trips myTrips : myTripsList) {
            Log.d("Vivek_trip Name" , myTrips.getTrip_name());
            Log.d("Vivek_trip key" , myTrips.getTrip_key());
            Log.d("Vivek_trip Id" , String.valueOf(myTrips.getId()));
            Log.d("Vivek_trip mem Id" , String.valueOf(myTrips.getMemberId()));
            Log.d("Vivek_trip mem name" , String.valueOf(myTrips.getMemberName()));
        }
        myTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MytripsActivity.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,StartActivity.class));
                finish();
                return true;


        }
        return  false;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit TripeX")
                .setMessage("Are you sure you want to exit TripeX?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
