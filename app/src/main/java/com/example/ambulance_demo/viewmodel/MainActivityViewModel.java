package com.example.ambulance_demo.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ambulance_demo.MainActivity;
import com.example.ambulance_demo.adapter.MyAlertAdapter;
import com.example.ambulance_demo.client.OnlineClient;
import com.example.ambulance_demo.entity.AlertTable;
import com.example.ambulance_demo.entity.ErrorTable;
import com.example.ambulance_demo.entity.UserTable;
import com.example.ambulance_demo.service.Services;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {

    public MutableLiveData<UserTable[]> vehicles_data;
    public MutableLiveData<AlertTable[]> alerts_data;
    public MutableLiveData<AlertTable> alert_from_user, alert_accept_from_driver;
    public MutableLiveData<UserTable> alert_picker_details_for_user, alert_user_details_for_picker;
    public MutableLiveData<ErrorTable> alert_error_cancel_from_user, alert_error_reject_from_driver;
    public MutableLiveData<Integer> call, quit, reject, recycleView;
    public MutableLiveData<String> name, phone, address;
    public MutableLiveData<AlertTable> alert_user_rejection, alert_picker_rejection;


    public MainActivityViewModel(){
        vehicles_data = new MutableLiveData<>();
        alerts_data = new MutableLiveData<>();
        alert_from_user = new MutableLiveData<>();
        alert_picker_details_for_user = new MutableLiveData<>();
        alert_error_cancel_from_user = new MutableLiveData<>();
        name = new MutableLiveData<>();
        phone = new MutableLiveData<>();
        call = new MutableLiveData<>();
        quit = new MutableLiveData<>();
        reject = new MutableLiveData<>();
        address = new MutableLiveData<>();
        recycleView = new MutableLiveData<>();
        alert_accept_from_driver = new MutableLiveData<>();
        alert_user_details_for_picker = new MutableLiveData<>();
        alert_error_reject_from_driver = new MutableLiveData<>();
        alert_user_rejection = new MutableLiveData<>();
        alert_picker_rejection = new MutableLiveData<>();
    }


    public <Datatype> void setData(Call<Datatype> func, MutableLiveData<Datatype> func1){
        func.enqueue(new Callback<Datatype>() {
            @Override
            public void onResponse(Call<Datatype> call, Response<Datatype> response) {
                if(response.body() != null){
                    func1.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<Datatype> call, Throwable t) {

            }
        });
    }

    public <Datatype> void setValue(MutableLiveData<Datatype> func, Datatype val){
        func.postValue(val);
    }



}
