package esgi.com.newsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import esgi.com.newsapp.database.RealmManager;
import esgi.com.newsapp.model.Token;

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
    private static final String NEED_SYNC = "NeedSync";
    private static final String LAST_NETWORK_STATE = "LastNetworkState";

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
     * Method used to get the token from the database
     * @return the String containing the token
     */
    public String getToken() {
        return RealmManager.getTokenDAO().getToken();
    }

    /**
     * Method used to set the token into the database
     */
    public void setToken(String authToken) {
        RealmManager.getTokenDAO().save(new Token(authToken));
    }

    /**
     * Method used to delete the token
     */
    public void deleteToken() {
        RealmManager.getTokenDAO().deleteToken();
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

    /**
     * Method used to save if the app needs to sync into SharedPreferences
     */
    public void setNeedSync(boolean needSync) {
        INSTANCE.sharedPreferences.edit().putBoolean(NEED_SYNC, needSync).apply();
    }

    /**
     * Method used to know if a sync is needed from SharedPreferences
     * @return the boolean containing sync need
     */
    public boolean getNeedSync() {
        return INSTANCE.sharedPreferences != null && INSTANCE.sharedPreferences.getBoolean(NEED_SYNC, false);
    }

    /**
     * Method used to save if the last network state into SharedPreferences
     */
    public void setLastNetworkState(boolean available) {
        INSTANCE.sharedPreferences.edit().putBoolean(LAST_NETWORK_STATE, available).apply();
    }

    /**
     * Method used to know the last network state from SharedPreferences
     * @return the boolean containing syncing state
     */
    public boolean getLastNetworkState() {
        return INSTANCE.sharedPreferences != null && INSTANCE.sharedPreferences.getBoolean(LAST_NETWORK_STATE, false);
    }
}
