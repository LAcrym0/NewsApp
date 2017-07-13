package esgi.com.newsapp.network;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import esgi.com.newsapp.R;
import esgi.com.newsapp.database.TopicDAO;
import esgi.com.newsapp.model.Topic;
import esgi.com.newsapp.utils.Network;
import esgi.com.newsapp.utils.PreferencesHelper;
import esgi.com.newsapp.utils.Utils;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Grunt on 12/07/2017.
 */

public class TopicService {
    private ITopicService topicService;

    //HTTP return codes
    private static final int HTTP_200 = 200;
    private static final int HTTP_201 = 201;
    private static final int HTTP_204 = 204;

    Realm realm;
    private TopicDAO topicDAO;
    private RealmResults<Topic> topicListOff;

    TopicService(Retrofit retrofit) {
        realm = Realm.getDefaultInstance();
        topicDAO = new TopicDAO();
        topicService = retrofit.create(ITopicService.class

        );

    }

    //---------------
    // TOPIC CREATION
    //---------------

    //validated
    /**
     * Method used to create a topic
     * @param topic the topic to create
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void createTopic(Topic topic, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.topicService.createTopic("Bearer " + PreferencesHelper.getInstance().getToken(), topic);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_201) {
                        Log.d(getClass().getSimpleName(), "Return code : " + response.body());
                        Log.d(getClass().getSimpleName(), "Topic created");
                        Void value = response.body();
                        callback.success(value);
                    }  else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("TopicService", "Error while calling the 'createTopic' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // TOPIC DISPLAY
    //---------------

    /**
     * Method used to get topic by id
     * @param id the id string
     * @param callback the callback that returns the topic for a success or the return code + message for a failure
     */
    public void getTopic(String id, final ApiResult<Topic> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Topic> call = this.topicService.getTopic("Bearer " + PreferencesHelper.getInstance().getToken(), id);
            call.enqueue(new Callback<Topic>() {
                @Override
                public void onResponse(Call<Topic> call, Response<Topic> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got Topic");
                        Topic value = response.body();
                        callback.success(value);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Topic> call, Throwable t) {
                    Log.e("TopicService", "Error while calling the 'getTopic' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //validated
    /**
     * Method used to get topics
     * @param callback the callback that returns the topic list for a success or the return code + message for a failure
     */
    public void getTopicList(final ApiResult<List<Topic>> callback) {
        if (Network.isConnectionAvailable()) {
            Call<List<Topic>> call = this.topicService.getTopicsList("Bearer " + PreferencesHelper.getInstance().getToken());
            call.enqueue(new Callback<List<Topic>>() {
                @Override
                public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got topic list");
                        final List<Topic> values = response.body();
                        topicDAO.save(values);
                        callback.success(values);

                    }
                }

                @Override
                public void onFailure(Call<List<Topic>> call, Throwable t) {
                    Log.e("TopicService", "Error while calling the 'getTopicList' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {

            List<Topic> topicList = topicDAO.getList();
            if (!topicList.isEmpty()){
                callback.success(topicList);
            }else{
                onConnectionError(callback);
            }

        }
    }

    //---------------
    // DELETE TOPIC
    //---------------

    /**
     * Method used to delete a topic
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void deleteTopic(String id, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.topicService.deleteTopic("Bearer " + PreferencesHelper.getInstance().getToken(), id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_204) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Deleted topic");
                        Void values = response.body();
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("TopicService", "Error while calling the 'deleteTopic' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // EDIT TOPIC
    //---------------

    /**
     * Method used to edit a topic
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void editTopic(String id, Topic topic, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.topicService.editTopic("Bearer " + PreferencesHelper.getInstance().getToken(), id, topic);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_204) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Edited topic");
                        Void values = response.body();
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("TopicService", "Error while calling the 'editTopic' method !", t);
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
