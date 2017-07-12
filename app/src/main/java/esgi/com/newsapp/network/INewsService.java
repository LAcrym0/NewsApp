package esgi.com.newsapp.network;

import java.util.List;

import esgi.com.newsapp.model.News;
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

public interface INewsService {
    @POST("/news/")
    @Headers("Content-Type: application/json")
    Call<Void> createNews(@Header("Authorization") String token, @Body News news);

    @GET("/news/{id}/")
    @Headers("Content-Type: application/json")
    Call<News> getNews(@Header("Authorization") String token, @Path("id") String id);

    @GET("/news/")
    @Headers("Content-Type: application/json")
    Call<List<News>> getNewsList(@Header("Authorization") String token);

    @PUT("/news/{id}/")
    @Headers("Content-Type: application/json")
    Call<Void> editNews(@Header("Authorization") String token, @Path("id") String id, @Body News news);

    @DELETE("/news/{id}/")
    @Headers("Content-Type: application/json")
    Call<Void> deleteNews(@Header("Authorization") String token, @Path("id") String id);
}
