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

public class Post extends RealmObject  {

    @SerializedName("_id")
    private String id;

    @Exclude
    @Index
    @PrimaryKey
    private String bddId;

    @Required
    @Index
    private String title;

    @Required
    private String content;

    @Index
    private String topic;

    private String date;

    @Index
    private String author;

    @Exclude
    private Boolean synced;



    public Post(){

    }
    public Post(String title, String content, String topic, String date) {
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.date = date;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSynced(Boolean synced){this.synced = synced;}

    public Boolean getSynced(){return synced;}

    public String getBddId() {
        return bddId;
    }

    public void setBddId(String bddId) {
        this.bddId = bddId;
    }
}
