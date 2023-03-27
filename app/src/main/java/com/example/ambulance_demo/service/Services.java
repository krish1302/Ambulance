package com.example.ambulance_demo.service;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ambulance_demo.LoginActivity;
import com.example.ambulance_demo.entity.ErrorTable;
import com.example.ambulance_demo.entity.UserTable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Services {

    public static Boolean checkExits(Context context, EditText[] editTexts){
        for(EditText editText: editTexts){
            String text = editText.getText().toString();
            String hint = editText.getHint().toString();
            if(text.equals("")){
                toast(context, "Please enter "+hint);
                return false;
            }
        }
        return true;
    }

    public static void navigate(Context context, Object object, String key){
        Intent i = new Intent(context, (Class<?>) object);
        if(key == "main"){
            i.putExtra("email", "email");
            i.putExtra("name", "name");
            i.putExtra("id", "id");
        }
        context.startActivity(i);
    }

    public static void toast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void call(Call<ErrorTable> call, Context context){
        call.enqueue(new Callback<ErrorTable>() {
            @Override
            public void onResponse(Call<ErrorTable> call, Response<ErrorTable> response) {
                if(response.body().getErr_id() != 233) {
                    toast(context, response.body().getErr_id() + ":" + response.body().getErr_msg());
                }
            }
            @Override
            public void onFailure(Call<ErrorTable> call, Throwable t) {
//                toast(context, t.getLocalizedMessage());
            }
        });
    }

    public static void call(Call<ErrorTable> call, Context context, Object object, String key){
        call.enqueue(new Callback<ErrorTable>() {
            @Override
            public void onResponse(Call<ErrorTable> call, Response<ErrorTable> response) {
                toast(context, response.body().getErr_id() +":"+ response.body().getErr_msg());
                if(response.body().getErr_status()) {
                    navigate(context, object, key);
                }
            }
            @Override
            public void onFailure(Call<ErrorTable> call, Throwable t) {
//                Services.toast(context, t.getLocalizedMessage());
            }
        });
    }

    public static void calls(Call<UserTable> call, Context context, Object object, String key){
        call.enqueue(new Callback<UserTable>() {
            @Override
            public void onResponse(Call<UserTable> call, Response<UserTable> response) {
//                toast(context, response.body().getUser_id() +"");
                if(response.body() != null){
                    Intent i = new Intent(context, (Class<?>) object);
                    i.putExtra("email", response.body().getUser_email());
                    i.putExtra("name", response.body().getUser_name());
                    i.putExtra("id", response.body().getUser_id());
                    i.putExtra("type", response.body().getUser_type());
                    context.startActivity(i);
                }
            }
            @Override
            public void onFailure(Call<UserTable> call, Throwable t) {
                Services.toast(context, t.getLocalizedMessage());
            }
        });
    }
}
