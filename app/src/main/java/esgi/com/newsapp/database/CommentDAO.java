package esgi.com.newsapp.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import esgi.com.newsapp.model.Comment;
import esgi.com.newsapp.utils.PreferencesHelper;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by junior on 15/07/2017.
 */

public class CommentDAO {

    private Realm realm;

    CommentDAO(){

        realm = RealmManager.getRealmInstance();

    }

    public boolean save(final List<Comment> commentList){
        for(int i = 0 ; i < commentList.size(); i ++){
            commentList.get(i).setSynced(true);
            if(commentList.get(i).getBddId() == null) {
                commentList.get(i).setBddId(commentList.get(i).getId());
            }
        }

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
        RealmResults<Comment> commentRealmResults = realm.where(Comment.class).equalTo("news", id).findAll();
        List<Comment> commentList = new ArrayList<>();

        if (!commentRealmResults.isEmpty()){
            Comment comment ;
            for (int i = 0 ; i < commentRealmResults.size();i++){
                comment = new Comment();
                comment.setId(commentRealmResults.get(i).getId());
                comment.setTitle(commentRealmResults.get(i).getTitle());
                comment.setContent(commentRealmResults.get(i).getContent());
                comment.setNews(commentRealmResults.get(i).getNews());
                comment.setBddId(commentRealmResults.get(i).getBddId());
                commentList.add(comment);
            }
        }

        return commentList;
    }

    public List<Comment> getCommentOff(){
        RealmResults<Comment> commentRealmResults = realm.where(Comment.class).equalTo("synced", false).findAll();
        List<Comment> commentList = new ArrayList<>();

        if(!commentRealmResults.isEmpty()){
            Comment comment;
            for(int i = 0; i < commentRealmResults.size(); i ++){
                comment = new Comment();
                comment.setId(commentRealmResults.get(i).getId());
                comment.setTitle(commentRealmResults.get(i).getTitle());
                comment.setContent(commentRealmResults.get(i).getContent());
                comment.setNews(commentRealmResults.get(i).getNews());
                comment.setDate(commentRealmResults.get(i).getDate());
                comment.setBddId(commentRealmResults.get(i).getBddId());
                commentList.add(comment);
            }
        }

        return commentList;
    }

    public boolean needSync() {
        RealmResults<Comment> commentRealmResults = realm.where(Comment.class).equalTo("synced", false).findAll();
        return commentRealmResults.size() != 0;
    }

    public void createOrUpdateComment(Comment comment) {
        createOrUpdateComment(comment, false);
        PreferencesHelper.getInstance().setNeedSync(true);
    }

    public void createOrUpdateComment(final Comment comment, boolean synced) {
        comment.setSynced(synced);
        if(comment.getBddId() == null) {
            comment.setBddId(UUID.randomUUID().toString());
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(comment);
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGCOMMENTCREATE","SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGCOMMENTCREATE",error.toString());
            }
        });

    }

    public void deleteComment(final String id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Comment.class).equalTo("id", id).findAll().deleteAllFromRealm();
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGCOMMENTDELETE", "SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGCOMMENTDELETE", error.toString());
            }
        });
    }

    public void deleteOfflineComment(final String id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Comment.class).equalTo("bddId", id).findAll().deleteAllFromRealm();
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGCOMMENTOFFLINEDELETE", "SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGCOMMENTOFFLINEDELETE", error.toString());
            }
        });
    }

}
