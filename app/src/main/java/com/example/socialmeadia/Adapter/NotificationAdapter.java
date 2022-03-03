package com.example.socialmeadia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmeadia.CommentActivity;
import com.example.socialmeadia.Modal.Notification;
import com.example.socialmeadia.R;
import com.example.socialmeadia.databinding.Notification2sampleBinding;
import com.example.socialmeadia.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder>{

    ArrayList<Notification> list;
    Context context;

    public NotificationAdapter(ArrayList<Notification> list, Context context) {
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =LayoutInflater.from(context).inflate(R.layout.notification2sample,parent,false);


     return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
      Notification notification = list.get(position);

      String type = notification.getType();
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(notification.getNotificationBY())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        user user = snapshot.getValue(com.example.socialmeadia.user.class);
                        Picasso.get().load(user.getProfile())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.binding.profileImage);

                        if(type.equals("likes")){
                            holder.binding.notification.setText(Html.fromHtml("<b>" +user.getName()+"</b>"+" Liked your post"));

                    }
                        else if(type.equals("comment")){
                            holder.binding.notification.setText(Html.fromHtml("<b>" +user.getName()+"</b>"+" comment on your post"));

                        }else {

                            holder.binding.notification.setText(Html.fromHtml("<b>" +user.getName()+"</b>"+" Start following us"));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.opennotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                context.startActivity(intent);
                if (type.equals("follow")) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("notification")
                            .child(notification.getPostedBy())
                            .child(notification.getNotificationId())
                            .child("checjOpen")
                            .setValue(true);
                    holder.binding.opennotification.setBackgroundColor(Color.parseColor("#E6E3E3"));
                    intent.putExtra("postId",notification.getPostId());
                    intent.putExtra("postedBy",notification.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

            }
        });

        Boolean checjOpen = notification.isChecjOpen();
        if (checjOpen==true){
            holder.binding.opennotification.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }else {

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        Notification2sampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = Notification2sampleBinding.bind(itemView);

        }
    }
}
