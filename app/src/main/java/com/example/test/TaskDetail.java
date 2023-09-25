package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.controller.TaskController;
import com.example.test.model.TaskModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaskDetail extends AppCompatActivity {
    TaskController taskController;
    Button btnSave, btnAddToMyDay, btnRemind, btnAddDue, btnRepeat;
    EditText edtTask;
    CheckBox chboxDone, chboxImpo;
    boolean addMDclicked = false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        String task = intent.getStringExtra("task");
        int id = intent.getIntExtra("id",0);
        int done = intent.getIntExtra("done",0);
        int impo = intent.getIntExtra( "impo",0);
        Date time = (Date) intent.getSerializableExtra("time");

        chboxDone = findViewById(R.id.chbxDone);
        chboxImpo = findViewById(R.id.chbxImp);
        btnSave = findViewById(R.id.btnSaveEdit);
        edtTask = findViewById(R.id.txtTask);
        btnAddToMyDay = findViewById(R.id.btnAddToMyDay);
        btnRemind = findViewById(R.id.btnRemind);
        btnAddDue = findViewById(R.id.btnAddDue);
        btnRepeat = findViewById(R.id.btnRepeat);

        Date date = new Date();
        edtTask.setText(task);

        if (done==0){
            chboxDone.setChecked(false);
        }
        else chboxDone.setChecked(true);

        if (impo==0){
            chboxImpo.setChecked(false);
        }
        else chboxImpo.setChecked(true);
        Intent i = new Intent(this, MainActivity.class);

        btnAddToMyDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addMDclicked == false) {
                    addMDclicked = true;
                    btnAddToMyDay.setText("Added to my day");
                    btnAddToMyDay.setTextColor(Color.parseColor("#8A2BE2"));
                }
                else{
                    addMDclicked = false;
                    btnAddToMyDay.setText("Add to my day");
                    btnAddToMyDay.setTextColor(Color.BLACK);
                }
            }
        });
        btnRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(TaskDetail.this, btnRemind);
                popupMenu.getMenuInflater().inflate(R.menu.remind_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        btnRemind.setText(item.getTitle());
                        btnRemind.setTextColor(Color.parseColor("#8A2BE2"));
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        btnAddDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(TaskDetail.this, btnAddDue);
                popupMenu.getMenuInflater().inflate(R.menu.adddue_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        btnAddDue.setText(item.getTitle());
                        btnAddDue.setTextColor(Color.parseColor("#8A2BE2"));
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(TaskDetail.this, btnRepeat);
                popupMenu.getMenuInflater().inflate(R.menu.repeat_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        btnRepeat.setText(item.getTitle());
                        btnRepeat.setTextColor(Color.parseColor("#8A2BE2"));
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskModel mtask = new TaskModel();
                mtask.setId(id);
                mtask.setTask(edtTask.getText().toString());
                chboxDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (chboxDone.isChecked())
                            mtask.setDone(1);
                        else
                            mtask.setDone(0);
                    }
                });
                chboxImpo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (chboxImpo.isChecked())
                            mtask.setImpo(1);
                        else
                            mtask.setImpo(0);

                    }
                });
                if (addMDclicked)
                {
                    date.setHours(time.getHours());
                    date.setMinutes(time.getMinutes());
                    date.setSeconds(time.getSeconds());
                    mtask.setTime(date);
                }
                else mtask.setTime(time);
                Log.d("TEST", "value: "+mtask.getTask());
                Log.d("TEST", "value: "+mtask.getTime());

                DatabaseReference taskRef = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("task");
                Map<String, Object> map = mtask.toMap();
                taskRef.child(String.valueOf(mtask.getId())).updateChildren(map);

                startActivity(i);
            }

        });
    }

}