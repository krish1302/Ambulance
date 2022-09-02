package com.example.ambulance_demo.api;

import com.example.ambulance_demo.entity.ErrorTable;
import com.example.ambulance_demo.entity.UserTable;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OnlineApiInterface {

    @POST("/signup")
    Call<ErrorTable> createUser(@Body UserTable userTable);

    @GET("/login/{email}/{password}")
    Call<UserTable> checkUser(@Path("email") String email, @Path("password") String password);

    @PUT("/user")
    Call<ErrorTable> createLocation(@Body UserTable userTable);

    @GET("/lat/{email}")
    Call<UserTable> checkLocation(@Path("email") String email);

    @GET("allusers/lat")
    Call<UserTable[]> getAllLocation();

    @POST("alert/user")
    Call<Void> alertUser();
}
