package com.example.timble;

import static com.example.timble.otpActivity.MY_Prefernce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {


    RelativeLayout ll;
    TextView date,time,name;
    String nameSh;
    LinearLayout appbar;
    GifImageView gif;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ll=findViewById(R.id.ll);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        name=findViewById(R.id.name);
        SimpleDateFormat sdf=new SimpleDateFormat("HH");
        SimpleDateFormat cd=new SimpleDateFormat("MMMM d,yyyy");
        SimpleDateFormat ct=new SimpleDateFormat("HH:mm");
        time.setText(ct.format(new Date()));
        date.setText(cd.format(new Date()));
        gif=findViewById(R.id.gif);

        Window window = SplashActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        Integer integer= Integer.valueOf(sdf.format(new Date()));
        if (integer>17 || integer<7){
            window.setStatusBarColor(ContextCompat.getColor(SplashActivity.this,R.color.splash_status));
            ll.setBackgroundColor(getResources().getColor(R.color.black));
            name.setTextColor(Color.WHITE);
            date.setTextColor(Color.WHITE);
            time.setTextColor(Color.WHITE);
            gif.setImageResource(R.drawable.work);
        }else{
            window.setStatusBarColor(ContextCompat.getColor(SplashActivity.this,R.color.purple_500));

        }
        SharedPreferences preferences=getSharedPreferences(MY_Prefernce, MODE_PRIVATE);
        nameSh=preferences.getString("name","");
        name.setText(nameSh);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 2000);
    }
}