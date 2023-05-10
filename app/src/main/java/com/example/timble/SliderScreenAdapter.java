package com.example.timble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class SliderScreenAdapter extends PagerAdapter {
    Context context;
    List<SliderScreenModel> list;

    public SliderScreenAdapter(Context context, List<SliderScreenModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }



    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ViewGroup v=(ViewGroup) LayoutInflater.from(context).inflate(R.layout.slider_layout,container,false);
        ImageView SI;
        TextView SM,SH;
        SI=v.findViewById(R.id.SI);
        SM=v.findViewById(R.id.SM);
        SH=v.findViewById(R.id.SH);
        switch (position) {
            case 0:
                SI.setImageResource(list.get(0).ImageUrl);
                SH.setText(list.get(0).title);
                SM.setText(list.get(0).desc);
                break;
            case 1:
                SI.setImageResource(list.get(1).ImageUrl);
                SH.setText(list.get(1).title);
                SM.setText(list.get(1).desc);
                break;
            case 2:
                SI.setImageResource(list.get(2).ImageUrl);
                SH.setText(list.get(2).title);
                SM.setText(list.get(2).desc);
                break;
        }

        return v;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ViewGroup) object);
    }
}
