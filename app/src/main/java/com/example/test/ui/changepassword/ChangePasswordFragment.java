package com.example.test.ui.changepassword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.test.databinding.FragmentChangepasswordBinding;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangepasswordBinding binding;

    private ChangePasswordViewModel mViewModel;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        binding = FragmentChangepasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mViewModel.setNewPass(binding.edtChangeNewpassword);
        mViewModel.setConfirmNewPasss(binding.edtConfirmChangePassword);
        mViewModel.setBtnChangePass(binding.btnChangepass);

        mViewModel.getBtnChangePass().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = mViewModel.onClickChangePassword(getContext());
                if(intent == null)
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                else{
                    startActivity(intent);
                    Activity activity= (Activity) getContext();
                    activity.finish();
                }
            }
        });
        return root;
    }

}