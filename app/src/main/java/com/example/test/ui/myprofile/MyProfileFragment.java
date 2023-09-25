package com.example.test.ui.myprofile;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.MainActivity;
import com.example.test.controller.UserController;
import com.example.test.databinding.FragmentMyprofileBinding;

public class MyProfileFragment extends Fragment {

    private MainActivity mainActivity;
    private UserController userController = new UserController();

    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MyProfileViewModel mViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        com.example.test.databinding.FragmentMyprofileBinding binding = FragmentMyprofileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mViewModel.setImageAvatar(binding.imgAvatar);
        mViewModel.setEdtFullName(binding.edtFullName);
        mViewModel.setTxtEmail(binding.edtEmail);
        mViewModel.setBtnMyProfile(binding.btnUpdateProfile);

        mainActivity = (MainActivity) getActivity();

        userController.SetUserInformation(mViewModel.getEdtFullName()
                , mViewModel.getTxtEmail()
                , mViewModel.getImageAvatar()
        , this.getActivity(), mainActivity);
        userController.initListener(mViewModel.getImageAvatar()
                , mViewModel.getBtnMyProfile()
                , mViewModel.getEdtFullName()
                , mViewModel.getUri()
                , mainActivity
                , this.getActivity());

        return root;
    }

}