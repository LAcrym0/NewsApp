package esgi.com.newsapp.model;

import com.google.gson.annotations.SerializedName;

import esgi.com.newsapp.network.Exclude;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Grunt on 11/07/2017.
 */

public class Topic extends RealmObject {


    @SerializedName("_id")
    private String id;

    @Exclude
    @Index
    @PrimaryKey
    private String bddId;

    private String author;

    @Required
    @Index
    private String title;

    @Required
    private String content;

    @Exclude
    private boolean synced;

    private String date;

    public Topic(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public Topic(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getBddId() {
        return bddId;
    }

    public void setBddId(String bddId) {
        this.bddId = bddId;
    }
}
