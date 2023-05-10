package com.example.timble;

import static android.content.Context.MODE_PRIVATE;
import static android.icu.number.NumberRangeFormatter.with;

import static com.example.timble.otpActivity.MY_Prefernce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timble.Util.Schedule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ScheduleFragment extends Fragment {

    FirebaseAuth auth;
    public  static String Visibility="visibility";
    LinearLayout sch_edit,sch_dlt,task,e1,e2,e3,e4,min1,e5,e6,min2,min3;
    ImageView min;

     ScheduleViewModel scheduleViewModel;

    FirebaseDatabase database;
    ImageView edit_sch;
    String name,group,dayn;
    BottomSheetDialog bottomSheetDialog;
    ArrayAdapter<String > adapter;
    LinearLayout cir_menu,ll1,newbox,newbox1,newbox2;
    FrameLayout fl;
    ScrollView sl;

    String []items1={"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    String day1,clg,nameF,userI;
    Handler mHandler;
    ImageView sm;
    TextView rmc,tvt,tvs,tvl,addnewbox,ns1,ns2,ns3,tvn1,tvn2,tvn3;
    private ScheduleShardeViewmodel scheduleShardeViewmodel;

    public ScheduleFragment(){
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_schedule, container, false);
        TextView sal=(TextView) view.findViewById(R.id.sal);
        TextView time=(TextView) view.findViewById(R.id.time);
        TextView tut=(TextView) view.findViewById(R.id.tut);TextView lab=(TextView) view.findViewById(R.id.lab);TextView lec=(TextView) view.findViewById(R.id.lec);
        ImageView users=(ImageView)  view.findViewById(R.id.users);
        edit_sch=view.findViewById(R.id.edit_sch);
        ll1=view.findViewById(R.id.ll1);
        sm=view.findViewById(R.id.sm);
        e1=view.findViewById(R.id.e1);
        e2=view.findViewById(R.id.e2);
        e3=view.findViewById(R.id.e3);
        ns1=view.findViewById(R.id.ns1);
        ns2=view.findViewById(R.id.ns2);
        ns3=view.findViewById(R.id.ns3);
        e4=view.findViewById(R.id.e4);
        e5=view.findViewById(R.id.e5);
        e6=view.findViewById(R.id.e6);
        min1=view.findViewById(R.id.min1);
        min2=view.findViewById(R.id.min2);
        min3=view.findViewById(R.id.min3);
        newbox=view.findViewById(R.id.newbox);
        newbox1=view.findViewById(R.id.newbox1);
        newbox2=view.findViewById(R.id.newbox2);
        tvl=view.findViewById(R.id.tvl);
        tvn1=view.findViewById(R.id.tvn1);tvn2=view.findViewById(R.id.tvn2);tvn3=view.findViewById(R.id.tvn3);
        tvs=view.findViewById(R.id.tvs);
        tvt=view.findViewById(R.id.tvt);
        sl=view.findViewById(R.id.sl);
        addnewbox=view.findViewById(R.id.addnewbox);
        rmc=view.findViewById(R.id.rmc);
        rmc.setSelected(true);
        fl=view.findViewById(R.id.fl);


        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();



        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.isSuccessful()){
                    String rc=mFirebaseRemoteConfig.getString("text");
                    if(rc!=null){
                        rmc.setText("\t \t \t \t \t \t"+rc+"\t \t \t \t \t \t");
                    }
                }
            }
        });



        // getting shared preference data
        Activity activity=getActivity();
        if(activity!=null && isAdded()){
            SharedPreferences preferences= getActivity().getSharedPreferences(MY_Prefernce,MODE_PRIVATE);
            String sch_L=preferences.getString("Sch_L","Lecture ");
            String sch_T=preferences.getString("Sch_T","Tutorial ");
            String sch_Lab=preferences.getString("Sch_lab","Lab ");
            String n1=preferences.getString("e4","");
            String n2=preferences.getString("e5","");
            String n3=preferences.getString("e6","");
            name=preferences.getString("name","User");
            clg=preferences.getString("college","");
            sal.setText("Hello"+" "+name+" !");
            tvl.setText(sch_L);
            tvt.setText(sch_T);
            tvs.setText(sch_Lab);
            tvn1.setText(n1);
            tvn2.setText(n2);
            tvn3.setText(n3);
        }





        SimpleDateFormat sdf=new SimpleDateFormat("HH");
        Integer integer= Integer.valueOf(sdf.format(new Date()));

        if (integer>17 || integer<7){
            sm.setImageResource(R.drawable.moon);
        }else{
            sm.setImageResource(R.drawable.cloud);
        }

        String  saveCurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("EEE d MMM");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat day = new SimpleDateFormat("EEE");
        dayn = day.format(calendar.getTime());
        sch_edit= view.findViewById(R.id.sch_edit);
        task=view.findViewById(R.id.task);
        min=view.findViewById(R.id.min);
        cir_menu=view.findViewById(R.id.cir_menu);
        sch_dlt=view.findViewById(R.id.sch_dlt);
        time.setText(saveCurrentDate);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        users.setOnClickListener(v -> {
            cir_menu.setVisibility(View.VISIBLE);
        });
        task.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(),TaskActivity.class));
            cir_menu.setVisibility(View.GONE);

        });
        min.setOnClickListener(v -> {
            cir_menu.setVisibility(View.GONE);
        });


        SharedPreferences preferences= getActivity().getSharedPreferences(Visibility,MODE_PRIVATE);
        String sch_=preferences.getString("newbox","g");
        String sch_1=preferences.getString("newbox1","g");
        String sch_2=preferences.getString("newbox2","g");


        if(sch_.equals("v")){
            newbox.setVisibility(View.VISIBLE);
        }
        if(sch_1.equals("v")){
            newbox1.setVisibility(View.VISIBLE);
        }
        if(sch_2.equals("v")){
            newbox2.setVisibility(View.VISIBLE);
        }


       addnewbox.setOnClickListener(v -> {
      if(newbox.getVisibility()==View.GONE){
          SharedPreferences.Editor editor1= requireActivity().getSharedPreferences(Visibility, MODE_PRIVATE).edit();
          editor1.putString("newbox","v");
          editor1.apply();
          newbox.setVisibility(View.VISIBLE);
      }else if(sch_.equals("v") && newbox1.getVisibility()==View.GONE ) {
          newbox1.setVisibility(View.VISIBLE);
          SharedPreferences.Editor editor1= requireActivity().getSharedPreferences(Visibility, MODE_PRIVATE).edit();
          editor1.putString("newbox1","v");
          editor1.apply();
      } else if (sch_1.equals("v")  ) {
          SharedPreferences.Editor editor1= requireActivity().getSharedPreferences(Visibility, MODE_PRIVATE).edit();
          editor1.putString("newbox2","v");
          editor1.apply();
          newbox2.setVisibility(View.VISIBLE);
      }
       });
       min1.setOnClickListener(v -> {
           SharedPreferences.Editor editor1= requireActivity().getSharedPreferences(Visibility, MODE_PRIVATE).edit();
           editor1.putString("newbox","g");
           editor1.apply();
           newbox.setVisibility(View.GONE);
       });
       min2.setOnClickListener(v -> {
           SharedPreferences.Editor editor1= requireActivity().getSharedPreferences(Visibility, MODE_PRIVATE).edit();
           editor1.putString("newbox1","g");
           editor1.apply();
           newbox1.setVisibility(View.GONE);
       });
        min3.setOnClickListener(v -> {
            SharedPreferences.Editor editor1= requireActivity().getSharedPreferences(Visibility, MODE_PRIVATE).edit();
            editor1.putString("newbox2","g");
            editor1.apply();
            newbox2.setVisibility(View.GONE);
        });

        if(auth.getCurrentUser()!=null){
            DatabaseReference reference=database.getReference().child(clg).child("Users").child(Objects.requireNonNull(auth.getUid()));
            reference.keepSynced(true);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    nameF=snapshot.child("name").getValue().toString();
                    userI=snapshot.child("imageUri").getValue().toString();
                    Activity activity=getActivity();
                    if(activity!=null && isAdded()){
                        SharedPreferences.Editor editor= requireActivity().getSharedPreferences(MY_Prefernce, MODE_PRIVATE).edit();
                        editor.putString("name",nameF);
                        editor.putString("uimg",userI);
                        editor.apply();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        scheduleViewModel=new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
                .create(ScheduleViewModel.class);

            scheduleViewModel.getsh(dayn).observe(getActivity(), schedule -> {
                    if(schedule!=null){
                        lab.setText(schedule.getLab());
                        tut.setText(schedule.getTut());
                        lec.setText(schedule.getLec());
                    }
            });
        return view;
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        e1.setOnClickListener(v13 -> {
            Dialog dialog = new Dialog(getActivity(), R.style.Dialoge);
            dialog.setContentView(R.layout.dialog_schedule);
            dialog.setCancelable(true);
            TextView yesBtn;
            TextInputEditText sType;
            yesBtn = dialog.findViewById(R.id.yesBtn);
            sType=dialog.findViewById(R.id.sType);
            sType.setText(tvl.getText().toString());
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String st=sType.getText().toString();
                    if(st.isEmpty()){
                        sType.setError("enter name");
                    }else {
                        dialog.dismiss();
                        SharedPreferences.Editor editor=requireActivity().getSharedPreferences(MY_Prefernce,MODE_PRIVATE).edit();
                        editor.putString("Sch_L",st);
                        editor.apply();
                    }
                }
            });
            dialog.show();
        });
        e2.setOnClickListener(v13 -> {
            Dialog dialog = new Dialog(getActivity(), R.style.Dialoge);
            dialog.setContentView(R.layout.dialog_schedule);
            dialog.setCanceledOnTouchOutside(true);
            TextView yesBtn;
            TextInputEditText sType;

            yesBtn = dialog.findViewById(R.id.yesBtn);
            sType=dialog.findViewById(R.id.sType);
            sType.setText(tvt.getText().toString());
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String st=sType.getText().toString();
                    if(st.isEmpty()){
                        sType.setError("enter name");
                    }else {
                        dialog.dismiss();
                        SharedPreferences.Editor editor=requireActivity().getSharedPreferences(MY_Prefernce,MODE_PRIVATE).edit();
                        editor.putString("Sch_T",st);
                        editor.apply();
                    }
                }
            });
            dialog.show();
        });
        e3.setOnClickListener(v13 -> {
            Dialog dialog = new Dialog(getActivity(), R.style.Dialoge);
            dialog.setContentView(R.layout.dialog_schedule);
            dialog.setCanceledOnTouchOutside(true);

            TextView yesBtn;
            TextInputEditText sType;
            yesBtn = dialog.findViewById(R.id.yesBtn);
            sType=dialog.findViewById(R.id.sType);
            sType.setText(tvs.getText().toString());
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String st=sType.getText().toString();
                    if(st.isEmpty()){
                        sType.setError("enter name");
                    }else {
                        dialog.dismiss();
                        SharedPreferences.Editor editor=requireActivity().getSharedPreferences(MY_Prefernce,MODE_PRIVATE).edit();
                        editor.putString("Sch_lab",st);
                        editor.apply();
                    }
                }
            });
            dialog.show();
        });
        e4.setOnClickListener(v13 -> {
            Dialog dialog = new Dialog(getActivity(), R.style.Dialoge);
            dialog.setContentView(R.layout.dialog_schedule);
            dialog.setCanceledOnTouchOutside(true);

            TextView yesBtn;
            TextInputEditText sType;
            yesBtn = dialog.findViewById(R.id.yesBtn);
            sType=dialog.findViewById(R.id.sType);
            sType.setText(tvn1.getText().toString());
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String st=sType.getText().toString();
                    if(st.isEmpty()){
                        sType.setError("enter name");
                    }else {
                        dialog.dismiss();
                        SharedPreferences.Editor editor=requireActivity().getSharedPreferences(MY_Prefernce,MODE_PRIVATE).edit();
                        editor.putString("e4",st);
                        editor.apply();
                    }
                }
            });
            dialog.show();
        });
        e5.setOnClickListener(v13 -> {
            Dialog dialog = new Dialog(getActivity(), R.style.Dialoge);
            dialog.setContentView(R.layout.dialog_schedule);
            dialog.setCanceledOnTouchOutside(true);

            TextView yesBtn;
            TextInputEditText sType;
            yesBtn = dialog.findViewById(R.id.yesBtn);
            sType=dialog.findViewById(R.id.sType);
            sType.setText(tvn2.getText().toString());
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String st=sType.getText().toString();
                    if(st.isEmpty()){
                        sType.setError("enter name");
                    }else {
                        dialog.dismiss();
                        SharedPreferences.Editor editor=requireActivity().getSharedPreferences(MY_Prefernce,MODE_PRIVATE).edit();
                        editor.putString("e5",st);
                        editor.apply();
                    }
                }
            });
            dialog.show();
        });
        e6.setOnClickListener(v13 -> {
            Dialog dialog = new Dialog(getActivity(), R.style.Dialoge);
            dialog.setContentView(R.layout.dialog_schedule);
            dialog.setCanceledOnTouchOutside(true);

            TextView yesBtn;
            TextInputEditText sType;
            yesBtn = dialog.findViewById(R.id.yesBtn);
            sType=dialog.findViewById(R.id.sType);
            sType.setText(tvn3.getText().toString());
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String st=sType.getText().toString();
                    if(st.isEmpty()){
                        sType.setError("enter name");
                    }else {
                        dialog.dismiss();
                        SharedPreferences.Editor editor=requireActivity().getSharedPreferences(MY_Prefernce,MODE_PRIVATE).edit();
                        editor.putString("e6",st);
                        editor.apply();
                    }
                }
            });
            dialog.show();
        });
        sch_edit.setOnClickListener(v -> {
            bottomSheetDialog=new BottomSheetDialog(getActivity(),R.style.BottomSheetStyle);
            View view1= LayoutInflater.from(getActivity()).inflate(R.layout.schedule_bottomsheet,(ScrollView) v.findViewById(R.id.sch));
            bottomSheetDialog.setContentView(view1);
            EditText labsch=(EditText) bottomSheetDialog.findViewById(R.id.addLabSch);
            EditText lecsch=(EditText) bottomSheetDialog.findViewById(R.id.addLecSch);
            EditText tutsch=(EditText) bottomSheetDialog.findViewById(R.id.addTutSch);
            Button schbtn=(Button) bottomSheetDialog.findViewById(R.id.addSch);Button delbtn=(Button) bottomSheetDialog.findViewById(R.id.delSch);
            AutoCompleteTextView daysch=(AutoCompleteTextView )bottomSheetDialog.findViewById(R.id.day);
            adapter=new ArrayAdapter<String>(getActivity(),R.layout.list_items,items1);
            daysch.setAdapter(adapter);
            daysch.setOnItemClickListener((parent, view2, position, id) -> {
                day1=parent.getItemAtPosition(position).toString();
            });
            assert schbtn != null;
            schbtn.setOnClickListener(v1 -> {
                String labs=labsch.getText().toString();
                String lecs=lecsch.getText().toString();
                String tuts=tutsch.getText().toString();
                scheduleShardeViewmodel=new ViewModelProvider(getActivity()).get(ScheduleShardeViewmodel.class);
                boolean edit=scheduleShardeViewmodel.isEdit();
                if(TextUtils.isEmpty(day1)){
                    Toast.makeText(getActivity(), "Select day ", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(day1) && !TextUtils.isEmpty(labs) && !TextUtils.isEmpty(tuts)&&  !TextUtils.isEmpty(lecs)) {
                    Schedule schedule=new Schedule(day1,lecs,labs,tuts);
                    ScheduleViewModel.insert(schedule);
                    bottomSheetDialog.dismiss();
                } else  if(!TextUtils.isEmpty(day1)) {
                    if (TextUtils.isEmpty(labs)){
                        labs="No lab";
                    }else if (TextUtils.isEmpty(lecs)){
                        lecs="No lecture";
                    } else if (TextUtils.isEmpty(tuts)) {
                        tuts="No tutorial";
                    }
                        Schedule schedule=new Schedule(day1,lecs,labs,tuts);
                        ScheduleViewModel.insert(schedule);
                        bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.show();
            cir_menu.setVisibility(View.GONE);
            delbtn.setOnClickListener(v12 -> {
                if(day1!=null){
                   ScheduleViewModel.delete(day1);
                   bottomSheetDialog.dismiss();
                }else {
                    Toast.makeText(getActivity(), "Select Day", Toast.LENGTH_SHORT).show();
                }
            });
        });
        edit_sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog=new BottomSheetDialog(getActivity(),R.style.BottomSheetStyle);
                View view1= LayoutInflater.from(getActivity()).inflate(R.layout.schedule_bottomsheet,(ScrollView) v.findViewById(R.id.sch));
                bottomSheetDialog.setContentView(view1);
                EditText labsch=(EditText) bottomSheetDialog.findViewById(R.id.addLabSch);
                EditText lecsch=(EditText) bottomSheetDialog.findViewById(R.id.addLecSch);
                EditText tutsch=(EditText) bottomSheetDialog.findViewById(R.id.addTutSch);
                Button schbtn=(Button) bottomSheetDialog.findViewById(R.id.addSch);Button delbtn=(Button) bottomSheetDialog.findViewById(R.id.delSch);
                AutoCompleteTextView daysch=(AutoCompleteTextView )bottomSheetDialog.findViewById(R.id.day);
                TextView tvt=(TextView)  bottomSheetDialog.findViewById(R.id.tvt);
                TextView tvs=(TextView)  bottomSheetDialog.findViewById(R.id.tvs);
                TextView tvl=(TextView)  bottomSheetDialog.findViewById(R.id.tvl);
                SharedPreferences preferences= getActivity().getSharedPreferences(MY_Prefernce,MODE_PRIVATE);
                String sch_L=preferences.getString("Sch_L","Lecture schedule");
                String sch_T=preferences.getString("Sch_T","Tutorial schedule");
                String sch_Lab=preferences.getString("Sch_lab","Lab schedule");
                tvl.setText(sch_L);
                tvt.setText(sch_T);
                tvs.setText(sch_Lab);
                adapter=new ArrayAdapter<String>(getActivity(),R.layout.list_items,items1);
                daysch.setAdapter(adapter);
                scheduleViewModel=new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
                        .create(ScheduleViewModel.class);
                scheduleViewModel.getsh(dayn).observe(getActivity(), schedule -> {
                    if(schedule!=null){
                        labsch.setText(schedule.getLab());
                        tutsch.setText(schedule.getTut());
                        lecsch.setText(schedule.getLec());
                    }
                });
                schbtn.setOnClickListener(v1 -> {
                    String dayedit=daysch.getEditableText().toString();
                    String labs=labsch.getText().toString();
                    String lecs=lecsch.getText().toString();
                    String tuts=tutsch.getText().toString();
                    if(TextUtils.isEmpty(dayedit)){
                        Toast.makeText(getActivity(), "Select day ", Toast.LENGTH_SHORT).show();
                    } else if (!TextUtils.isEmpty(dayedit) && !TextUtils.isEmpty(labs) && !TextUtils.isEmpty(tuts)&&  !TextUtils.isEmpty(lecs)) {
                        Schedule schedule=new Schedule(dayedit,lecs,labs,tuts);
                        ScheduleViewModel.insert(schedule);
                        bottomSheetDialog.dismiss();
                    }else {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
                bottomSheetDialog.show();
            }
        });
        sch_dlt.setOnClickListener(v -> {
            cir_menu.setVisibility(View.GONE);
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete");
            builder.setIcon(R.drawable.delete);
            builder.setMessage("Are you sure to delete?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                ScheduleViewModel.deleteAll();
                dialog.dismiss();
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        });

    }
}