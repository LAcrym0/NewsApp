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
    private static CommentDAO commentDAO;
    private static TokenDAO tokenDAO;

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
        commentDAO = new CommentDAO();
        tokenDAO = new TokenDAO();
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

    public static CommentDAO getCommentDAO(){
        return commentDAO;
    }

    public static TokenDAO getTokenDAO() {
        return tokenDAO;
    }
}
