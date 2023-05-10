package com.example.timble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SliderActivity extends AppCompatActivity {

    SliderScreenAdapter sliderScreenAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    Button nxtbtn;
    int postion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        nxtbtn=findViewById(R.id.nxtbtn);
        tabLayout=findViewById(R.id.tabIndicator);
        viewPager=findViewById(R.id.screenPager);

        List<SliderScreenModel>list=new ArrayList<>();
        list.add(new SliderScreenModel("Hello","Welcome",R.drawable.bg1));
        list.add(new SliderScreenModel("Hello1","Welcome",R.drawable.bg2));
        list.add(new SliderScreenModel("Hello2","Welcome",R.drawable.bg3));

        sliderScreenAdapter=new SliderScreenAdapter(this,list);
        viewPager.setAdapter(sliderScreenAdapter);
        tabLayout.setupWithViewPager(viewPager);
        sliderScreenAdapter.notifyDataSetChanged();
        postion=viewPager.getCurrentItem();

        nxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postion<list.size()){
                   postion++;
                   viewPager.setCurrentItem(postion);
                } else if (postion==list.size()) {
                    startActivity(new Intent(SliderActivity.this,MainActivity.class));
                    finish();
                }
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                postion=tab.getPosition();
                if (tab.getPosition()==list.size()-1){
                    nxtbtn.setText("Get Started");
                }
                else {
                    nxtbtn.setText("Next");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private  void setSliderScreenAdapter(List<SliderScreenModel>list){


    }
}