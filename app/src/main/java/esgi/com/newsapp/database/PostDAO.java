package esgi.com.newsapp.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.utils.PreferencesHelper;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by junior on 13/07/2017.
 */

public class PostDAO {

    private Realm realm;

    PostDAO(){

        realm = RealmManager.getRealmInstance();
    }

    public boolean save(final List<Post> postList){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < postList.size(); i ++){
                    postList.get(i).setSynced(true);
                    if(postList.get(i).getBddId() == null) {
                        postList.get(i).setBddId(postList.get(i).getId());
                    }
                }
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
        RealmResults<Post> topicListOff = realm.where(Post.class).equalTo("topic", id).findAll();
        List<Post> topicList = new ArrayList<>();

        if (!topicListOff.isEmpty()){
            Post post;
            for (int i = 0 ; i < topicListOff.size(); i ++){
                post = new Post();
                post.setId(topicListOff.get(i).getId());
                post.setTitle(topicListOff.get(i).getTitle());
                post.setContent(topicListOff.get(i).getContent());
                post.setBddId(topicListOff.get(i).getBddId());
                topicList.add(post);
            }
        }

        return topicList;
    }

    public List<Post> getPostOff(){
        RealmResults<Post> postRealmResults = realm.where(Post.class).equalTo("synced", false).findAll();
        List<Post> topicsList = new ArrayList<>();

        if(!postRealmResults.isEmpty()){
            Post topic;
            for(int i = 0; i < postRealmResults.size(); i ++){
                topic = new Post();
                topic.setId(postRealmResults.get(i).getId());
                topic.setTitle(postRealmResults.get(i).getTitle());
                topic.setContent(postRealmResults.get(i).getContent());
                topic.setDate(postRealmResults.get(i).getDate());
                topic.setTopic(postRealmResults.get(i).getTopic());
                topic.setBddId(postRealmResults.get(i).getBddId());
                topicsList.add(topic);
            }
        }

        return topicsList;
    }

    public boolean needSync() {
        RealmResults<Post> commentRealmResults = realm.where(Post.class).equalTo("synced", false).findAll();
        return commentRealmResults.size() != 0;
    }


    public void createOrUpdatePost(Post post) {
        createOrUpdatePost(post, false);
        PreferencesHelper.getInstance().setNeedSync(true);
    }

    public void createOrUpdatePost(final Post post, boolean synced) {
        post.setSynced(synced);
        if(post.getBddId() == null) {
            if (post.getId() != null)
                post.setBddId(post.getId());
            else
                post.setBddId(UUID.randomUUID().toString());
        }
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

    public void deletePost(final String id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Post.class).equalTo("id", id).findAll().deleteAllFromRealm();
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGPOSTDELETE", "SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGPOSTDELETE", error.toString());
            }
        });
    }

    public void deleteOfflinePost(final String id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Post.class).equalTo("bddId", id).findAll().deleteAllFromRealm();
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGPOSTOFFLINEDELETE", "SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGPOSTOFFLINEDELETE", error.toString());
            }
        });
    }
}
