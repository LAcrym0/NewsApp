package esgi.com.newsapp.network;

import android.util.Log;

import java.util.List;

import esgi.com.newsapp.R;
import esgi.com.newsapp.database.RealmManager;
import esgi.com.newsapp.model.EditObject;
import esgi.com.newsapp.model.News;
import esgi.com.newsapp.model.Topic;
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

public class NewsService {
    private INewsService newsService;

    //HTTP return codes
    private static final int HTTP_200 = 200;
    private static final int HTTP_201 = 201;
    private static final int HTTP_204 = 204;
    private static final int OFFLINE = -22;

    NewsService(Retrofit retrofit) {
        newsService = retrofit.create(INewsService.class);
    }

    //---------------
    // NEWS CREATION
    //---------------

    /**
     * Method used to create a news
     * @param news the news to create
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void createNews(News news, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.newsService.createNews("Bearer " + PreferencesHelper.getInstance().getToken(), news);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_201) {
                        Log.d(getClass().getSimpleName(), "Return code : " + response.body());
                        Log.d(getClass().getSimpleName(), "News created");
                        Void value = response.body();
                        callback.success(value);
                    }  else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("NewsService", "Error while calling the 'createNews' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            RealmManager.getNewsDAO().createOrUpdateNews(news);
            callback.error(OFFLINE, "Créé en local");
        }
    }

    //---------------
    // NEWS DISPLAY
    //---------------

    /**
     * Method used to get news by id
     * @param id the id string
     * @param callback the callback that returns the news for a success or the return code + message for a failure
     */
    public void getNews(String id, final ApiResult<News> callback) {
        if (Network.isConnectionAvailable()) {
            Call<News> call = this.newsService.getNews("Bearer " + PreferencesHelper.getInstance().getToken(), id);
            call.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got News");
                        News value = response.body();
                        callback.success(value);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    Log.e("NewsService", "Error while calling the 'getNews' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    public void sendUnsyncedContent() {
        final List<News> newsList = RealmManager.getNewsDAO().getNewsOff();
        for (int i = 0; i < newsList.size(); i ++) {
            final int finalI = i;
            RetrofitSession.getInstance().getNewsService().createNews(newsList.get(finalI), new ApiResult<Void>() {
                    @Override
                    public void success(Void res) {
                        RealmManager.getNewsDAO().deleteOfflineNews(newsList.get(finalI).getBddId());
                    }

                    @Override
                    public void error(int code, String message) {

                    }
                });
        }
    }

    /**
     * Method used to get news
     * @param callback the callback that returns the news list for a success or the return code + message for a failure
     */
    public void getNewsList(final ApiResult<List<News>> callback) {
        if (Network.isConnectionAvailable()) {
            Call<List<News>> call = this.newsService.getNewsList("Bearer " + PreferencesHelper.getInstance().getToken());
            call.enqueue(new Callback<List<News>>() {
                @Override
                public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got news list");
                        List<News> values = response.body();
                        RealmManager.getNewsDAO().save(values);
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<News>> call, Throwable t) {
                    Log.e("NewsService", "Error while calling the 'getNewsList' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            // onConnectionError(callback);

            List<News> newsList = RealmManager.getNewsDAO().getList();
            Log.d("NEWSLIST", "GOT THE LIST");
            if (!newsList.isEmpty()){
                callback.success(newsList);
            }else{
                onConnectionError(callback);
            }
        }
    }

    //---------------
    // DELETE NEWS
    //---------------

    /**
     * Method used to delete a news
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void deleteNews(final String id, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.newsService.deleteNews("Bearer " + PreferencesHelper.getInstance().getToken(), id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_204) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Deleted news");
                        Void values = response.body();
                        RealmManager.getNewsDAO().deleteNews(id);
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("NewsService", "Error while calling the 'deleteNews' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // EDIT NEWS
    //---------------

    /**
     * Method used to edit a news
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void editNews(String id, EditObject news, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.newsService.editNews("Bearer " + PreferencesHelper.getInstance().getToken(), id, news);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_204) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Edited news");
                        Void values = response.body();
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("NewsService", "Error while calling the 'editNews' method !", t);
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
