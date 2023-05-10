package com.example.timble;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class otpActivity extends AppCompatActivity {
    TextView btn_otp,verify,btn_login;
    EditText name,otp;
    public static final String MY_Prefernce="Shared";
    String phone;
    AutoCompleteTextView group,sem;
    ArrayAdapter<String > adapter;
    ArrayAdapter<String > adapter1;
    CircleImageView img;
    FirebaseAuth auth;
    String verificationId;
    ActivityResultLauncher<String > mTakePhoto;
    Uri imageUri;
    String imageURI;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String name1,sem1,group1;
    ProgressDialog progressDialog;
    String []items1={"Girnar","Udaygiri","Nilgiri","Aravali","Zanskar","Jwalamukhi","Shivalik","Kumaon"};
    String []items2={"IIT DeLHI"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        img=findViewById(R.id.img);

        btn_login=findViewById(R.id.btn_login);
        name=findViewById(R.id.name);
        name.requestFocus();

        verify=findViewById(R.id.verify);
        otp=findViewById(R.id.otp);
        otp.requestFocus();
        group=findViewById(R.id.group);
        sem=findViewById(R.id.sem);
        phone=getIntent().getStringExtra("phone");
        verificationId=getIntent().getStringExtra("vid");
        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();
        adapter=new ArrayAdapter<String>(this,R.layout.list_items,items1);
        sem.setAdapter(adapter);
        sem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sem1=parent.getItemAtPosition(position).toString();
            }
        });
        adapter1=new ArrayAdapter<String>(this,R.layout.list_items,items2);
        group.setAdapter(adapter1);
        group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                group1=parent.getItemAtPosition(position).toString();
            }
        });


        mTakePhoto =registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        imageUri=result;
                        img.setImageURI(result);
                    }
                }
        );
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePhoto.launch("image/*");
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(otpActivity.this,loginActivity.class));
                finish();
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1=name.getText().toString();
                String OTP=otp.getText().toString();
                progressDialog.show();
                if(TextUtils.isEmpty(name1) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(OTP)|| TextUtils.isEmpty(sem1)|| TextUtils.isEmpty(sem1)) {
                    progressDialog.dismiss();
                    Toast.makeText(otpActivity.this, "All fields are Compulsory", Toast.LENGTH_SHORT).show();
                }
                 else if (sem1==null) {
                    Toast.makeText(otpActivity.this, "Select hostel", Toast.LENGTH_SHORT).show();
                } else if (group1==null) {
                    Toast.makeText(otpActivity.this, "Select institute", Toast.LENGTH_SHORT).show();
                } else{
                    verifyotp(otp.getText().toString());
                }
            }
        });
    }
    private void verifyotp(String Code) {
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationId,Code);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            DatabaseReference reference =database.getReference().child(group1).child("Users").child(Objects.requireNonNull(auth.getUid()));
                            StorageReference storageReference= storage.getReference().child(group1).child("Profile pic").child(auth.getUid());
                            if(imageUri!=null){
                                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()){
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageURI=uri.toString();
                                                    Users users=new Users(auth.getUid(),name1,phone,imageURI,group1,sem1);
                                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                    SharedPreferences.Editor editor=getSharedPreferences(MY_Prefernce,MODE_PRIVATE).edit();
                                                                    editor.putString("name",name1);
                                                                    editor.putString("phone",phone);
                                                                    editor.putString("college",group1);
                                                                    editor.putString("uimg",imageURI);
                                                                    editor.apply();
                                                                Toast.makeText(otpActivity.this, "account created", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(otpActivity.this, MainActivity.class));
                                                                finish();
                                                            }else{
                                                                Toast.makeText(otpActivity.this, "error ", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }else {
                                            progressDialog.dismiss();
                                            Toast.makeText(otpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                imageURI="https://firebasestorage.googleapis.com/v0/b/insien-9497e.appspot.com/o/user.png?alt=media&token=26015f24-c215-4dfb-af9d-d50482d5ed9c";
                                Users users=new Users(auth.getUid(),name1,phone,imageURI,group1,sem1);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(otpActivity.this,MainActivity.class));
                                            finish();
                                        }else{
                                            Toast.makeText(otpActivity.this, "error ", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }
}