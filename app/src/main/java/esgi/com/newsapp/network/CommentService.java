package esgi.com.newsapp.network;

import android.util.Log;

import java.util.List;

import esgi.com.newsapp.R;
import esgi.com.newsapp.database.RealmManager;
import esgi.com.newsapp.model.Comment;
import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.utils.Network;
import esgi.com.newsapp.utils.PreferencesHelper;
import esgi.com.newsapp.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Grunt on 11/07/2017.
 */

public class CommentService {
    private ICommentService commentService;

    //HTTP return codes
    private static final int HTTP_200 = 200;
    private static final int HTTP_201 = 201;
    private static final int HTTP_204 = 204;

    CommentService(Retrofit retrofit) {
        commentService = retrofit.create(ICommentService.class);
    }

    //---------------
    // COMMENT CREATION
    //---------------

    /**
     * Method used to create a comment
     * @param comment the comment to create
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void createComment(Comment comment, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.commentService.createComment("Bearer " + PreferencesHelper.getInstance().getToken(), comment);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_201) {
                        Log.d(getClass().getSimpleName(), "Return code : " + response.body());
                        Log.d(getClass().getSimpleName(), "Comment created");
                        Void value = response.body();
                        callback.success(value);
                    }  else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("CommentService", "Error while calling the 'createComment' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // COMMENT DISPLAY
    //---------------

    /**
     * Method used to get comment by id
     * @param id the id string
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void getComment(String id, final ApiResult<Comment> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Comment> call = this.commentService.getComment("Bearer " + PreferencesHelper.getInstance().getToken(), id);
            call.enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got comment");
                        Comment value = response.body();
                        callback.success(value);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    Log.e("CommentService", "Error while calling the 'getComment' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    /**
     * Method used to get comments for a news
     * @param id the id of the news
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void getCommentsForNews(final String id, final ApiResult<List<Comment>> callback) {
        if (Network.isConnectionAvailable()) {
            String criteria = "{\"offset\":0, \"where\":{\"news\":\"" + id + "\"}}";
            Call<List<Comment>> call = this.commentService.getCommentsForNews("Bearer " + PreferencesHelper.getInstance().getToken(), criteria);
            call.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got comments for news " + id);
                        List<Comment> values = response.body();
                        RealmManager.getCommentDAO().save(values);
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    Log.e("CommentService", "Error while calling the 'getCommentsForNews' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
           //onConnectionError(callback);

            List<Comment> commentList = RealmManager.getCommentDAO().getListForNews(id);
            if (!commentList.isEmpty()){
                callback.success(commentList);
            }else{
                onConnectionError(callback);
            }
        }
    }

    /**
     * Method used to get comments
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void getCommentsList(final ApiResult<List<Comment>> callback) {
        if (Network.isConnectionAvailable()) {
            Call<List<Comment>> call = this.commentService.getCommentsList("Bearer " + PreferencesHelper.getInstance().getToken());
            call.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got comment");
                        List<Comment> values = response.body();
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    Log.e("CommentService", "Error while calling the 'getComment' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // DELETE COMMENT
    //---------------

    /**
     * Method used to delete a comment
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void deleteComment(String id, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.commentService.deleteComment("Bearer " + PreferencesHelper.getInstance().getToken(), id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_204) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Deleted comment");
                        Void values = response.body();
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("CommentService", "Error while calling the 'deleteComment' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // EDIT COMMENT
    //---------------

    /**
     * Method used to edit a comment
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void editComment(String id, Comment comment, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.commentService.editComment("Bearer " + PreferencesHelper.getInstance().getToken(), id, comment);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_204) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Edited comment");
                        Void values = response.body();
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("CommentService", "Error while calling the 'editComment' method !", t);
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
