package com.example.ambulance_demo.api;

import com.example.ambulance_demo.entity.AlertTable;
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

    @PUT("/user/location")
    Call<ErrorTable> createLocation(@Body UserTable userTable);

    @GET("/user/all/location")
    Call<UserTable[]> getVehicles();

    @POST("/alert/user")
    Call<AlertTable> alertUser(@Body AlertTable alertTable);

    @PUT("/alert/user/cancel")
    Call<ErrorTable> cancelAlertUser(@Body AlertTable alertTable);

    @GET("/alert/all")
    Call<AlertTable[]> getAllAlert();

    @PUT("/alert/picker")
    Call<AlertTable> getAcceptAlert(@Body AlertTable alertTable);

    @GET("/alert_user/{user_id}")
    Call<UserTable> getAlertUser(@Path("user_id") int user_id);

    @PUT("/alert/reject")
    Call<ErrorTable> rejectUser(@Body AlertTable alertTable);

    @GET("/alert_picker/{alert_id}")
    Call<UserTable> getAlertPicker(@Path("alert_id") int alert_id);

    @GET("/alert_user_picker/{alert_id}")
    Call<AlertTable> getAlertUserPicker(@Path("alert_id") int alert_id);

    @GET("/alert_picker_user/{alert_id}")
    Call<AlertTable> getAlertPickerUser(@Path("alert_id") int alert_id);
}
