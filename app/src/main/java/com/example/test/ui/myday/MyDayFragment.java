package com.example.test.ui.myday;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.adapter.RcVwAdapter;
import com.example.test.databinding.FragmentMydayBinding;
import com.example.test.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class MyDayFragment extends Fragment{

    List<TaskModel> lst;
    private RcVwAdapter adapter;
    MyDayViewModel myDayViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        myDayViewModel =
                new ViewModelProvider(this).get(MyDayViewModel.class);
        com.example.test.databinding.FragmentMydayBinding binding = FragmentMydayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGreat;
        myDayViewModel.getTxtGrate().observe(getViewLifecycleOwner(),textView::setText);

        final TextView textView1 = binding.txtTime;
        myDayViewModel.getTxtTime().observe(getViewLifecycleOwner(),textView1::setText);

        lst = new ArrayList<>();

        RecyclerView recyView = binding.recycleViewMyDay;
        recyView.setHasFixedSize(true);
        recyView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RcVwAdapter(getContext(), lst);
        recyView.setAdapter(adapter);
        myDayViewModel.getList(lst, adapter, "MyDay");
        return root;
    }
}
