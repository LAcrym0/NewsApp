package esgi.com.newsapp.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Grunt on 28/06/2017.
 */

public class Network {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private Network() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isConnectionAvailable() {
        Context context = Utils.getContext();
        if (context == null) {
            return false;
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
    }
}

