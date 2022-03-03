package com.example.socialmeadia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmeadia.CommentActivity;
import com.example.socialmeadia.Modal.Notification;
import com.example.socialmeadia.Modal.Post;
import com.example.socialmeadia.R;
import com.example.socialmeadia.databinding.DashboardRvSampleBinding;
import com.example.socialmeadia.user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.util.ArrayList;

//import com.example.socialmeadia.databinding.DashboardRvSampleBinding;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder>{

    ArrayList<Post> list;
    Context context;

    public PostAdapter(ArrayList<Post> list, Context context) {
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =LayoutInflater.from(context).inflate(R.layout.dashboard_rv_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Post modal = list.get(position);
        Picasso.get()
                .load(modal.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.postImg);
        holder.binding.like.setText(modal.getPostLike()+"");
        holder.binding.comment.setText(modal.getCommentCount()+"");
        String description = modal.getPostDecription();

        if (description.equals("")){
            holder.binding.postDecription.setVisibility(View.GONE);

        }else{
            holder.binding.postDecription.setText(modal.getPostDecription());
            holder.binding.postDecription.setVisibility(View.VISIBLE);
        }

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(modal.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user user = snapshot.getValue(com.example.socialmeadia.user.class);
                Picasso.get()
                        .load(user.getProfile())
                        .placeholder(R.drawable.placeholder)
                        .into(holder.binding.profileImage);
                holder.binding.username.setText(user.getName());
                holder.binding.about.setText(user.getProfession());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(modal.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redheart,0,0,0);

                }else {
                    holder.binding.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("posts")
                                    .child(modal.getPostId())
                                    .child("likes")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(modal.getPostId())
                                            .child("postLike")
                                            .setValue(modal.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redheart,0,0,0);

                                            Notification notification = new Notification();

                                            notification.setNotificationBY(FirebaseAuth.getInstance().getUid());
                                         //   notification.setNotificationAt(new Date().getTime());
                                            notification.setPostId(modal.getPostId());
                                            notification.setPostedBy(modal.getPostedBy());
                                            notification.setType("like");


                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("notification")
                                                    .child(modal.getPostedBy())
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
        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId",modal.getPostId());
                intent.putExtra("postedBy",modal.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
       }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class  viewHolder extends RecyclerView.ViewHolder{
        DashboardRvSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DashboardRvSampleBinding.bind(itemView);
        }
    }

}
