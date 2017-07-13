package esgi.com.newsapp.network;

import android.util.Log;

import java.util.List;

import esgi.com.newsapp.R;
import esgi.com.newsapp.database.RealmManager;
import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.model.Topic;
import esgi.com.newsapp.utils.Network;
import esgi.com.newsapp.utils.PreferencesHelper;
import esgi.com.newsapp.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Grunt on 12/07/2017.
 */

public class PostService {
    private IPostService postService;

    //HTTP return codes
    private static final int HTTP_200 = 200;
    private static final int HTTP_201 = 201;
    private static final int HTTP_204 = 204;

    PostService(Retrofit retrofit) {
        postService = retrofit.create(IPostService.class);
    }

    //---------------
    // POST CREATION
    //---------------

    //validated
    /**
     * Method used to create a post
     * @param post the post to create
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void createPost(Post post, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.postService.createPost("Bearer " + PreferencesHelper.getInstance().getToken(), post);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_201) {
                        Log.d(getClass().getSimpleName(), "Return code : " + response.body());
                        Log.d(getClass().getSimpleName(), "Post created");
                        Void value = response.body();
                        callback.success(value);
                    }  else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("PostService", "Error while calling the 'createPost' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // POST DISPLAY
    //---------------

    //validated
    /**
     * Method used to get post by id
     * @param id the id string
     * @param callback the callback that returns the post for a success or the return code + message for a failure
     */
    public void getPost(String id, final ApiResult<Post> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Post> call = this.postService.getPost("Bearer " + PreferencesHelper.getInstance().getToken(), id);
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got Post");
                        Post value = response.body();
                        callback.success(value);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    Log.e("PostService", "Error while calling the 'getPost' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //validated
    /**
     * Method used to get posts
     * @param callback the callback that returns the post list for a success or the return code + message for a failure
     */
    public void getPostList(final ApiResult<List<Post>> callback) {
        if (Network.isConnectionAvailable()) {
            Call<List<Post>> call = this.postService.getPostsList("Bearer " + PreferencesHelper.getInstance().getToken());
            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got post list");
                        List<Post> values = response.body();
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    Log.e("PostService", "Error while calling the 'getPostList' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    /**
     * Method used to get comments for a news
     * @param id the id of the topic
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void getPostsForTopic(final String id, final ApiResult<List<Post>> callback) {
        if (Network.isConnectionAvailable()) {
            String criteria = "{\"offset\":0, \"where\":{\"topic\":\"" + id + "\"}}";
            Call<List<Post>> call = this.postService.getPostsForTopic("Bearer " + PreferencesHelper.getInstance().getToken(), criteria);
            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got comments for topic " + id);
                        List<Post> values = response.body();
                        RealmManager.getPostDAO().save(values);
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    Log.e("CommentService", "Error while calling the 'getPostsForTopic' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {

            List<Post> postList = RealmManager.getPostDAO().getListForTopic(id);
            if (!postList.isEmpty()){
                callback.success(postList);
            }else{
                onConnectionError(callback);
            }
            //onConnectionError(callback);
        }
    }

    //---------------
    // DELETE POST
    //---------------

    /**
     * Method used to delete a post
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void deletePost(String id, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.postService.deletePost("Bearer " + PreferencesHelper.getInstance().getToken(), id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_204) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Deleted post");
                        Void values = response.body();
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("PostService", "Error while calling the 'deletePost' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // EDIT POST
    //---------------

    /**
     * Method used to edit a post
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void editPost(String id, Post post, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.postService.editPost("Bearer " + PreferencesHelper.getInstance().getToken(), id, post);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_204) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Edited post");
                        Void values = response.body();
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("PostService", "Error while calling the 'editPost' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    private void onConnectionError(final ApiResult callback) {
        String error = Utils.getContext().getString(R.string.error_network_not_available);
        Log.d(getClass().getSimpleName(), error);
        callback.error(-1, error);
    }
}
