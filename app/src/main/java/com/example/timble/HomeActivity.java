package com.example.timble;

import static com.example.timble.otpActivity.MY_Prefernce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    RecyclerView mainrec;
    UserAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView alluser;
    String clg;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        alluser=findViewById(R.id.alluser);

        SharedPreferences preferences= getSharedPreferences(MY_Prefernce, Context.MODE_PRIVATE);
        clg=preferences.getString("college","");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersArrayList = new ArrayList<>();
        DatabaseReference reference = database.getReference().child(clg).child("Users");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mainrec = findViewById(R.id.mainrec);
        mainrec.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(HomeActivity.this, usersArrayList);
        mainrec.setAdapter(adapter);
        alluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainrec.setVisibility(mainrec.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
            }
        });
    }
}