package com.example.timble;

import static com.example.timble.otpActivity.MY_Prefernce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public  class MainActivity extends AppCompatActivity{
    FirebaseAuth auth;
    FirebaseDatabase database;
    ScheduleFragment scheduleFragment;
    private ScheduleViewModel scheduleViewModel;
    Handler mHandler;
    String clg,userI,name,phone;
    boolean night;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        final DrawerLayout drawerLayout=findViewById(R.id.drawer);
        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView=findViewById(R.id.navigation);
        View headerview=navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);
        LinearLayout header=(LinearLayout) headerview.findViewById(R.id.header);
        SwitchCompat mode=(SwitchCompat) headerview.findViewById(R.id.mode);

        sharedPreferences=getSharedPreferences("MODE",Context.MODE_PRIVATE);
        night=sharedPreferences.getBoolean("nightmode",false);
        if(night){
            mode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(night){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor=sharedPreferences.edit();
                    editor.putBoolean("nightmode",false);

                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor=sharedPreferences.edit();
                    editor.putBoolean("nightmode",true);
                }
                editor.apply();
            }
        });
        mHandler=new Handler();
        m_Runnable.run();
        SharedPreferences preferences=getSharedPreferences(MY_Prefernce, Context.MODE_PRIVATE);
        clg=preferences.getString("college","");
        name=preferences.getString("name","");
        phone=preferences.getString("phone","");
        userI=preferences.getString("uimg","https://firebasestorage.googleapis.com/v0/b/timble-db046.appspot.com/o/user.png?alt=media&token=57bdcbef-a7ed-4ab5-ba1d-dcb3a7dff1e5");
        CircleImageView profile=(CircleImageView)headerview.findViewById(R.id.profile);
        Picasso.get().load(userI).resize(400,400).centerCrop().into(profile);
        TextView n=(TextView) headerview.findViewById(R.id.name);
        n.setText(name);
        TextView p=(TextView) headerview.findViewById(R.id.no);
        p.setText("+91"+" "+phone);


        if(auth.getCurrentUser()!=null){
        }else{
            startActivity(new Intent(MainActivity.this,loginActivity.class));
            finish();
        }
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Profile_Activity.class));
            }
        });

        NavController navController= Navigation.findNavController(this,R.id.nav);
        NavigationUI.setupWithNavController(navigationView,navController);
        TextView header_title=findViewById(R.id.header_title);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                header_title.setText(navDestination.getLabel());
            }
        });
    }

    private final Runnable m_Runnable=new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(m_Runnable,2000);
        }
    };

    private void showBottomSheetDialog() {

    }
}