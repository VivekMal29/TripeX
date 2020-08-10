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
import com.vivek.tripanalyzer.ChatActivity;
import com.vivek.tripanalyzer.ProfileActivity;
import com.vivek.tripanalyzer.R;
import com.vivek.tripanalyzer.models.Chat;

import java.util.ArrayList;

public class RecyclerViewAdapterChat extends RecyclerView.Adapter <RecyclerViewAdapterChat.ViewHolder>{

    private Context context;
    private ArrayList<Chat> chatArrayList;
    public static int MSG_TYPE_LEFT =0;
    public static int MSG_TYPE_RIGHT =1;

    int memberId = ChatActivity.memberId;

    public RecyclerViewAdapterChat(Context context, ArrayList<Chat> chatArrayList) {
        this.context = context;
        this.chatArrayList = chatArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,null,false);
            return new ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,null,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterChat.ViewHolder holder, int position) {

        Chat chat =  chatArrayList.get(position);

        holder.message.setText(chat.getMessage());
//        holder.mem_name.setText(chat.getMemName());


        Log.d("member from activity Id", String.valueOf(memberId));
        Log.d("member chat id", String.valueOf(chat.getMemberId()));

        if((memberId !=chat.getMemberId())){
//            Log.d("Hello","hello");
            holder.mem_name.setText(chat.getMemName());
            if(!chat.getImageUrl().equals("default")){
                Glide.with(context).load(chat.getImageUrl()).into(holder.profile_pic);
            }
            else{
                holder.profile_pic.setImageResource(R.drawable.ic_account_circle_blackk_24dp);
            }

        }


    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView message;
        TextView mem_name;
        ImageView profile_pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            message = itemView.findViewById(R.id.messege);
            mem_name = itemView.findViewById(R.id.MemName);
            profile_pic = itemView.findViewById(R.id.profile_picture);

        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = chatArrayList.get(position);
        if(memberId==chat.getMemberId()){
            return MSG_TYPE_RIGHT;
        }
        else {
            return  MSG_TYPE_LEFT;
        }
    }
}
