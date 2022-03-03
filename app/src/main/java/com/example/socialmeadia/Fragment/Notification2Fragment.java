package com.example.socialmeadia.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmeadia.Adapter.NotificationAdapter;
import com.example.socialmeadia.Modal.Notification;
import com.example.socialmeadia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Notification2Fragment extends Fragment {

        RecyclerView notificationRV;
        ArrayList<Notification> list;
        FirebaseDatabase database;


    public Notification2Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification2, container, false);

     // recyclerView = view.findViewById(R.id.notification2RV);
       notificationRV = view.findViewById(R.id.notification2RV);

        list = new ArrayList<>();
//        list.add(new Notification(R.drawable.girl,"<b>Narendry Choudhary</b> mention you in comment : !!!  ",
//              "just now"));
//        list.add(new Notification(R.drawable.girl,"<b>Ayush gupta</b> mention you in comment : nice!!! ",
//                "1 hour ago"));
//        list.add(new Notification(R.drawable.girl,"<b>kunal soni</b> mention you in comment : great!!! ",
//                "2 hour"));
//        list.add(new Notification(R.drawable.girl,"<b>salman khan</b> mention you in comment : You rock!!! ",
//                "7 hour"));
//        list.add(new Notification(R.drawable.girl,"<b>Heral gour</b> like your comment  ",
//                "45 minuits"));
//        list.add(new Notification(R.drawable.girl,"<b>Saurabh kushwah</b> mention you in comment : Congratulation!!! ",
//                "6 hour"));
//        list.add(new Notification(R.drawable.girl,"<b>Saurabh kushwah</b> mention you in comment : Congratulation!!! ",
//                "6 hour"));
//        list.add(new Notification(R.drawable.girl,"<b>Saurabh kushwah</b> mention you in comment : Congratulation!!! ",
//                "6 hour"));
//        list.add(new Notification(R.drawable.girl,"<b>Saurabh kushwah</b> mention you in comment : Congratulation!!! ",
//                "6 hour"));

        NotificationAdapter adapter = new NotificationAdapter(list,getContext());
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        notificationRV.setLayoutManager(LayoutManager);
        notificationRV.setNestedScrollingEnabled(false);
        notificationRV.setAdapter(adapter);


        database.getReference()
                .child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Notification notification = dataSnapshot.getValue(Notification.class);
                            notification.setNotificationId(dataSnapshot.getKey());
                            list.add(notification);


                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        return view;
    }
}