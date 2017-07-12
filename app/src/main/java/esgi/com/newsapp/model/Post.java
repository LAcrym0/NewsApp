package esgi.com.newsapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Grunt on 11/07/2017.
 */

public class Post {
    @SerializedName("_id")
    private String id;
    private String title;
    private String content;
    private String topic;
    private String date;
    private String author;

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
}
