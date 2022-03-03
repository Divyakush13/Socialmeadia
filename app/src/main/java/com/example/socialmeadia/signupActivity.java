package com.example.socialmeadia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.socialmeadia.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class signupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         auth = FirebaseAuth.getInstance();
         database = FirebaseDatabase.getInstance();

         binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String email = binding.emailET.getText().toString(),password = binding.passwordET.getText().toString();
                 auth.createUserWithEmailAndPassword(email,password)
                         .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           user user = new user(binding.nameET.getText().toString(), binding.proffesionET.getText().toString(),email,password);
                           String id = task.getResult().getUser().getUid();
                           database.getReference().child("Users").child(id).setValue(user);
                           Toast.makeText(signupActivity.this, "Mission successful!!", Toast.LENGTH_SHORT).show();

                           Intent intent = new Intent(signupActivity.this,MainActivity.class);
                           startActivity(intent);
                       }
                       else {
                           Toast.makeText(signupActivity.this, "error", Toast.LENGTH_SHORT).show();
                       }
                     }
                 });
             }
         });
        binding.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}