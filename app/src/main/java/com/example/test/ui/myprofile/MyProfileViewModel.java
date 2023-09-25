package com.example.test.ui.myprofile;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

public class MyProfileViewModel extends ViewModel {
    private Uri uri;
    private ImageView imageAvatar;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public ImageView getImageAvatar() {
        return imageAvatar;
    }

    public void setImageAvatar(ImageView imageAvatar) {
        this.imageAvatar = imageAvatar;
    }

    public EditText getEdtFullName() {
        return edtFullName;
    }

    public void setEdtFullName(EditText edtFullName) {
        this.edtFullName = edtFullName;
    }

    public TextView getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(TextView txtEmail) {
        this.txtEmail = txtEmail;
    }

    public Button getBtnMyProfile() {
        return btnMyProfile;
    }

    public void setBtnMyProfile(Button btnMyProfile) {
        this.btnMyProfile = btnMyProfile;
    }

    private EditText edtFullName;
    private TextView txtEmail;
    private Button btnMyProfile;

    public void MyProfileViewModel(){}
}