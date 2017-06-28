package esgi.com.newsapp.network;

import esgi.com.newsapp.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Grunt on 28/06/2017.
 */

public interface IUserService {
    @POST("/auth/login/")
    Call<String> login(@Body User user);

    @POST("/auth/subscribe/")
    Call<Void> createAccount(@Body User user);
}
