package com.example.timble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder> {
    Context context;
    ArrayList<ParentModel> listofbooklist;

    public ParentAdapter(Context context, ArrayList<ParentModel> listofbooklist) {
        this.context = context;
        this.listofbooklist = listofbooklist;
    }

    @NonNull
    @Override
    public ParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.category_book_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentAdapter.ViewHolder holder, int position) {
        ParentModel parentModel=listofbooklist.get(position);
        holder.textView.setText(parentModel.title);
        BookAdapter bookAdapter;
        holder.rec.setLayoutManager(new GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,false));
        bookAdapter=new BookAdapter(context,parentModel.listofbooklist);
        holder.rec.setAdapter(bookAdapter);
        bookAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return listofbooklist==null?0:listofbooklist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView rec;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.title);
            rec=itemView.findViewById(R.id.rec);
        }
    }
}
