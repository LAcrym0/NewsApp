package esgi.com.newsapp.network;

import java.util.List;

import esgi.com.newsapp.model.EditObject;
import esgi.com.newsapp.model.Topic;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Grunt on 11/07/2017.
 */

interface ITopicService {
    @POST("/topics/")
    @Headers("Content-Type: application/json")
    Call<Void> createTopic(@Header("Authorization") String token, @Body Topic topic);

    @GET("/topics/{id}/")
    @Headers("Content-Type: application/json")
    Call<Topic> getTopic(@Header("Authorization") String token, @Path("id") String id);

    @GET("/topics/")
    @Headers("Content-Type: application/json")
    Call<List<Topic>> getTopicsList(@Header("Authorization") String token);

    @PUT("/topics/{id}/")
    @Headers("Content-Type: application/json")
    Call<Void> editTopic(@Header("Authorization") String token, @Path("id") String id, @Body EditObject topic);

    @DELETE("/topics/{id}/")
    @Headers("Content-Type: application/json")
    Call<Void> deleteTopic(@Header("Authorization") String token, @Path("id") String id);
}
