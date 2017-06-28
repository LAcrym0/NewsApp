package esgi.com.newsapp.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Grunt on 28/06/2017.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Exclude {
    /**
     * If {@code true}, the field marked with this annotation is not written out in the JSON while
     * serializing. Defaults to {@code true}.
     */
    boolean serialize() default true;

    /**
     * If {@code true}, the field marked with this annotation is not deserialized from the JSON.
     * Defaults to {@code true}.
     */
    boolean deserialize() default true;
}
