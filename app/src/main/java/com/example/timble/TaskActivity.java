package com.example.timble;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

public class TaskActivity extends AppCompatActivity implements ontaskClickListener{

    private  TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private taskAdapter taskAdapter;
    private int counter;
    BottomSheetFragment bottomSheetFragment;
    private Sharedviewmodel sharedviewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        counter=0;
        bottomSheetFragment=new BottomSheetFragment();
        ConstraintLayout constraintLayout=findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout>bottomSheetBehavior=BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);


        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel=new ViewModelProvider.AndroidViewModelFactory(TaskActivity.this.getApplication())
                .create(TaskViewModel.class);
        sharedviewmodel=new ViewModelProvider(this)
                .get(Sharedviewmodel.class);

        taskViewModel.getAllTasks().observe(this, (List<Task> tasks) -> {
            taskAdapter =new taskAdapter(tasks,this);
            recyclerView.setAdapter(taskAdapter);
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
           showBottomSheetDialog();
        });
    }


    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(),bottomSheetFragment.getTag());
    }

    @Override
    public void ontaskClick(Task task) {
       sharedviewmodel.selectItem(task);
       sharedviewmodel.setIsEdit(true);
       showBottomSheetDialog();
    }


    @Override
    public void ontaskrb(Task task) {
        TaskViewModel.delete(task);
        taskAdapter.notifyDataSetChanged();
    }

}