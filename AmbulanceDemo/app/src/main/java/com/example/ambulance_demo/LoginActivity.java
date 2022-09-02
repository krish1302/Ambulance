package com.example.ambulance_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.ambulance_demo.api.OnlineApiInterface;
import com.example.ambulance_demo.client.OnlineClient;
import com.example.ambulance_demo.databinding.ActivityLoginBinding;
import com.example.ambulance_demo.entity.ErrorTable;
import com.example.ambulance_demo.entity.UserTable;
import com.example.ambulance_demo.service.Services;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
    public ActivityLoginBinding binding;
    public String email, password;
    public EditText[] editTexts;
    private OnlineApiInterface onlineApiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        onlineApiInterface = OnlineClient.getInterface();
    }



    public void login(View v){
        email = binding.loginEmail.getText().toString();
        password = binding.loginPassword.getText().toString();
        editTexts = new EditText[]{binding.loginEmail,binding.loginPassword};
        if(Services.checkExits(LoginActivity.this, editTexts)){
            Call<UserTable> call = onlineApiInterface.checkUser(email, password);
            Services.calls(call,LoginActivity.this, MainActivity.class, "main");
        }
    }

    public void goToSignup(View view){
        Services.navigate(LoginActivity.this, SignupActivity.class, "signup");
    }
}