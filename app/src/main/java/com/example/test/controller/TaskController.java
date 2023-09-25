package com.example.test.controller;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.adapter.RcVwAdapter;
import com.example.test.model.TaskModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

public class TaskController {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Users");

    public void CreateTask(String nameTask, Date time) {
        TaskModel taskModel = new TaskModel(nameTask, time);
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("task")
                .child(String.valueOf(taskModel.getId()))
                .setValue(taskModel);
    }

    public void EditTask(int id, String nameCheck, int checkBox) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("task")
                        .child(String.valueOf(id))
                        .child(nameCheck)
                        .setValue(checkBox);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void DeleteTask(ItemTouchHelper.SimpleCallback simpleCallback, RecyclerView recyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public ItemTouchHelper.SimpleCallback ItemTouchHelperForDelete(List<TaskModel> taskModels, RcVwAdapter adapter) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int postion = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("task")
                                .child(String.valueOf(taskModels.get(postion).getId()))
                                .getRef().removeValue();
                        if (taskModels.size() == 0)
                            taskModels.clear();
                        else
                            taskModels.remove(postion);
                        adapter.notifyItemRemoved(postion);
                        break;
                    default:
                        break;
                }
            }
        };
        return simpleCallback;
    }

    public void GetTask(List<TaskModel> lst, RcVwAdapter adapter, String nameFragment) {
        if (!lst.isEmpty())
            lst.clear();
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("task").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        switch (nameFragment){
                            case "MyDay":
                                if(snapshot.getValue(TaskModel.class).getDone() == 1)
                                    lst.add(snapshot.getValue(TaskModel.class));
                                adapter.notifyDataSetChanged();
                                break;
                            case "Important":
                                if (snapshot.getValue(TaskModel.class).getImpo() == 1) {
                                    lst.add(snapshot.getValue(TaskModel.class));
                                    adapter.notifyDataSetChanged();
                                }
                                break;
                            case "Task":
                                lst.add(snapshot.getValue(TaskModel.class));
                                adapter.notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        switch (nameFragment){
                            case "Important":
                                int i = 0;
                                if(snapshot.getValue(TaskModel.class).getImpo() == 0)
                                    for (TaskModel taskModel : lst) {
                                        if (taskModel.getId() == snapshot.getValue(TaskModel.class).getId()) {
                                            lst.remove(taskModel);
                                            adapter.notifyItemRemoved(i);
                                            break;
                                        }
                                        i++;
                                    }
                                break;
                            case "MyDay":
                                int j = 0;
                                if(snapshot.getValue(TaskModel.class).getDone() == 0)
                                    for (TaskModel taskModel : lst) {
                                        if (snapshot.getValue(TaskModel.class).getId() == taskModel.getId()) {
                                            Log.d("test", String.valueOf(taskModel.getId()));
                                            lst.remove(taskModel);
                                            adapter.notifyItemRemoved(j);
                                            break;
                                        }
                                        j++;
                                    }
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void deleteTask(int id)
    {
        DatabaseReference taskRef = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("task");
        taskRef.orderByChild("id").equalTo(String.valueOf(id)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Log.d("TEST-DELETE", "value:" + data.getRef().getKey());
                    data.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR-DELETE", "loadPost:onCancelled", error.toException());
            }
        });
    }
}