package esgi.com.newsapp.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import esgi.com.newsapp.model.News;
import esgi.com.newsapp.utils.PreferencesHelper;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by junior on 14/07/2017.
 */

public class NewsDAO {

    private Realm realm;

    NewsDAO(){
        realm = RealmManager.getRealmInstance();
    }


    public void save(final List<News> newsList) {

        for (int i = 0; i < newsList.size(); i ++) {
            newsList.get(i).setSynced(true);
            if(newsList.get(i).getBddId() == null) {
                newsList.get(i).setBddId(newsList.get(i).getId());
            }
        }
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

    public List<News> getList(){
        RealmResults<News> newsList = realm.where(News.class).findAll();
        List<News> newsListBack = new ArrayList<>();

        if (!newsList.isEmpty()){
            News news ;
            for (int i = 0 ; i < newsList.size();i++){
                news = new News();
                news.setId(newsList.get(i).getId());
                news.setTitle(newsList.get(i).getTitle());
                news.setContent(newsList.get(i).getContent());
                news.setDate(newsList.get(i).getDate());
                news.setBddId(newsList.get(i).getBddId());
                newsListBack.add(news);
            }
        }

        return newsListBack;
    }

    public List<News> getNewsOff(){
        RealmResults<News> newsRealmResults = realm.where(News.class).equalTo("synced", false).findAll();
        List<News> newsList = new ArrayList<>();

        if(!newsRealmResults.isEmpty()){
            News news;
            for (int i = 0; i < newsRealmResults.size(); i ++){
                news = new News();
                news.setId(newsRealmResults.get(i).getId());
                news.setTitle(newsRealmResults.get(i).getTitle());
                news.setContent(newsRealmResults.get(i).getContent());
                news.setDate(newsRealmResults.get(i).getDate());
                news.setBddId(newsRealmResults.get(i).getBddId());
                newsList.add(news);
            }
        }

        return newsList;
    }

    public boolean needSync() {
        RealmResults<News> commentRealmResults = realm.where(News.class).equalTo("synced", false).findAll();
        return commentRealmResults.size() != 0;
    }

    public void createOrUpdateNews(News news) {
        createOrUpdateNews(news, false);
        PreferencesHelper.getInstance().setNeedSync(true);
    }

    public void createOrUpdateNews(final News news, final boolean synced) {
        news.setSynced(synced);
        if(news.getBddId() == null) {
            if (news.getId() != null)
                news.setBddId(news.getId());
            else
                news.setBddId(UUID.randomUUID().toString());
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (synced) {
                    realm.where(News.class).equalTo("bddId", news.getBddId()).findAll().deleteAllFromRealm();
                }
                realm.copyToRealmOrUpdate(news);
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGNEWSCREATE", "SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGNEWSCREATE", error.toString());
            }
        });
    }

    public void deleteNews(final String id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
               realm.where(News.class).equalTo("id", id).findAll().deleteAllFromRealm();
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGNEWSDELETE", "SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGNEWSDELETE", error.toString());
            }
        });
    }

    public void deleteOfflineNews(final String id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(News.class).equalTo("bddId", id).findAll().deleteAllFromRealm();
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGNEWSOFFLINEDELETE", "SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGNEWSOFFLINEDELETE", error.toString());
            }
        });
    }
}
