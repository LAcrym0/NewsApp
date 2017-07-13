package esgi.com.newsapp.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Grunt on 11/07/2017.
 */

public class Topic extends RealmObject {


    @SerializedName("_id")
    @Index
    @PrimaryKey
    private String id;


    private String author;

    @Required
    @Index
    private String title;

    @Required
    private String content;

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
}
