package esgi.com.newsapp.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import esgi.com.newsapp.model.Topic;
import esgi.com.newsapp.utils.PreferencesHelper;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by junior on 13/07/2017.
 */

public class TopicDAO {

    private Realm realm;

    TopicDAO(){
        realm = RealmManager.getRealmInstance();
    }

    public boolean save(final List<Topic> topicList){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < topicList.size(); i ++) {
                    topicList.get(i).setSynced(true);
                    if(topicList.get(i).getBddId() == null) {
                        topicList.get(i).setBddId(topicList.get(i).getId());
                    }
                }
                realm.copyToRealmOrUpdate(topicList);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGLISTOPIC","SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGLISTOPIC",error.toString());
            }
        });
        return false;
    }

    public List<Topic> getTopicOff(){
        RealmResults<Topic> topicRealmResults = realm.where(Topic.class).equalTo("synced", false).findAll();
        List<Topic> topicsList = new ArrayList<>();

        if(!topicRealmResults.isEmpty()){
            Topic topic;
            for(int i =0; i < topicRealmResults.size(); i ++){
                topic = new Topic();
                topic.setId(topicRealmResults.get(i).getId());
                topic.setTitle(topicRealmResults.get(i).getTitle());
                topic.setContent(topicRealmResults.get(i).getContent());
                topic.setDate(topicRealmResults.get(i).getDate());
                topic.setBddId(topicRealmResults.get(i).getBddId());
                topicsList.add(topic);
            }
        }

        return topicsList;
    }

    public List<Topic> getList(){
        RealmResults<Topic> topicListOff = realm.where(Topic.class).findAll();
        List<Topic> topicList = new ArrayList<>();

        if (!topicListOff.isEmpty()){
            Topic topic ;
            for (int i = 0 ; i < topicListOff.size();i++){
                topic = new Topic();
                topic.setId(topicListOff.get(i).getId());
                topic.setTitle(topicListOff.get(i).getTitle());
                topic.setContent(topicListOff.get(i).getContent());
                topic.setBddId(topicListOff.get(i).getBddId());
                topicList.add(topic);
            }
        }

        return topicList;
    }

    public boolean needSync() {
        RealmResults<Topic> commentRealmResults = realm.where(Topic.class).equalTo("synced", false).findAll();
        return commentRealmResults.size() != 0;
    }

    public void createOrUpdateTopic(Topic topic) {
        createOrUpdateTopic(topic, false);
        PreferencesHelper.getInstance().setNeedSync(true);
    }

    public void createOrUpdateTopic(final Topic topic, final boolean synced) {
        topic.setSynced(synced);
        if(topic.getBddId() == null) {
            if (topic.getId() != null)
                topic.setBddId(topic.getId());
            else
                topic.setBddId(UUID.randomUUID().toString());
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (synced) {
                    realm.where(Topic.class).equalTo("bddId", topic.getBddId()).findAll().deleteAllFromRealm();
                }
                realm.copyToRealmOrUpdate(topic);
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGTOPICCREATE", "SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGTOPICCREATE", error.toString());
            }
        });

    }

    public void deleteTopic(final String id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Topic.class).equalTo("id", id).findAll().deleteAllFromRealm();
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

    public void deleteOfflineTopic(final String id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Topic.class).equalTo("bddId", id).findAll().deleteAllFromRealm();
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGTOPICOFFLINEDELETE", "SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGTOPICOFFLINEDELETE", error.toString());
            }
        });
    }
}
