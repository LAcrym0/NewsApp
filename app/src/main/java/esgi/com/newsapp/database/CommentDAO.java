package esgi.com.newsapp.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import esgi.com.newsapp.model.Comment;
import esgi.com.newsapp.model.Post;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by junior on 15/07/2017.
 */

public class CommentDAO {

    Realm realm;

    public CommentDAO(){

        realm = RealmManager.getRealmInstance();
    }

    public boolean save(final List<Comment> commentList){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(commentList);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("COMMENTSAVE","SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("COMMENTSAVE",error.toString());
            }
        });
        return false;
    }

    public List<Comment> getListForNews(String id){
        RealmResults<Comment> commentRealmResults = realm.where(Comment.class).equalTo("news",id).findAll();
        List<Comment> commentList = new ArrayList<>();

        if (!commentRealmResults.isEmpty()){
            Comment comment ;
            for (int i = 0 ; i < commentRealmResults.size();i++){
                comment = new Comment();
                comment.setId(commentRealmResults.get(i).getId());
                comment.setTitle(commentRealmResults.get(i).getTitle());
                comment.setContent(commentRealmResults.get(i).getContent());
                comment.setNews(commentRealmResults.get(i).getNews());
                commentList.add(comment);
            }
        }

        return commentList;
    }


    /*public void createPost(Post post) {
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

    }*/

}
