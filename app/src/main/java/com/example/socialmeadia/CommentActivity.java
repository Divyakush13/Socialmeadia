package com.example.socialmeadia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.socialmeadia.Adapter.CommentAdapter;
import com.example.socialmeadia.Modal.Notification;
import com.example.socialmeadia.Modal.Post;
import com.example.socialmeadia.Modal.Comment;
import com.example.socialmeadia.databinding.ActivityCommentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {
    ActivityCommentBinding binding;
    Intent intent;
    String postId;
    String postBy;

    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Comment> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCommentBinding.inflate(getLayoutInflater());

        setSupportActionBar(binding.toolbar2);
        CommentActivity.this.setTitle("comment");
        CommentActivity.this.setTitleColor(R.color.white);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        setContentView(binding.getRoot());
        intent=getIntent();

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        postId=intent.getStringExtra("postId");
        postBy=intent.getStringExtra("postedBy");


        database.getReference().child("posts")
                .child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post=snapshot.getValue(Post.class);
                Picasso.get().load(post.getPostImage())
                        .placeholder(R.drawable.placeholder)
                        .into(binding.postImage);
                binding.decription.setText(post.getPostDecription());
                binding.like.setText(post.getPostLike() + "");
                binding.comment.setText(post.getCommentCount() + "");

//                binding.comment.setText(post.getPostDecription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.getReference()
                .child("Users")
                .child(postBy).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user user=snapshot.getValue(com.example.socialmeadia.user.class);
                Picasso.get().load(user.getProfile())
                        .placeholder(R.drawable.placeholder)
                        .into(binding.profileImage);
                binding.name.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.commentPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Comment comment=new Comment();
                comment.setCommentBody(binding.commentET.toString());
                comment.setCommentAt(new Date().getTime());
                comment.setCommentBy(FirebaseAuth.getInstance().getUid());
                database.getReference()
                        .child("posts")
                        .child(postId)
                        .child("comment")
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference()
                                .child("posts")
                                .child(postId)
                                .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int commentCount=0;
                                if (snapshot.exists())
                                    commentCount=snapshot.getValue(int.class);

                                database.getReference()
                                        .child("posts")
                                        .child(postId)
                                        .child("commentCount")
                                        .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        binding.commentET.setText("");


                                        Notification notification = new Notification();
                                        notification.setNotificationBY(FirebaseAuth.getInstance().getUid());
                                        notification.setNotificationAt(new Date().getTime());
                                        notification.setPostId(postId);
                                        notification.setPostedBy(postBy);
                                        notification.setType("comment");


                                        FirebaseDatabase.getInstance().getReference()
                                                .child("notification")
                                                .child(postBy)
                                                .push()
                                                .setValue(notification);

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

            }
        });
        CommentAdapter adapter = new CommentAdapter(this,list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.commentRv.setLayoutManager(linearLayoutManager);
        binding.commentRv.setAdapter(adapter);
         database.getReference()
                 .child("posts")
                 .child(postId)
                 .child("comment").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 list.clear();
                 for (DataSnapshot dataSnapshot : snapshot.getChildren()){

//                     Comment comment = dataSnapshot.getValue(Comment.class);
//                     list.add(comment);
                 }
                 adapter.notifyDataSetChanged();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}