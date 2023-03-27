package com.example.ambulance_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.ambulance_demo.client.OnlineClient;
import com.example.ambulance_demo.databinding.ActivitySignupBinding;
import com.example.ambulance_demo.entity.UserTable;
import com.example.ambulance_demo.service.Services;
import com.example.ambulance_demo.viewmodel.SignupActivityViewModel;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private SignupActivityViewModel signupActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        this.signupActivityViewModel = ViewModelProviders.of(SignupActivity.this).get(SignupActivityViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void signup(View view){
        String user_name = binding.userName.getText().toString();
        String email = binding.email.getText().toString();
        String phone = binding.phone.getText().toString();
        String password = binding.password.getText().toString();
        String user_type = binding.userType.getText().toString();
        EditText[] editTexts = new EditText[]{binding.userName, binding.email, binding.phone, binding.password, binding.userType};

        if(Services.checkExits(SignupActivity.this, editTexts)){
            this.signupActivityViewModel.setData(OnlineClient.getInterface().createUser(new UserTable(user_name, email, phone, password, user_type)), this.signupActivityViewModel.user);
        }

        this.signupActivityViewModel.user.observeForever(errorTable -> {
            if(errorTable != null){
                Services.toast(SignupActivity.this, errorTable.getErr_id() +":"+ errorTable.getErr_msg());
                if(errorTable.getErr_status()) {
                    Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
    }


}