package esgi.com.newsapp.network;

import java.util.List;

import esgi.com.newsapp.model.Comment;
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

public interface ICommentService {
    @POST("/comments/")
    @Headers("Content-Type: application/json")
    Call<Void> createComment(@Header("Authorization") String token, @Body Comment comment);

    @GET("/comments/{id}/")
    @Headers("Content-Type: application/json")
    Call<Comment> getComment(@Header("Authorization") String token, @Path("id") String id);

    @GET("/comments/")
    @Headers("Content-Type: application/json")
    Call<List<Comment>> getCommentsList(@Header("Authorization") String token);

    @GET("/comments")
    @Headers("Content-Type: application/json")
    Call<List<Comment>> getCommentsForNews(@Header("Authorization") String token, @Query("criteria") String criteria);

    @PUT("/comments/{id}/")
    @Headers("Content-Type: application/json")
    Call<Void> editComment(@Header("Authorization") String token, @Path("id") String id, @Body Comment comment);

    @DELETE("/comments/{id}/")
    @Headers("Content-Type: application/json")
    Call<Void> deleteComment(@Header("Authorization") String token, @Path("id") String id);
}
