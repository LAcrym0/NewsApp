package esgi.com.newsapp.network;

/**
 * Created by Grunt on 28/06/2017.
 */

public interface ApiResult<T> {
    void success(T res);

    void error(int code, String message);
}
