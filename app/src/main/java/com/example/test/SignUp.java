package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    TextInputEditText txtSUEmail;
    TextInputEditText txtSUPass;

    TextInputEditText txtSUUserName;
    TextInputEditText txtSUConfirmPass;
    Button btnSignUp;
    Button btnSignIn;

//    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        txtSUUserName = findViewById(R.id.txtSUUserName);
        txtSUPass = findViewById(R.id.txtSUPass);
        txtSUEmail = findViewById(R.id.txtSUEmail);
        txtSUConfirmPass = findViewById(R.id.txtSUConfirmPass);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignUp = findViewById(R.id.btnSignUp);

        onStart();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignUp();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean KiemTra(String strSignUp){
        if(strSignUp.isEmpty())
            return false;
        return true;
    }


    public void onClickSignUp() {
        String strEmail = txtSUEmail.getText().toString().trim();
        String strPass = txtSUPass.getText().toString().trim();
        String strConFirm = txtSUConfirmPass.getText().toString().trim();
        String strUserName = txtSUUserName.getText().toString().trim();

        if(!KiemTra(strEmail) && !KiemTra(strPass) && !KiemTra(strConFirm) && !KiemTra(strUserName)){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                txtSUEmail.setError("Enter a valid email");
                return;
            }
            else if(!strPass.equals(strConFirm)) {
                txtSUConfirmPass.setError("Password don't match");
                return;
            }
            txtSUEmail.requestFocus();
            txtSUPass.requestFocus();
            txtSUConfirmPass.requestFocus();
            txtSUUserName.requestFocus();
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(strEmail, strPass)
                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserModel user = new UserModel(strEmail, strUserName);

                            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(strUserName)
                                            .build();
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            firebaseUser.updateProfile(userProfileChangeRequest);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
//                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                                startActivity(intent);
                                                finishAffinity();
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Toast.makeText(SignUp.this, "Authentication failedkokasdkpdaskd ",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
//                             Sign in success, update UI with the signed-in user's information
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUp.this, "Authentication failed ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
