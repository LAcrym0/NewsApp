package esgi.com.newsapp.network;

import android.util.Log;

import java.util.List;

import esgi.com.newsapp.R;
import esgi.com.newsapp.model.Auth;
import esgi.com.newsapp.model.EditUser;
import esgi.com.newsapp.utils.Network;
import esgi.com.newsapp.utils.PreferencesHelper;
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

    //HTTP return codes
    private static final int HTTP_200 = 200;
    private static final int HTTP_201 = 201;
    private static final int HTTP_204 = 204;

    UserService(Retrofit retrofit) {
        userService = retrofit.create(IUserService.class);
    }

    //---------------
    // AUTHENTICATION
    //---------------

    /**
     * Method used to login
     * @param auth the user's credentiels to log in
     * @param callback the callback that returns the token string for a success or the return code + message for a failure
     */
    public void login(Auth auth, final ApiResult<String> callback) {
        if (Network.isConnectionAvailable()) {
            Call<String> call = this.userService.login(auth);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "token : " + response.body());
                        PreferencesHelper.getInstance().setToken(response.body());
                        String token = response.body();
                        callback.success(token);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(getClass().getSimpleName(), "Error while calling the 'login' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // ACCOUNT CREATION
    //---------------

    /**
     * Method used to create an account
     * @param user the user to create an account for
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
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
                        Log.d(getClass().getSimpleName(), "Account created");
                        Void value = response.body();
                        callback.success(value);
                    } else if (statusCode == HTTP_200){
                        Log.d(getClass().getSimpleName(), "Account already existing");
                        callback.error(statusCode, response.message());
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("UserService", "Error while calling the 'createAccount' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // USER DISPLAY
    //---------------

    /**
     * Method used to get logged user's information
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void getMyInformation(final ApiResult<User> callback) {
        if (Network.isConnectionAvailable()) {
            Call<User> call = this.userService.getMyInformation("Bearer " + PreferencesHelper.getInstance().getToken());
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got user information");
                        User value = response.body();
                        callback.success(value);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("UserService", "Error while calling the 'getMyInformation' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    /**
     * Method used to get the user's list
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void getUsersList(final ApiResult<List<User>> callback) {
        if (Network.isConnectionAvailable()) {
            Call<List<User>> call = this.userService.getUsersList();
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got usersList");
                        List<User> values = response.body();
                        callback.success(values);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.e("UserService", "Error while calling the 'getUsersList' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // ACCOUNT EDITION
    //---------------

    /**
     * Method used to get edit the logged user
     * @param user the user object containing the fields to edit
     * @param callback the callback that returns nothing for a success or the return code + message for a failure
     */
    public void editUser(EditUser user, final ApiResult<User> callback) {
        if (Network.isConnectionAvailable()) {
            Call<User> call = this.userService.editUser("Bearer " + PreferencesHelper.getInstance().getToken(), user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    int statusCode = response.code();
                    System.out.println("Return code : " + statusCode);
                    if (statusCode == HTTP_204) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "User edited");
                        User value = response.body();
                        callback.success(value);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("UserService", "Error while calling the 'editUser' method !", t);
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
