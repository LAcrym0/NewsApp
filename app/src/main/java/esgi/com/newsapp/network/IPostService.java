package esgi.com.newsapp.network;

import java.util.List;

import esgi.com.newsapp.model.Post;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Grunt on 11/07/2017.
 */

public interface IPostService {
    @POST("/posts/")
    @Headers("Content-Type: application/json")
    Call<Void> createPost(@Header("Authorization") String token, @Body Post post);

    @GET("/posts/{id}/")
    @Headers("Content-Type: application/json")
    Call<Post> getPost(@Header("Authorization") String token, @Path("id") String id);

    @GET("/posts/")
    @Headers("Content-Type: application/json")
    Call<List<Post>> getPostsList(@Header("Authorization") String token);

    @GET("/posts")
    @Headers("Content-Type: application/json")
    Call<List<Post>> getPostsForTopic(@Header("Authorization") String token, @Query("criteria") String criteria);

    @PUT("/posts/{id}/")
    @Headers("Content-Type: application/json")
    Call<Void> editPost(@Header("Authorization") String token, @Path("id") String id, @Body Post post);

    @DELETE("/posts/{id}/")
    @Headers("Content-Type: application/json")
    Call<Void> deletePost(@Header("Authorization") String token, @Path("id") String id);
}
