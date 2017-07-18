package esgi.com.newsapp.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Grunt on 18/07/2017.
 */

public class Token extends RealmObject {

    @Index
    @PrimaryKey
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public Token(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
