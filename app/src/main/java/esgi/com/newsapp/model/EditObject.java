package esgi.com.newsapp.model;

/**
 * Created by Grunt on 16/07/2017.
 */

public class EditObject {
    private String title;

    private String content;

    public EditObject(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
