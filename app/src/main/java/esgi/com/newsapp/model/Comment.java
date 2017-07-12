package esgi.com.newsapp.model;

/**
 * Created by Grunt on 11/07/2017.
 */

public class Comment {
    private String title;
        private String content;
        private String news;
        private String date;

    public Comment(String title, String content, String news, String date) {
            this.title = title;
            this.content = content;
            this.news = news;
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
}
