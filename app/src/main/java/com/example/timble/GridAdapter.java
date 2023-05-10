package com.example.timble;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    Context context;
    ArrayList<Books>booksArrayList;

    public GridAdapter(Context context, ArrayList<Books> booksArrayList) {
        this.context = context;
        this.booksArrayList = booksArrayList;
    }

    @Override
    public int getCount() {
        return booksArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemView = convertView;
        if(listitemView==null){
            listitemView = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        }
        Books books= booksArrayList.get(position);

        ImageView bi,url;
        TextView bn,ba;
        LinearLayout desb;
        ba=listitemView.findViewById(R.id.ba);
        bn=listitemView.findViewById(R.id.bn);
        bi=listitemView.findViewById(R.id.bi);
        url=listitemView.findViewById(R.id.url);
        desb=listitemView.findViewById(R.id.desb);
        bn.setText(books.name);
        Picasso.get().load(books.cover).into(bi);
        ba.setText(books.author);
        bi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desb.setVisibility(desb.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
            }
        });
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(books.url));
                context.startActivity(i);
            }
        });
        return listitemView;
    }
}
