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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context context;
    ArrayList<SearchBook>list;

    public SearchAdapter(Context context, ArrayList<SearchBook> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_list_book,parent,false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        SearchBook searchBook=list.get(position);
        holder.bn.setText(searchBook.name);
        holder.br.setText(searchBook.rating);
        holder.by.setText(searchBook.year);
        Picasso.get().load(searchBook.cover).into(holder.bi);
        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(searchBook.url));
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
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView bi,url;
        TextView bn,br,by;
        LinearLayout desb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            by=itemView.findViewById(R.id.by);
            br=itemView.findViewById(R.id.br);
            bn=itemView.findViewById(R.id.bn);
            bi=itemView.findViewById(R.id.bi);
            url=itemView.findViewById(R.id.url);
            desb=itemView.findViewById(R.id.desb);
        }
    }
}
