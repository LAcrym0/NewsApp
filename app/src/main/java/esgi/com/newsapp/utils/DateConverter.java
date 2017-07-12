package esgi.com.newsapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Grunt on 12/07/2017.
 */

public class DateConverter {
    public static String getCurrentFormattedDate() {
        SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return ISO8601DATEFORMAT.format(Calendar.getInstance().getTime()) + ".000Z";
    }
}
