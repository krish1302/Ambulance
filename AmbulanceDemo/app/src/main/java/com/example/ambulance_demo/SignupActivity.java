package com.example.ambulance_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Service;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.ambulance_demo.api.OnlineApiInterface;
import com.example.ambulance_demo.client.OnlineClient;
import com.example.ambulance_demo.databinding.ActivitySignupBinding;
import com.example.ambulance_demo.entity.ErrorTable;
import com.example.ambulance_demo.entity.UserTable;
import com.example.ambulance_demo.service.Services;

import retrofit2.Call;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    String user_name, email, phone, password;
    OnlineApiInterface onlineApiInterface;
    EditText[] editTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        onlineApiInterface = OnlineClient.getInterface();
    }

    public void signup(View view){
        user_name = binding.userName.getText().toString();
        email = binding.email.getText().toString();
        phone = binding.phone.getText().toString();
        password = binding.password.getText().toString();
        editTexts = new EditText[]{binding.userName, binding.email, binding.phone, binding.password};

        if(Services.checkExits(SignupActivity.this, editTexts)){
            Call<ErrorTable> user = onlineApiInterface.createUser(new UserTable(user_name, email, phone, password));
            Services.call(user, SignupActivity.this, LoginActivity.class, "login");
        }
    }
}