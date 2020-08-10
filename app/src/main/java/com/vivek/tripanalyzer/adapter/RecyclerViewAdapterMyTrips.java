package com.vivek.tripanalyzer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vivek.tripanalyzer.R;
import com.vivek.tripanalyzer.TripActivity;
import com.vivek.tripanalyzer.models.Trips;

import java.util.ArrayList;

public class RecyclerViewAdapterMyTrips extends RecyclerView.Adapter<RecyclerViewAdapterMyTrips.ViewHolder> {
    private Context context;
    private ArrayList<Trips> tripsArrayList;


    public RecyclerViewAdapterMyTrips(Context context, ArrayList<Trips> tripsArrayList) {
        this.context = context;
        this.tripsArrayList = tripsArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterMyTrips.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trips,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMyTrips.ViewHolder holder, int position) {
        Trips trips = tripsArrayList.get(position);
        holder.tripName.setText(trips.getTrip_name());
    }

    @Override
    public int getItemCount() {
        return tripsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tripName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tripName = itemView.findViewById(R.id.nameOfTrip);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Trips trips = tripsArrayList.get(position);
            Intent intent = new Intent(context, TripActivity.class);
            intent.putExtra("tripKey",trips.getTrip_key());
            intent.putExtra("memberName",trips.getMemberName());
            intent.putExtra("memberId",trips.getMemberId());
            Log.d("pello_key",trips.getTrip_key());
            Log.d("pello_name",trips.getMemberName());
            Log.d("pello_id", String.valueOf(trips.getMemberId()));
            context.startActivity(intent);


        }
    }
}
