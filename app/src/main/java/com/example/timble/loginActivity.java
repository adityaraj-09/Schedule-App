package com.example.timble;

import static com.example.timble.otpActivity.MY_Prefernce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class loginActivity extends AppCompatActivity {
    EditText otp,phone;
    CircleImageView back;
    TextView login,send,btn_reg;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayAdapter<String > adapter1;
    String phoneno;
    String verificationId,number,group1;
    AutoCompleteTextView group;
    ProgressDialog progressDialog;
    TextView cap_war;
    String []items2={"IIT DeLHI"};
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        otp=findViewById(R.id.otp);
        back=findViewById(R.id.back);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        auth=FirebaseAuth.getInstance();
        phone=findViewById(R.id.phone);
        cap_war=findViewById(R.id.cap_war);
        group=findViewById(R.id.group);
        phone.requestFocus();
        login=findViewById(R.id.login);
        btn_reg=findViewById(R.id.btn_reg);
        send=findViewById(R.id.send);
        adapter1=new ArrayAdapter<String>(this,R.layout.list_items,items2);
        group.setAdapter(adapter1);
        group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                group1=parent.getItemAtPosition(position).toString();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(phone.getText().toString())){
                    phone.setError("Enter Phone number");
                }
                else{
                     number=phone.getText().toString();
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(auth)
                                    .setPhoneNumber("+91"+number)
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(loginActivity.this)
                                    .setCallbacks(mCallbacks)
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                    cap_war.setVisibility(View.VISIBLE);

                }
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this,RegisterActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                phoneno=phone.getText().toString();
                String OTP=otp.getText().toString();
                if(TextUtils.isEmpty(phoneno)  || TextUtils.isEmpty(OTP)) {
                    progressDialog.dismiss();
                    otp.setError("Enter OTP");
                }else if (phoneno.length()!=10){
                    progressDialog.dismiss();
                    phone.setError("Enter correct phone number");
                }else{
                    verifyotp(otp.getText().toString());
                }

            }
        });
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            cap_war.setVisibility(View.GONE);
            Toast.makeText(loginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s,token);
            verificationId=s;
            cap_war.setVisibility(View.GONE);
            Toast.makeText(loginActivity.this, "OTP sent to"+" "+"+91"+" "+number, Toast.LENGTH_SHORT).show();
        }
    };
    private void verifyotp(String Code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,Code);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    SharedPreferences.Editor editor=getSharedPreferences(MY_Prefernce,MODE_PRIVATE).edit();
                    editor.putString("phone",phoneno);
                    editor.putString("college",group1);
                    editor.apply();
                    progressDialog.dismiss();
                    startActivity(new Intent(loginActivity.this,InformationActivity.class));
                    finish();
                }else{
                    progressDialog.dismiss();
                    otp.setError("Enter valid otp");
                }
            }
        });
    }
}