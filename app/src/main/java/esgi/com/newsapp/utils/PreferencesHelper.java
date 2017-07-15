package esgi.com.newsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Grunt on 28/06/2017.
 */

/**
 * This class is used to store and get the user's token
 */
public class PreferencesHelper {
    //unique instance
    private static final PreferencesHelper INSTANCE = new PreferencesHelper();
    //bool used to verify if the class has already been initialized
    private boolean alreadyInit = false;

    private SharedPreferences sharedPreferences;
    private static final String AUTH_TOKEN_NAME = "NewsAppAuthToken";
    private static final String USER_ID = "UserId";

    //only method to call because the constructor is private
    public static PreferencesHelper getInstance() {
        return INSTANCE;
    }

    //private constructor
    private PreferencesHelper() {
        //does nothing
    }

    //method to call once to init, secured in case of multiple calls
    public void init(Context context) {
        if (INSTANCE.alreadyInit) {
            return;
        }
        //init and remember it has already been initialized
        INSTANCE.sharedPreferences = context.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
        INSTANCE.alreadyInit = true;
    }

    /**
     * Method used to get the token from SharedPreferences
     * @return the String containing the token
     */
    public String getToken() {
        return INSTANCE.sharedPreferences == null ? null : INSTANCE.sharedPreferences.getString(AUTH_TOKEN_NAME, null);
    }

    /**
     * Method used to set the token into SharedPreferences
     */
    public void setToken(String authToken) {
        INSTANCE.sharedPreferences.edit().putString(AUTH_TOKEN_NAME, authToken).apply();
    }

    /**
     * Method used to get the user id from SharedPreferences
     * @return the String containing the id
     */
    public String getUserId() {
        return INSTANCE.sharedPreferences == null ? null : INSTANCE.sharedPreferences.getString(USER_ID, null);
    }

    /**
     * Method used to set the user id into SharedPreferences
     */
    public void setUserId(String userId) {
        INSTANCE.sharedPreferences.edit().putString(USER_ID, userId).apply();
    }
}
