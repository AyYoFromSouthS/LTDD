package com.example.test.ui.important;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.adapter.RcVwAdapter;
import com.example.test.controller.TaskController;
import com.example.test.model.TaskModel;

import java.util.List;

public class ImportantViewModel extends ViewModel {
    TaskController taskController = new TaskController();

    public ImportantViewModel() {
    }

    public void getList(List<TaskModel> lst, RcVwAdapter adapter, String nameFragment) {
        taskController.GetTask(lst, adapter, nameFragment);
    }
}