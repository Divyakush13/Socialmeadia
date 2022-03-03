package com.example.socialmeadia.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.socialmeadia.Adapter.PostAdapter;
import com.example.socialmeadia.Adapter.StoryAdapter;
import com.example.socialmeadia.Modal.Post;
import com.example.socialmeadia.Modal.Story;
import com.example.socialmeadia.Modal.UserStory;
import com.example.socialmeadia.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {

    RecyclerView storyRv;
    ShimmerRecyclerView dashboardRV;
    ArrayList<Story> arrayList;
    ArrayList<Post> postList;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    RoundedImageView addStoryImage;
    ActivityResultLauncher<String> gallarylauncher;
    ProgressDialog dialog;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        dashboardRV = view.findViewById(R.id.dashboardRV);
        dashboardRV.showShimmerAdapter();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storyRv = view.findViewById(R.id.storyRv);
        arrayList = new ArrayList<>();
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("story uploding..");
        dialog.setMessage("please wait..");
        dialog.setCancelable(false);



        StoryAdapter adapter = new StoryAdapter(arrayList,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(false);
        storyRv.setAdapter(adapter);

        database.getReference()
                .child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    arrayList.clear();
                    for (DataSnapshot storySnapshot : snapshot.getChildren()){
                        Story story = new Story();
                        story.setStoryBy(storySnapshot.getKey());
                        story.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));
                        ArrayList<UserStory> stories = new ArrayList<>();
                        for (DataSnapshot snapshot1 : storySnapshot.child("userStories").getChildren()){
                            UserStory userStory = snapshot1.getValue(UserStory.class);
                            stories.add(userStory);
                        }
                        story.setStories(stories);
                        arrayList.add(story);

                    }
                    dashboardRV.setAdapter(adapter);
                    dashboardRV.hideShimmerAdapter();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        postList = new ArrayList<>();



        PostAdapter postAdapter= new PostAdapter(postList,getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dashboardRV.setLayoutManager(layoutManager);
        dashboardRV.setNestedScrollingEnabled(false);

        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                 for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                     Post post = dataSnapshot.getValue(Post.class);
                     post.setPostId(dataSnapshot.getKey());
                     postList.add(post);

                 }
                dashboardRV.setAdapter(postAdapter);
                dashboardRV.hideShimmerAdapter();
                 postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addStoryImage = view.findViewById(R.id.storyImg);
        addStoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallarylauncher.launch("image/*");
            }
        });

        gallarylauncher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                          addStoryImage.setImageURI(result);
                          dialog.show();
                          final StorageReference reference = storage.getReference()
                                  .child("stories")
                                  .child(FirebaseAuth.getInstance().getUid())
                                  .child(new Date().getTime()+"");

                          reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                              @Override
                              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override

                                        public void onSuccess(Uri uri) {
                                            Story story = new Story();
                                            story.setStoryAt(new Date().getTime());
                                            database.getReference()
                                                    .child("stories")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .child("postedBy")
                                                    .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    UserStory userStory = new UserStory(uri.toString(), story.getStoryAt());
                                                    database.getReference()
                                                            .child("stories")
                                                            .child(FirebaseAuth.getInstance().getUid())
                                                            .child("userStories")
                                                            .push()
                                                            .setValue(story).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                             dialog.dismiss();
                                                        }
                                                    });


                                                }

                                            });
                                        }
                                    });
                              }
                          });
                    }
                });

        return view;

    }
}