package esgi.com.newsapp.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import esgi.com.newsapp.model.Topic;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by junior on 13/07/2017.
 */

public class TopicDAO {

    Realm realm;

    public TopicDAO(){
        realm = Realm.getDefaultInstance();
    }

    public boolean save(final List<Topic> topicList){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
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
                topicList.add(topic);
            }
        }

        return topicList;
    }
}
