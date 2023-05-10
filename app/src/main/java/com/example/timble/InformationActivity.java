package com.example.timble;

import static com.example.timble.ScheduleFragment.Visibility;
import static com.example.timble.otpActivity.MY_Prefernce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class InformationActivity extends AppCompatActivity {

    TextView homebtn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String clg,nameF,userI,sch_,sch_1,sch_2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        homebtn=findViewById(R.id.homebtn);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this,MainActivity.class));
                finish();
            }
        });
        SharedPreferences preferences= getSharedPreferences(MY_Prefernce,MODE_PRIVATE);
        clg=preferences.getString("college","");

        if(auth.getCurrentUser()!=null){
            DatabaseReference reference=database.getReference().child(clg).child("Users").child(Objects.requireNonNull(auth.getUid()));
            reference.keepSynced(true);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    nameF=snapshot.child("name").getValue().toString();
                    userI=snapshot.child("imageUri").getValue().toString();
                        SharedPreferences.Editor editor= getSharedPreferences(MY_Prefernce, MODE_PRIVATE).edit();
                        editor.putString("name",nameF);
                        editor.putString("uimg",userI);
                        editor.apply();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            reference.child("Schedule").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    sch_=snapshot.child("newbox").getValue().toString();
                    sch_1=snapshot.child("newbox1").getValue().toString();
                    sch_2=snapshot.child("newbox2").getValue().toString();
                    SharedPreferences.Editor editor= getSharedPreferences(Visibility, MODE_PRIVATE).edit();
                    editor.putString("newbox",sch_);
                    editor.putString("newbox1",sch_1);
                    editor.putString("newbox2",sch_2);
                    editor.apply();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}