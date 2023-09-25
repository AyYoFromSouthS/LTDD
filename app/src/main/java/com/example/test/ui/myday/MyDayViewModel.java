package com.example.test.ui.myday;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.adapter.RcVwAdapter;
import com.example.test.controller.TaskController;
import com.example.test.model.TaskModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyDayViewModel extends ViewModel {
    private final MutableLiveData<String> txtGreat;
    TaskController taskController = new TaskController();

    public MutableLiveData<String> getTxtTime() {
        return txtTime;
    }
    private final MutableLiveData<String> txtTime;

    public MyDayViewModel() {
        txtGreat = new MutableLiveData<>();
        txtGreat.setValue("Good "+ getTime());
        txtTime = new MutableLiveData<>();
        txtTime.setValue(getDate());
    }

    public void getList(List<TaskModel> lst, RcVwAdapter adapter, String nameFragment){
        taskController.GetTask(lst, adapter, nameFragment);
    }

    public String getDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleFormat = new SimpleDateFormat("MMMM dd, yyyy");
        return simpleFormat.format(new Date());
    }

    public String getTime(){
        Date date = new Date();
        int currentHour = date.getHours();
        if (currentHour >=0 && currentHour<= 12)
            return "Morning";
        else if (currentHour <= 17)
            return "Afternoon";
        else if (currentHour <= 20)
            return "Evening";
        else return "Night";
    }

    public LiveData<String> getTxtGrate() {
        return txtGreat;
    }
}