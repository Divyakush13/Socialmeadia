package com.example.socialmeadia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmeadia.Modal.Follow;
import com.example.socialmeadia.Modal.Notification;
import com.example.socialmeadia.R;
import com.example.socialmeadia.databinding.UserSampleBinding;
import com.example.socialmeadia.user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder>{

    Context context;
    ArrayList<user> list;

    public UserAdapter(Context contextl, ArrayList<user> list) {
        this.context =contextl;
        this.list=list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view =LayoutInflater.from(context).inflate(R.layout.user_sample,parent,false);
      return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        user user = list.get(position);
        Picasso.get()
                .load(user.getProfile())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.profileImage);

        holder.binding.name.setText(user.getName());
        holder.binding.proffsion.setText(user.getProfession());

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(user.getUserID())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    holder.binding.followBtn.setBackground(ContextCompat.getDrawable(context,R.drawable.followingback));//unused
                    holder.binding.followBtn.setText("Following");
                    holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.black));
                    holder.binding.followBtn.setEnabled(true);
                }
                else {
                    holder.binding.followBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Follow follow = new Follow();
                            follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            follow.setFollowedAt(new Date().getTime());

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(user.getUserID())
                                    .child("followers")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(user.getUserID())
                                            .child("followercount")
                                            .setValue(user.getFollwerCount() +1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            holder.binding.followBtn.setBackground(ContextCompat.getDrawable(context,R.drawable.followingback));//unused
                                            holder.binding.followBtn.setText("Following");
                                            holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.white));
                                            holder.binding.followBtn.setEnabled(false);
                                            Toast.makeText(context, "You Followed " + user.getName(), Toast.LENGTH_SHORT).show();

                                            Notification notification = new Notification();
                                            notification.setNotificationBY(FirebaseAuth.getInstance().getUid());
                                            notification.setNotificationAt(new Date().getTime());
                                            notification.setType("follow");

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("notification")
                                                    .child(user.getUserID())
                                                    .push()
                                                    .setValue(notification);


                                        }
                                    });
                                }
                            });

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        UserSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = UserSampleBinding.bind(itemView);

        }
    }
}
