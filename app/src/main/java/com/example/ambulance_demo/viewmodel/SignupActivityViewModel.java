package com.example.ambulance_demo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ambulance_demo.entity.ErrorTable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivityViewModel extends ViewModel {
    public MutableLiveData<ErrorTable> user;

    public SignupActivityViewModel(){
        user = new MutableLiveData<>();
    }

    public <Datatype> void setValue(MutableLiveData<Datatype> func, Datatype val){
        func.postValue(val);
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

}
