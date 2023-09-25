package com.example.test.ui.important;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.adapter.RcVwAdapter;
import com.example.test.databinding.FragmentImportantBinding;
import com.example.test.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class ImportantFragment extends Fragment {
    private FragmentImportantBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ImportantViewModel homeViewImportant = new ViewModelProvider(this).get(ImportantViewModel.class);
        binding = FragmentImportantBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<TaskModel> listTaskImportant = new ArrayList<>();

        RecyclerView reCyView = binding.recycleViewImportant;
        reCyView.setHasFixedSize(true);
        reCyView.setLayoutManager(new LinearLayoutManager(getContext()));
        RcVwAdapter adapter = new RcVwAdapter(getContext(), listTaskImportant);
        reCyView.setAdapter(adapter);
        homeViewImportant.getList(listTaskImportant, adapter, "Important");

        return root;
    }
}
