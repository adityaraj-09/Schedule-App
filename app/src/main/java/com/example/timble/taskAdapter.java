package com.example.timble;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timble.Util.Utils;
import com.google.android.material.chip.Chip;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;




public class taskAdapter extends RecyclerView.Adapter<taskAdapter.ViewHolder> {
    private final List<Task> taskList;
    private final ontaskClickListener clickListener;
    TimePickerDialog timePickerDialog;
    int mHour,mMinute,Hour,Minute;
    String tt;
    TaskViewModel taskViewModel;
    MediaPlayer music;

    public taskAdapter(List<Task> taskList,ontaskClickListener ontaskClickListener) {
        this.taskList = taskList;
        this.clickListener = ontaskClickListener;
    }

    @NonNull
    @Override
    public taskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row,parent,false);
        return new ViewHolder(view);
    }
    int songp;
     volatile boolean isPlaying;
    @Override
    public void onBindViewHolder(@NonNull taskAdapter.ViewHolder holder, int position) {
        Task task=taskList.get(position);
        String formatted= Utils.formatDate(task.getDuedate());
        holder.task.setText(task.getTask());
        holder.todaychip.setText(formatted);
        String dt1=task.getTt();
        String[] arrstr1=dt1.split(":",2);
        Hour= Integer.parseInt(arrstr1[0]);
        Minute= Integer.parseInt(arrstr1[1]);
        int min=Hour*60+Minute;
        Calendar calendar=Calendar.getInstance();
        int hourN=calendar.get(Calendar.HOUR_OF_DAY);
        int minN=calendar.get(Calendar.MINUTE);
        int Now=hourN*60+minN;
        music=new MediaPlayer();
        music.setAudioStreamType(AudioManager.STREAM_MUSIC);
        holder.alarm_time.setText(task.getTt());
        holder.pr.setOnClickListener(v -> {
            if(isPlaying){
                music.pause();
            }else {
                if(songp==0){
                    play(holder);
                }else {
                    music.start();
                }

            }
            isPlaying=!isPlaying;
        });


      holder.todaychip.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              play(holder);
          }
      });

        holder.alarm_time.setOnClickListener(v -> {
            String dt=task.getTt();
            String[] arrstr=dt.split(":",2);
            mHour= Integer.parseInt(arrstr[0]);
            mMinute= Integer.parseInt(arrstr[1]);
            timePickerDialog = new TimePickerDialog(v.getContext(),
                    (view12, hourOfDay, minute) -> {
                        tt=hourOfDay + ":" + minute;
                        timePickerDialog.dismiss();
                    }, mHour, mMinute, true );
            timePickerDialog.show();
        });
    }

    private  void play(ViewHolder holder){

        holder.sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int songprogress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                songprogress=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                songp=songprogress;
                music.seekTo(songprogress);
            }
        });
        int duration= musicplay()/1000;
        holder.sb.setVisibility(View.VISIBLE);
        holder.ll1.setVisibility(View.VISIBLE);
        holder.sb.setMax(duration);

        holder.dur.setText(duration/60+":"+duration%60);

        new Thread(){
            public void run(){
                songp=0;
                isPlaying=true;
                while ( songp<duration){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if(isPlaying){
                        songp++;
                        ((TaskActivity)holder.todaychip.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.sb.setProgress(songp);
                                if(songp>60){
                                    holder.ct.setText(String.valueOf(songp/60+":"+songp%60));
                                }else {
                                    holder.ct.setText("00:"+songp);
                                }

                            }
                        });
                    }


                }
                ((TaskActivity)holder.todaychip.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        music.pause();
                        songp=0;
                        isPlaying=false;
                        music.seekTo(songp);
                        holder.ct.setText("00");
                        holder.sb.setProgress(songp);
                        music=new MediaPlayer();

                    }
                });

            }
        }.start();
    }

    public int musicplay()
    {
        String  url="https://pwdown.info/112181/01.%20Teri%20Jhuki%20Nazar.mp3";

        try {
            music.setDataSource(url);
            music.prepare();
           music.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return music.getDuration();
    }
    public void musicpause()
    {
        music.pause();
    }

    // Stopping the music

    @Override
    public int getItemCount() {
        return taskList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public AppCompatRadioButton radioButton;
        public AppCompatTextView task;
        public Chip todaychip;
        public Chip alarm_time,alarm_stop;
        SeekBar sb;
        ontaskClickListener ontaskClickListener;
        LinearLayout ll1;
        TextView ct,dur;
        ImageView pr;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            radioButton=itemView.findViewById(R.id.todo_radio_button);
            task=itemView.findViewById(R.id.todo_row_todo);
            todaychip=itemView.findViewById(R.id.todo_row_chip);
            alarm_time=itemView.findViewById(R.id.alarm_time);
            alarm_stop=itemView.findViewById(R.id.alarm_stop);
            sb=itemView.findViewById(R.id.sb);
            ll1=itemView.findViewById(R.id.ll1);
            ct=itemView.findViewById(R.id.ct);
            dur=itemView.findViewById(R.id.dur);
            this.ontaskClickListener=clickListener;
            itemView.setOnClickListener(this);
            pr=itemView.findViewById(R.id.pr);
            radioButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Task currTask=taskList.get(getAdapterPosition());
            int id=v.getId();
            if (id==R.id.todo_row_layout){
                ontaskClickListener.ontaskClick(currTask);
            }else if (id==R.id.todo_radio_button){
                ontaskClickListener.ontaskrb(currTask);
            }
        }
    }

}
