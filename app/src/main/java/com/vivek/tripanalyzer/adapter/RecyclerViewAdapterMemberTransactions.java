package com.vivek.tripanalyzer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vivek.tripanalyzer.R;
import com.vivek.tripanalyzer.models.MemberTransactions;

import java.util.List;

public class RecyclerViewAdapterMemberTransactions extends RecyclerView.Adapter<RecyclerViewAdapterMemberTransactions.ViewHolder> {

    private Context context;
    private List<MemberTransactions> memberTransactionsList ;

    public RecyclerViewAdapterMemberTransactions(Context context, List<MemberTransactions> memberTransactionsList) {
        this.context = context;
        this.memberTransactionsList = memberTransactionsList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterMemberTransactions.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_expend_member,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMemberTransactions.ViewHolder holder, int position) {
        MemberTransactions memberTransactions = new MemberTransactions();
        memberTransactions = memberTransactionsList.get(position);

        holder.memName.setText(memberTransactions.getMemName());
        holder.memAmount.setText(String.valueOf(memberTransactions.getAmount()));
        if(!memberTransactions.getImageUrl().equals("default")){
            Glide.with(context).load(memberTransactions.getImageUrl()).into(holder.imageView);
        }
        else{
            holder.imageView.setImageResource(R.drawable.ic_account_circle_blackk_24dp);
        }

    }

    @Override
    public int getItemCount() {
        return memberTransactionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView memName;
        private TextView memAmount;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            memName = itemView.findViewById(R.id.MemName);
            memAmount = itemView.findViewById(R.id.totalExpendByMember);
            imageView = itemView.findViewById(R.id.profile_image);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
