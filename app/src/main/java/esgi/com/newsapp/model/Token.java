package esgi.com.newsapp.model;

/**
 * Created by Grunt on 28/06/2017.
 */

public class Token {
    private String value;

    Token(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
