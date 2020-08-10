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
import com.vivek.tripanalyzer.models.Member;

import java.util.ArrayList;

public class RecyclerViewAdapterMemberList extends RecyclerView.Adapter<RecyclerViewAdapterMemberList.ViewHolder>{

    private Context context;
    private ArrayList<Member> memberList;

    public RecyclerViewAdapterMemberList(Context context, ArrayList<Member> memberList) {
        this.context = context;
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterMemberList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_members,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMemberList.ViewHolder holder, int position) {
        Member member = memberList.get(position);
        holder.memName.setText(member.getName());
        holder.memEmail.setText(member.getEmail());
        holder.memPhone.setText(member.getPhoneNumber());
        if(!member.getImageUrl().equals("default")){
            Glide.with(context).load(member.getImageUrl()).into(holder.profile_pic);
        }
        else{
            holder.profile_pic.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }

    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView memName;
        private TextView memEmail;
        private TextView memPhone;
        private ImageView profile_pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            memName = itemView.findViewById(R.id.memberName);
            memEmail = itemView.findViewById(R.id.memEmail);
            memPhone = itemView.findViewById(R.id.memPhone);
            profile_pic = itemView.findViewById(R.id.profile_image);


        }

        @Override
        public void onClick(View v) {

        }
    }


}
