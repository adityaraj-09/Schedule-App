package com.example.timble;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.timble.Util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener{
    private EditText enterTodo;
    private ImageButton calendrbtn,prbtn;
    private RadioGroup prRg;
    private RadioButton selrb;
    private int selectedButtonId;
    private ImageButton savebtn,alarm;
    private CalendarView calendarView;
    private Group calendrgrp;
    int mHour, mMinute;
    TimePickerDialog timePickerDialog;
    private Date duedate;
    private String tt;
    Calendar calendar=Calendar.getInstance();
    private Sharedviewmodel sharedviewmodel;
    private  boolean isEdit;

    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bottom_sheet, container, false);
        calendrgrp=view.findViewById(R.id.calendar_group);
        calendarView=view.findViewById(R.id.calendar_view);
        calendrbtn=view.findViewById(R.id.today_calendar_button);
        enterTodo=view.findViewById(R.id.enter_todo_et);
        alarm=view.findViewById(R.id.alarm);
        savebtn=view.findViewById(R.id.save_todo_button);
        prbtn=view.findViewById(R.id.priority_todo_button);
        prRg=view.findViewById(R.id.radioGroup_priority);
        Chip tc=view.findViewById(R.id.today_chip);
        tc.setOnClickListener(this);
        Chip tmc=view.findViewById(R.id.tomorrow_chip);
        tmc.setOnClickListener(this);
        Chip nwc=view.findViewById(R.id.next_week_chip);
        nwc.setOnClickListener(this);
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        if(sharedviewmodel.getSelectedItem().getValue()!=null){
            isEdit=sharedviewmodel.getIsEdit();
            Task task=sharedviewmodel.getSelectedItem().getValue();
            enterTodo.setText(task.getTask());
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedviewmodel=new ViewModelProvider(requireActivity()).get(Sharedviewmodel.class);

        calendrbtn.setOnClickListener(v -> {
            calendrgrp.setVisibility(calendrgrp.getVisibility()==View.GONE?View.VISIBLE:View.GONE
            );
            Utils.hidesoftkey(v);
        });
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year,month,dayOfMonth);
            duedate=calendar.getTime();
        });
        alarm.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(getActivity(),
                    (view12, hourOfDay, minute) -> {
                        tt=hourOfDay + ":" + minute;
                        timePickerDialog.dismiss();
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        });

        savebtn.setOnClickListener(v -> {
            String task=enterTodo.getText().toString();
            if(!TextUtils.isEmpty(task) && duedate!=null){
                Task myTask=new Task(task,Priority.HIGH,
                        duedate, Calendar.getInstance().getTime(),
                        false,tt);
                if (isEdit){
                    Task updateTask=sharedviewmodel.getSelectedItem().getValue();
                    updateTask.setTask(task);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setPriority(Priority.HIGH);
                    updateTask.setDuedate(duedate);
                    updateTask.setTt(tt);
                    TaskViewModel.update(updateTask);
                    sharedviewmodel.setIsEdit(false);

                }else{
                    TaskViewModel.insert(myTask);
                }

                enterTodo.setText("");
                if(this.isVisible()){
                    this.dismiss();
                }

            }else {
                enterTodo.setError("Empty Task");
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.today_chip){
            calendar.add(Calendar.DAY_OF_YEAR,0);
            duedate=calendar.getTime();
        }else if(id==R.id.tomorrow_chip){
            calendar.add(Calendar.DAY_OF_YEAR,1);
            duedate=calendar.getTime();
        } else if (id==R.id.next_week_chip) {
            calendar.add(Calendar.DAY_OF_YEAR,7);
            duedate=calendar.getTime();
        }
    }
}