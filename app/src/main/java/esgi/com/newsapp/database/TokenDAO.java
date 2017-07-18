package esgi.com.newsapp.database;

import android.util.Log;

import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.model.Token;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Grunt on 18/07/2017.
 */

public class TokenDAO {
    private Realm realm;

    TokenDAO(){
        realm = RealmManager.getRealmInstance();
    }

    public void save(final Token token){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(token);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGTOKEN","SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGTOKEN",error.toString());
            }
        });
    }

    public String getToken(){
        Token tokenResult = realm.where(Token.class).findFirst();
        return tokenResult == null ? null : tokenResult.getToken();
    }

    public void deleteToken() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Token.class).findAll().deleteAllFromRealm();
            }
        },new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("TAGTOKENDELETE", "SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("TAGTOKENDELETE", error.toString());
            }
        });
    }
}
