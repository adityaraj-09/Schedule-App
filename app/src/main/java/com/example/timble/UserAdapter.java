package com.example.timble;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.circularstatusview.CircularStatusView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder>{
    Context homeActivity;
    ArrayList<Users> usersArrayList;


    public UserAdapter(HomeActivity homeActivity, ArrayList<Users> usersArrayList) {
        this.homeActivity=homeActivity;
        this.usersArrayList=usersArrayList;
    }
    @NonNull
    @Override
    public UserAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(homeActivity).inflate(R.layout.item_user_row,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.Viewholder holder, int position) {
        Users users=usersArrayList.get(position);
        holder.user_name.setText(users.name);
        holder.user_phone.setText(users.phone);
        Picasso.get().load(users.imageUri).resize(400,400).centerCrop().into(holder.user_profile);
        holder.chatl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view= LayoutInflater.from(homeActivity).inflate(R.layout.dialog_home,null);
                LinearLayout chat=view.findViewById(R.id.chat);
                LinearLayout call=view.findViewById(R.id.call);
                TextView tv= view.findViewById(R.id.tv);
                tv.setText("Contact "+users.name);
                AlertDialog.Builder builder=new AlertDialog.Builder(homeActivity);
                builder.setView(view);
                AlertDialog dialog=builder.create();
                dialog.show();
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent=new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+users.phone));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        homeActivity.startActivity(intent);
                    }
                });
            }
        });
        holder.user_profile.setOnClickListener(v -> {
            holder.lay_i.setVisibility(holder.lay_i.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
        });
    }

    private void callf(String phone) {

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }
    class Viewholder extends RecyclerView.ViewHolder{
        CircleImageView user_profile;
        TextView user_name;
        TextView user_phone;
        LinearLayout chatl,lay_i;
        CircularStatusView circular_status_view;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            user_profile=itemView.findViewById(R.id.user_img);
            user_phone=itemView.findViewById(R.id.user_phone);
            user_name=itemView.findViewById(R.id.user_name);
            chatl=itemView.findViewById(R.id.chatl);
            lay_i=itemView.findViewById(R.id.lay_i);

        }
    }
}
