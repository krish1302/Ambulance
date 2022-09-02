package com.example.ambulance_demo.client;

import com.example.ambulance_demo.api.OnlineApiInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OnlineClient {
    private static final String BASE_URL = "http://192.168.1.4:3000/";
    private static Retrofit retrofit = null;
    private static OnlineApiInterface onlineApiInterface = null;

    public static Retrofit getRetrofit(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static OnlineApiInterface getInterface(){
        if(onlineApiInterface == null){
            onlineApiInterface = getRetrofit().create(OnlineApiInterface.class);
        }
        return onlineApiInterface;
    }
}
