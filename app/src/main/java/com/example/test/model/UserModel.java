package com.example.test.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserModel {
    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
    private List<TaskModel> lst;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public UserModel(String email, String userName) {
        setEmail(email);
        setUserName(userName);
        lst = new ArrayList<>();
    }

    public UserModel(){}
}
