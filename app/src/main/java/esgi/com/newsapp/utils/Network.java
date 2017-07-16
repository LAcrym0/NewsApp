package esgi.com.newsapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import esgi.com.newsapp.database.RealmManager;
import esgi.com.newsapp.network.RetrofitSession;

/**
 * Created by Grunt on 28/06/2017.
 */

public class Network {

    private Network() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isConnectionAvailable() {
        boolean networkAvailable;
        Context context = Utils.getContext();
        if (context == null) {
            return false;
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            networkAvailable =  activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        if (networkAvailable) {
            if (PreferencesHelper.getInstance().getNeedSync() && !PreferencesHelper.getInstance().getLastNetworkState()) {
                PreferencesHelper.getInstance().setLastNetworkState(true);
                boolean needSync = false;
                Log.d("Offline content", "AVAILABLE");
                if (RealmManager.getNewsDAO().needSync()) {
                    RetrofitSession.getInstance().getNewsService().sendUnsyncedContent();
                    needSync = true;
                }
                if (RealmManager.getCommentDAO().needSync()) {
                    RetrofitSession.getInstance().getCommentService().sendUnsyncedContent();
                    needSync = true;
                }
                if (RealmManager.getTopicDAO().needSync()) {
                    RetrofitSession.getInstance().getTopicService().sendUnsyncedContent();
                    needSync = true;
                }
                if (RealmManager.getPostDAO().needSync()) {
                    RetrofitSession.getInstance().getPostService().sendUnsyncedContent();
                    needSync = true;
                }
                PreferencesHelper.getInstance().setNeedSync(needSync);
            }
        }
        else
            PreferencesHelper.getInstance().setLastNetworkState(false);
        return networkAvailable;
    }
}

