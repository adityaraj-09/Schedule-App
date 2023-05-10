package com.example.timble;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatGptAdapter extends RecyclerView.Adapter<ChatGptAdapter.ViewHolder> {
    Context context;
    ArrayList<ChatgptImage> list;

    public ChatGptAdapter(Context context, ArrayList<ChatgptImage> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatGptAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_chatgpt_mage,parent,false);
        return new ChatGptAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatGptAdapter.ViewHolder holder, int position) {
        ChatgptImage chatgptImage=list.get(position);
        Picasso.get().load(chatgptImage.url).into(holder.bi);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bi=itemView.findViewById(R.id.bi);
        }
    }
}
