package esgi.com.newsapp.database;

import android.annotation.SuppressLint;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Grunt on 13/07/2017.
 */

public class RealmManager {

    @SuppressLint("StaticFieldLeak")
    private static Realm realm;

    private static TopicDAO topicDAO;
    private static PostDAO postDAO;
    private static NewsDAO newsDAO;

    public static Realm getRealmInstance() {
        return realm;
    }

    public static void initRealmManager() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        topicDAO = new TopicDAO();
        postDAO = new PostDAO();
        newsDAO = new NewsDAO();
    }

    public static TopicDAO getTopicDAO() {
        return topicDAO;
    }

    public static  PostDAO getPostDAO(){
        return postDAO;
    }

    public static NewsDAO getNewsDAO() {
        return newsDAO;
    }
}
