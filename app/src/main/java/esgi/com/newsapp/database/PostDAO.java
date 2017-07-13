package esgi.com.newsapp.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.model.Topic;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by junior on 13/07/2017.
 */

public class PostDAO {

    Realm realm;

    public PostDAO(){

        realm = RealmManager.getRealmInstance();
    }

    public boolean save(final List<Post> postList){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(postList);
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

    public List<Post> getListForTopic(String id){
        RealmResults<Post> topicListOff = realm.where(Post.class).equalTo("topic",id).findAll();
        List<Post> topicList = new ArrayList<>();

        if (!topicListOff.isEmpty()){
            Post post ;
            for (int i = 0 ; i < topicListOff.size();i++){
                post = new Post();
                post.setId(topicListOff.get(i).getId());
                post.setTitle(topicListOff.get(i).getTitle());
                post.setContent(topicListOff.get(i).getContent());
                topicList.add(post);
            }
        }

        return topicList;
    }


    public void createPost(Post post) {
        createPost(post, false);
    }

    public void createPost(final Post post, boolean synced) {
        post.setSynced(synced);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(post);
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGPOSTCREATE","SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGPOSTCREATE",error.toString());
            }
        });

    }


}
