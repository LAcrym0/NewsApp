package esgi.com.newsapp.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import esgi.com.newsapp.model.News;
import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.model.Topic;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by junior on 14/07/2017.
 */

public class NewsDAO {

    Realm realm;

    public NewsDAO(){
        realm = RealmManager.getRealmInstance();
    }


    public void save(final List<News> newsList){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(newsList);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("NEWSAVE","SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("NEWSAVE",error.toString());
            }
        });
    }

    public List<News>getList(){
        RealmResults<News> newsList = realm.where(News.class).findAll();
        List<News> newsListBack = new ArrayList<>();

        if (!newsList.isEmpty()){
            News news ;
            for (int i = 0 ; i < newsList.size();i++){
                news = new News();
                news.setId(newsList.get(i).getId());
                news.setTitle(newsList.get(i).getTitle());
                news.setContent(newsList.get(i).getContent());
                newsListBack.add(news);
            }
        }

        return newsListBack;
    }



}
