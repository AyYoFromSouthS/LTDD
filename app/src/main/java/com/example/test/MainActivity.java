package com.example.test;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.test.databinding.ActivityMainBinding;
import com.example.test.model.TaskModel;
import com.example.test.ui.myprofile.MyProfileViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import com.example.test.controller.TaskController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    EditText edtTask;
    Button btnSave, btnSetDate, btnSetTime;

    TextView txtUserName;
    TextView txtEmail;
    ImageView headerImage;

    BottomSheetDialog dialog;
    Calendar calendar = Calendar.getInstance();
    Calendar selected = Calendar.getInstance();
    SimpleDateFormat simpleformat = new SimpleDateFormat("MMMM dd, yyyy");
    int hours = calendar.get(Calendar.HOUR_OF_DAY);
    int minutes = calendar.get(Calendar.MINUTE);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

    SimpleDateFormat format = new SimpleDateFormat("h:mm aa");
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Action bar
        setSupportActionBar(binding.appBarMain.toolbar);

        dialog = new BottomSheetDialog(this);


        //Floating button
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_myDay, R.id.nav_important,
                R.id.nav_tasks, R.id.nav_changePass,
                R.id.nav_myProfile
        )
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        MenuItem item = navigationView.getMenu().findItem(R.id.nav_signOut);

        txtUserName = navigationView.getHeaderView(0).findViewById(R.id.headerUserName);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.headerEmail);
        headerImage = navigationView.getHeaderView(0).findViewById(R.id.headerImage);
        showInformation();
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
                finish();
                return true;
            }
        });
    }

    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.add_new_task, null, false);
        dialog.setContentView(view);
        btnSave = dialog.findViewById(R.id.btnSave);
        edtTask = dialog.findViewById(R.id.edtTask);
        btnSetDate = dialog.findViewById(R.id.btnSetDate);
        btnSetDate.setText(getCurrentDate());
        btnSetTime = dialog.findViewById(R.id.btnSetTime);
        btnSetTime.setText(getCurrentTime());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (edtTask.getText().toString().equals(""))
                    Toast.makeText(MainActivity.this, "Vui lòng nhập task", Toast.LENGTH_SHORT).show();
                else {
                    TaskController taskController = new TaskController();
                    taskController.CreateTask(edtTask.getText().toString(), selected.getTime());
                    Toast.makeText(MainActivity.this, edtTask.getText().toString(), Toast.LENGTH_SHORT).show();
                    edtTask.setText(null);
                }
            }
        });
        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selected.set(Calendar.YEAR, year);
                        selected.set(Calendar.MONTH, month);
                        selected.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String time = simpleformat.format(selected.getTime());
                        btnSetDate.setText(time);
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selected.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selected.set(Calendar.MINUTE, minute);
                        selected.setTimeZone(TimeZone.getDefault());
                        String time = format.format(selected.getTime());
                        btnSetTime.setText(time);
                    }
                }, hours, minutes, false);
                timePickerDialog.show();

            }
        });
        dialog.show();
    }

    private void createAppSetting() {
        View view = getLayoutInflater().inflate(R.layout.app_new_setting, null, false);
        dialog.setContentView(view);
        LinearLayout appSettingSort = dialog.findViewById(R.id.appSettingSort);
        LinearLayout appSettingChange = dialog.findViewById(R.id.appSettingChangeTheme);
        Button btnDone = dialog.findViewById(R.id.btnDone);
        appSettingSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSort();
            }
        });

        appSettingChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChangeTheme();
            }
        });
        dialog.show();
    }

    private void createSort() {
        View view = getLayoutInflater().inflate(R.layout.app_in_sort, null, false);
        dialog.setContentView(view);
        Button btnSortBackSetting = dialog.findViewById(R.id.btnSortBackSetting);
        LinearLayout appSettingSort = dialog.findViewById(R.id.appSettingSort);
        btnSortBackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAppSetting();
            }
        });

        dialog.show();
    }

    private void createChangeTheme() {
        View view = getLayoutInflater().inflate(R.layout.app_in_changetheme, null, false);
        dialog.setContentView(view);

        Button btnChangeBackSetting = dialog.findViewById(R.id.btnChangeBackSetting);
        Button btnChangeColor = dialog.findViewById(R.id.btnChangColor);
        ImageButton imgBtnWhite = dialog.findViewById(R.id.imgBtnWhite);
        ImageButton imgBtnRed = dialog.findViewById(R.id.imgBtnRed);
        ImageButton imgBtnPurple = dialog.findViewById(R.id.imgBtnPurple);
        ImageButton imgBtnBlue = dialog.findViewById(R.id.imgBtnBlue);
        TextView txtGreat = findViewById(R.id.textGreat);
        TextView txtTime = findViewById(R.id.txtTime);
        TextView txtListTaskMyDay = findViewById(R.id.ListMyDayTxt);
        TextView txtListTak = findViewById(R.id.ListTaskTxt);

        imgBtnWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChangeColor.setTextColor(getColor(R.color.information));
                txtGreat.setTextColor(getColor(R.color.information));
                txtTime.setTextColor(getColor(R.color.information));
                txtListTaskMyDay.setTextColor(getColor(R.color.information));
                txtListTak.setTextColor(getColor(R.color.information));
                btnChangeColor.setBackgroundTintList(getColorStateList(R.color.white));
            }
        });
        imgBtnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // màu chữ
                btnChangeColor.setTextColor(getColor(R.color.white));
                txtGreat.setTextColor(getColor(R.color.error_500));
                txtTime.setTextColor(getColor(R.color.error_500));
                txtListTaskMyDay.setTextColor(getColor(R.color.error_500));
                txtListTak.setTextColor(getColor(R.color.error_500));
                // màu nền
                btnChangeColor.setBackgroundTintList(getColorStateList(R.color.Red));
            }
        });

        imgBtnPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChangeColor.setTextColor(getColor(R.color.white));
                txtGreat.setTextColor(getColor(R.color.DarkViolet));
                txtTime.setTextColor(getColor(R.color.DarkViolet));
                txtListTaskMyDay.setTextColor(getColor(R.color.DarkViolet));
                txtListTak.setTextColor(getColor(R.color.DarkViolet));
                // màu nền
                btnChangeColor.setBackgroundTintList(getColorStateList(R.color.DarkViolet));
            }
        });
        imgBtnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChangeColor.setTextColor(getColor(R.color.white));
                txtGreat.setTextColor(getColor(R.color.Blue));
                txtTime.setTextColor(getColor(R.color.Blue));
                txtListTaskMyDay.setTextColor(getColor(R.color.Blue));
                txtListTak.setTextColor(getColor(R.color.Blue));
                // màu nền
                btnChangeColor.setBackgroundTintList(getColorStateList(R.color.Blue));
            }
        });
        btnChangeBackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAppSetting();
            }
        });
        btnChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        dialog.show();
    }

    public String getCurrentDate() {

        return simpleformat.format(new Date());
    }

    public String getCurrentTime() {
        return format.format(calendar.getTime());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            createAppSetting();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    final MyProfileViewModel myProfile = new MyProfileViewModel();

    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent == null)
                    return;
                Uri uri = intent.getData();
                myProfile.setUri(uri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    myProfile.getImageAvatar().setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

    public static final int MY_REQUEST_CODE = 10;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openGallery();
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    public void showInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            return;
        Uri photoUrl = user.getPhotoUrl();
        if (user.getDisplayName() != null)
            txtUserName.setText(user.getDisplayName());
        txtEmail.setText(user.getEmail());
        if(photoUrl != null)
            Glide.with(txtEmail).load(photoUrl).error(R.drawable.ic_avata).into(headerImage);
    }
}