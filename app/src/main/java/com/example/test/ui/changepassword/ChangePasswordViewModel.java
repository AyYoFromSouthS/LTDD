package com.example.test.ui.changepassword;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.ViewModel;

import com.example.test.controller.TaskController;
import com.example.test.controller.UserController;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.Buffer;

public class ChangePasswordViewModel extends ViewModel {
    public EditText getNewPass() {
        return newPass;
    }

    public void setNewPass(EditText newPass) {
        this.newPass = newPass;
    }

    public EditText getConfirmNewPasss() {
        return confirmNewPasss;
    }

    public void setConfirmNewPasss(EditText confirmNewPasss) {
        this.confirmNewPasss = confirmNewPasss;
    }

    private EditText newPass;
    private EditText confirmNewPasss;

    UserController userController = new UserController();

    public Button getBtnChangePass() {
        return btnChangePass;
    }

    public Intent onClickChangePassword(Context context){
        String strNewPass = getNewPass().getText().toString().trim();
        String strConfirmNewPass = getConfirmNewPasss().getText().toString().trim();
        if(!strNewPass.isEmpty() && !strConfirmNewPass.isEmpty()){
            if(strNewPass.length() >= 6 && strNewPass.equals(strConfirmNewPass) == true){
              Intent intent = userController.ChangePassword(strNewPass, context);
              return intent;
            }
        }
        return null;
    }

    public void setBtnChangePass(Button btnChangePass) {
        this.btnChangePass = btnChangePass;
    }

    private Button btnChangePass;

    public void ChangePasswordFragment(){

    }
}