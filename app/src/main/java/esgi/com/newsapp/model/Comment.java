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

public class Comment extends RealmObject{


    @SerializedName("_id")
    @PrimaryKey
    @Index
    private String id;

    @Index
    private String title;

    @Required
    private String content;

    @Index
    private String news;
    private String date;
    private String author;

    @Exclude
    private Boolean synced;

    public Comment(String title, String content, String news, String date) {
            this.title = title;
            this.content = content;
            this.news = news;
            this.date = date;
        }

    public  Comment(){}


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getSynced() {
        return synced;
    }

    public void setSynced(Boolean synced) {
        this.synced = synced;
    }

    public String getAuthor() {
        return author;
    }
}

