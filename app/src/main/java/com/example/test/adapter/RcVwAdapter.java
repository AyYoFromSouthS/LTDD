package com.example.test.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.TaskDetail;
import com.example.test.controller.TaskController;
import com.example.test.model.TaskModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;


public class RcVwAdapter extends RecyclerView.Adapter<RcVwAdapter.TaskViewHolder> {
    SimpleDateFormat format = new SimpleDateFormat("h:mm aa");
    private List<TaskModel> taskList;
    Context context;
    TaskController taskController = new TaskController();

    public RcVwAdapter(Context context, List<TaskModel> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_layout, parent, false);
        return new TaskViewHolder(v);
    }

    public boolean toBoolean(int num) {
        return num != 0;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final TaskModel item = taskList.get(position);
        holder.txtActivity.setText(item.getTask());
        holder.txtTime.setText(format.format(item.getTime()));
        holder.chbxDone.setChecked(toBoolean(item.getDone()));
        holder.chbxImp.setChecked(toBoolean(item.getImpo()));
        //Done
        if(holder.chbxDone.isChecked())
            holder.txtActivity.setPaintFlags(holder.txtActivity.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.chbxDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.chbxDone.isChecked()) {
                    holder.txtActivity.setPaintFlags(holder.txtActivity.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    item.setDone(1);
                } else {
                    holder.txtActivity.setPaintFlags(holder.txtActivity.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    item.setDone(0);
                }
                if(item.getTask() != null)
                    taskController.EditTask(item.getId(), "done", item.getDone());
            }
        });
        //Important
        holder.chbxImp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.chbxImp.isChecked())
                    item.setImpo(1);
                else {
                    item.setImpo(0);
                }
                if(item.getTask() != null)
                    taskController.EditTask(item.getId(), "impo", item.getImpo());
            }
        });

        holder.setItemClickListener(new TaskViewHolder.ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, TaskDetail.class);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("Users");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("task")
                        .orderByChild("id")
                        .equalTo(taskList.get(position).getId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds: snapshot.getChildren()){
                                    TaskModel task = ds.getValue(TaskModel.class);
                                    Log.d("TEST", "value: "+ task.getTask());
                                    intent.putExtra("task", task.getTask());
                                    intent.putExtra("id", task.getId());
                                    intent.putExtra("impo", task.getImpo());
                                    intent.putExtra("done", task.getDone());
                                    intent.putExtra("time", task.getTime());
                                }
                                context.startActivity(intent);
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                throw error.toException();
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        if (taskList == null) return 0;
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtActivity, txtTime;
        CheckBox chbxDone, chbxImp;
        LinearLayout foreground;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        private ItemClickListener itemClickListener;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            txtActivity = itemView.findViewById(R.id.txtActivity);
            chbxImp = itemView.findViewById(R.id.chbxImp);
            chbxDone = itemView.findViewById(R.id.chbxDone);
            txtTime = itemView.findViewById(R.id.txtRemindTime);
            foreground = itemView.findViewById(R.id.layout_foreground);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        public interface ItemClickListener {
            void onClick(View view, int position, boolean isLongClick);
        }

    }

}
