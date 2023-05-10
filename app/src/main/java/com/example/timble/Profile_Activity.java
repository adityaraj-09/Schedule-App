package com.example.timble;

import static com.example.timble.ScheduleFragment.Visibility;
import static com.example.timble.otpActivity.MY_Prefernce;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import soup.neumorphism.NeumorphButton;

public class Profile_Activity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    String i,g,s,n,p,clg;
    EditText name,group,sem;
    CircleImageView pimg;
    ImageView add;
    LinearLayout LO;
    ActivityResultLauncher<String > mTakePhoto;
    Uri imageUri;
    NeumorphButton save;
    ProgressDialog progressDialog;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        group=findViewById(R.id.group);
        sem=findViewById(R.id.sem);
        name=findViewById(R.id.name);
         pimg=findViewById(R.id.pimg);
         add=findViewById(R.id.add);
         LO=findViewById(R.id.LO);
         save=findViewById(R.id.save);
         storage= FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        SharedPreferences preferences=getSharedPreferences(MY_Prefernce, Context.MODE_PRIVATE);
        clg=preferences.getString("college","");
        StorageReference storageReference=storage.getReference().child(clg).child("Profile pic").child(Objects.requireNonNull(auth.getUid()));
        database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference().child(clg).child("Users").child(auth.getUid());
        reference.keepSynced(true);

        LO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("please wait..backup is going on..");
                progressDialog.setCancelable(false);
                Dialog dialog = new Dialog(Profile_Activity.this, R.style.Dialoge);
                dialog.setContentView(R.layout.dialog_layout);
                TextView yesBtn, noBtn,stw;
                yesBtn = dialog.findViewById(R.id.yesBtn);
                noBtn = dialog.findViewById(R.id.noBtn);
                stw=dialog.findViewById(R.id.stw);
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.show();
                        SharedPreferences preferences=getSharedPreferences(Visibility,MODE_PRIVATE);
                        String sch_=preferences.getString("newbox","g");
                        String sch_1=preferences.getString("newbox1","g");
                        String sch_2=preferences.getString("newbox2","g");
                        HashMap<String ,Object> hashMap=new HashMap<>();
                        hashMap.put("newbox",sch_);
                        hashMap.put("newbox1",sch_1);
                        hashMap.put("newbox2",sch_2);
                        stw.setVisibility(View.GONE);
                        reference.child("Schedule").updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    dialog.dismiss();
                                    SharedPreferences.Editor editor=getSharedPreferences(MY_Prefernce,MODE_PRIVATE).edit();
                                    editor.clear();
                                    editor.apply();
                                    SharedPreferences.Editor editor1=getSharedPreferences(Visibility,MODE_PRIVATE).edit();
                                    editor1.clear();
                                    editor1.apply();
                                    startActivity(new Intent(Profile_Activity.this, loginActivity.class));
                                    finishAffinity();
                                    auth.signOut();
                                }else {
                                    stw.setVisibility(View.VISIBLE);
                                    Toast.makeText(Profile_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        ActivityResultLauncher<Intent> launcher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                        imageUri=result.getData().getData();
                        pimg.setImageURI(imageUri);
                    }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){

                    }
                });

        if(auth.getCurrentUser()!=null){

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    i=snapshot.child("imageUri").getValue().toString();
                    g=snapshot.child("group").getValue().toString();
                    s=snapshot.child("hostel").getValue().toString();
                    n=snapshot.child("name").getValue().toString();
                    p=snapshot.child("phone").getValue().toString();
                    Picasso.get().load(i).resize(400,400).centerCrop().into(pimg);
                    name.setText(n);
                    group.setText(g);
                    sem.setText(s);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(Profile_Activity.this)
                        .crop()
                        .provider(ImageProvider.BOTH)
                        .createIntentFromDialog((Function1)(new Function1(){
                            public Object invoke(Object var1){
                                this.invoke((Intent)var1);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(@NotNull Intent it){
                                Intrinsics.checkNotNullParameter(it,"it");
                                launcher.launch(it);
                            }
                        }));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name1,group1,sem1;
                name1= name.getText().toString();
                group1=group.getText().toString();
                sem1=sem.getText().toString();
                if(imageUri!=null){
                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalImageUri=uri.toString();
                                    Users users=new Users(auth.getUid(),name1,p,finalImageUri,group1,sem1);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressDialog.dismiss();
                                                SharedPreferences.Editor editor=getSharedPreferences(MY_Prefernce,MODE_PRIVATE).edit();
                                                editor.putString("name",name1);
                                                editor.apply();
                                                Toast.makeText(Profile_Activity.this, "Data updated", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Profile_Activity.this,MainActivity.class));
                                                finish();
                                            }else{
                                                progressDialog.dismiss();
                                                Toast.makeText(Profile_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });

                }else{
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageUri=uri.toString();
                            Users users=new Users(auth.getUid(),name1,p,finalImageUri,group1,sem1);
                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                        SharedPreferences.Editor editor=getSharedPreferences(MY_Prefernce,MODE_PRIVATE).edit();
                                        editor.putString("name",name1);
                                        editor.apply();
                                        Toast.makeText(Profile_Activity.this, "Data updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Profile_Activity.this,MainActivity.class));
                                        finish();
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(Profile_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

    }


}