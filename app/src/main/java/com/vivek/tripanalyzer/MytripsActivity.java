package com.vivek.tripanalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.vivek.tripanalyzer.adapter.RecyclerViewAdapterMyTrips;
import com.vivek.tripanalyzer.db_handler.DB_Handler;
import com.vivek.tripanalyzer.models.Trips;

import java.util.ArrayList;
import java.util.List;

public class MytripsActivity extends AppCompatActivity {

    ArrayList<Trips> tripsArrayList;
    RecyclerView recyclerView;
    RecyclerViewAdapterMyTrips recyclerViewAdapterMyTrips;

    DB_Handler db = new DB_Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrips);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Trips");

        recyclerView = findViewById(R.id.tripList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tripsArrayList = new ArrayList<>();

        tripsArrayList = (ArrayList<Trips>) db.getAllTrips();
        recyclerViewAdapterMyTrips = new RecyclerViewAdapterMyTrips(this,tripsArrayList);
        recyclerView.setAdapter(recyclerViewAdapterMyTrips);





    }
}
