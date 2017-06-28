package esgi.com.newsapp.application;

import android.app.Application;

import esgi.com.newsapp.utils.PreferencesHelper;
import esgi.com.newsapp.utils.Utils;

/**
 * Created by Grunt on 28/06/2017.
 */

public class NewsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        PreferencesHelper.getInstance().init(this);
    }
}
