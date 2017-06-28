package esgi.com.newsapp.network;

import android.util.Log;

import esgi.com.newsapp.R;
import esgi.com.newsapp.utils.Network;
import esgi.com.newsapp.utils.Utils;
import esgi.com.newsapp.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Grunt on 28/06/2017.
 */

public class UserService {
    private IUserService userService;
    private static final int HTTP_200 = 200;
    private static final int HTTP_201 = 201;
    /*public static final int HTTP_204 = 204;
    public static final int HTTP_400 = 400;
    public static final int HTTP_401 = 401;
    public static final int HTTP_404 = 404;*/
    private static final String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";

    UserService(Retrofit retrofit) {
        userService = retrofit.create(IUserService.class);
    }

    /* AUTHENTICATION */

    public void login(User user, final ApiResult<String> callback) {
        if (Network.isConnectionAvailable()) {
            Call<String> call = this.userService.login(user);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "token : " + response.body());
                        //PreferenceHelper.getInstance().setToken(authToken);
                        //initRetrofitClient();
                        String token = response.body();
                        callback.success(token);

                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(getClass().getSimpleName(), "Error while calling the 'login' method!", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    public void createAccount(User user, final ApiResult<Void> callback) {
        if (Network.isConnectionAvailable()) {
            Call<Void> call = this.userService.createAccount(user);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_201) {
                        Log.d(getClass().getSimpleName(), "Return code : " + response.body());
                        //PreferenceHelper.getInstance().setToken(authToken);
                        //initRetrofitClient();
                        Void value = response.body();
                        callback.success(value);

                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("UserService", "Error while calling the 'createAccount' method!", t);
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
