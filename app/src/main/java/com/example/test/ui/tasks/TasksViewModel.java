package com.example.test.ui.tasks;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.adapter.RcVwAdapter;
import com.example.test.controller.TaskController;
import com.example.test.model.TaskModel;

import java.util.List;

public class TasksViewModel extends ViewModel {

    TaskController taskController = new TaskController();

    public TasksViewModel() {

    }

    public void getList(List<TaskModel> lst, RcVwAdapter adapter, String nameFragment){
        taskController.GetTask(lst, adapter, nameFragment);
    }

    public void DeleteTask(List<TaskModel> taskModels, RcVwAdapter adapter, RecyclerView recyclerView){
        ItemTouchHelper.SimpleCallback simpleCallback = taskController.ItemTouchHelperForDelete(taskModels, adapter);
        if(simpleCallback != null)
            taskController.DeleteTask(simpleCallback, recyclerView);
    }
}