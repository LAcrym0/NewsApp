package esgi.com.newsapp.network;

/**
 * Created by Grunt on 28/06/2017.
 */

/**
 * The interface that is used as a callback by retrofit
 * @param <T> the return value class, defined later in the method making the call
 */
public interface ApiResult<T> {
    void success(T res);

    void error(int code, String message);
}
