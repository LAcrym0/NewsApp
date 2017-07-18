package esgi.com.newsapp.network;

import java.util.List;

import esgi.com.newsapp.model.Auth;
import esgi.com.newsapp.model.EditUser;
import esgi.com.newsapp.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Grunt on 28/06/2017.
 */

interface IUserService {
    @POST("/auth/login/")
    @Headers("Content-Type: application/json")
    Call<String> login(@Body Auth auth);

    @POST("/auth/subscribe/")
    @Headers("Content-Type: application/json")
    Call<Void> createAccount(@Body User user);

    @GET("/users/me/")
    @Headers("Content-Type: application/json")
    Call<User> getMyInformation(@Header("Authorization") String token);

    @GET("/users/")
    @Headers("Content-Type: application/json")
    Call<List<User>> getUsersList();

    @PUT("/users/i/")//todo remove "i/" when API will be fixed (tip)
    @Headers("Content-Type: application/json")
    Call<User> editUser(@Header("Authorization") String token, @Body EditUser user);
}
