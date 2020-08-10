package com.vivek.tripanalyzer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vivek.tripanalyzer.R;
import com.vivek.tripanalyzer.models.Transactions;

import java.util.List;

public class RecyclerViewAdapterTransactions extends RecyclerView.Adapter<RecyclerViewAdapterTransactions.ViewHolder>  {
    private Context context;
    private List<Transactions> transactionsList;

    public RecyclerViewAdapterTransactions(Context context, List<Transactions> transactionsList) {
        this.context = context;
        this.transactionsList = transactionsList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterTransactions.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterTransactions.ViewHolder holder, int position) {
        Log.d("transac", String.valueOf(transactionsList.size()));
        Transactions transactions = transactionsList.get(position);
        holder.memName.setText(transactions.getMemName());
        holder.description.setText(transactions.getDescription());
        holder.amount.setText(String.valueOf(transactions.getAmount()));
        if(!transactions.getImageUrl().equals("default")){
            Glide.with(context).load(transactions.getImageUrl()).into(holder.imageView);
        }
        else{
            holder.imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }

    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView memName;
        private TextView description;
        private TextView amount;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            memName = itemView.findViewById(R.id.transName);
            description = itemView.findViewById(R.id.transDrep);
            amount = itemView.findViewById(R.id.transAmount);
            imageView = itemView.findViewById(R.id.profile_image);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
