package com.example.test.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.adapter.RcVwAdapter;
import com.example.test.databinding.FragmentTasksBinding;
import com.example.test.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment{
    private List<TaskModel> lst;

    private RcVwAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TasksViewModel mTaskModel = new ViewModelProvider(this).get(TasksViewModel.class);
        com.example.test.databinding.FragmentTasksBinding binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lst = new ArrayList<>();

        RecyclerView recyView = binding.recycleViewTask;
        recyView.setHasFixedSize(true);
        recyView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RcVwAdapter(getContext(), lst);
        recyView.setAdapter(adapter);
        mTaskModel.getList(lst, adapter, "Task");

        mTaskModel.DeleteTask(lst, adapter, recyView);

        return root;
    }
}