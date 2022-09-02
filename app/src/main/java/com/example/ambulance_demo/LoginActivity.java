package com.example.ambulance_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.ambulance_demo.api.OnlineApiInterface;
import com.example.ambulance_demo.client.OnlineClient;
import com.example.ambulance_demo.databinding.ActivityLoginBinding;
import com.example.ambulance_demo.entity.ErrorTable;
import com.example.ambulance_demo.entity.UserTable;
import com.example.ambulance_demo.service.Services;
import com.example.ambulance_demo.viewmodel.LoginActivityViewModel;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
    public ActivityLoginBinding binding;
    public String email, password;
    public EditText[] editTexts;
    private LoginActivityViewModel loginActivityViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        this.loginActivityViewModel = ViewModelProviders.of(LoginActivity.this).get(LoginActivityViewModel.class);

        this.loginActivityViewModel.user.observeForever(userTable -> {
            if(userTable != null){
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("email", userTable.getUser_email());
                i.putExtra("name", userTable.getUser_name());
                i.putExtra("id", userTable.getUser_id());
                i.putExtra("type", userTable.getUser_type());
                startActivity(i);
            }
        });
    }

    public void login(View v){
        email = binding.loginEmail.getText().toString();
        password = binding.loginPassword.getText().toString();
        editTexts = new EditText[]{binding.loginEmail,binding.loginPassword};
        if(Services.checkExits(LoginActivity.this, editTexts)){
            this.loginActivityViewModel.setData(OnlineClient.getInterface().checkUser(email, password), this.loginActivityViewModel.user);
        }
    }

    public void goToSignup(View view){
        Services.navigate(LoginActivity.this, SignupActivity.class, "signup");
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
}