package com.example.timble;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FictionAdapter extends RecyclerView.Adapter<FictionAdapter.ViewHolder> {

    Context context;
    List<Books> list;

    public FictionAdapter(Context context, List<Books> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FictionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_book,parent,false);
        return new FictionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FictionAdapter.ViewHolder holder, int position) {
        Books books=list.get(position);
        holder.bn.setText(books.name);
        holder.ba.setText("--"+books.author);
        Picasso.get().load(books.cover).into(holder.bi);
        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(books.url));
                context.startActivity(i);
            }
        });
        holder.bi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.desb.setVisibility(holder.desb.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bi,url;
        TextView bn,ba;
        LinearLayout desb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ba=itemView.findViewById(R.id.ba);
            bn=itemView.findViewById(R.id.bn);
            bi=itemView.findViewById(R.id.bi);
            url=itemView.findViewById(R.id.url);
            desb=itemView.findViewById(R.id.desb);
        }
    }
}
