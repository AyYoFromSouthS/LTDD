
package com.example.test.controller;



import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static com.example.test.MainActivity.MY_REQUEST_CODE;

public class UserController {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Intent ChangePassword(String newPass, Context context){
        Intent intent = new Intent(context, SignIn.class);

        user.updatePassword(newPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                });
        return intent;
    }

    public void SetUserInformation(EditText edtFullName, TextView txtEmail, ImageView imageView, FragmentActivity fragment, MainActivity mainActivity){
        if(user != null){
            edtFullName.setText(user.getDisplayName());
            txtEmail.setText(user.getEmail());
            Glide.with(fragment).load(user.getPhotoUrl()).error(R.drawable.ic_avata).into(imageView);
//            mainActivity.showInformation();
        }
    }

    public void SetBitmapImageView(Bitmap bitmapImageView, ImageView imageAvatar){
        imageAvatar.setImageBitmap(bitmapImageView);
    }

    public void onClickRequestPermission(MainActivity mainActivity, FragmentActivity fragment){
        if(mainActivity == null)
            return;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            mainActivity.openGallery();
            return;
        }
        if(fragment.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            mainActivity.openGallery();
        }else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            fragment.requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }

    public void initListener(ImageView imageView, Button btn, EditText name, Uri uri, MainActivity mainActivity, FragmentActivity fragment){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission(mainActivity, fragment);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile(name, uri, mainActivity);
            }
        });
    }

    public void onClickUpdateProfile(EditText fullName, Uri uri, MainActivity mainActivity){
        if(user == null)
            return;
        String Name = fullName.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Name)
                .setPhotoUri(uri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful())
//                            mainActivity.showInformation();
                    }
                });
    }

}
