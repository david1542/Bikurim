package bikurim.silverfix.com.bikurim.database;

import android.provider.BaseColumns;

/**
 * Created by דודו on 26/06/2016.
 */
public abstract class TempTablesContract implements BaseColumns{

    public static final String TABLE_NAME = "temp";
    public static final String NAME_COLUMN = "name";
    public static final String VISITORS_COLUMNS = "visitors";
    public static final String CURRENT_TIME_COLUMN = "current_time";
    public static final String ADD_DATE_COLUMN = "family_date";
    public static final String TIME_LEFT_COLUMN = "time_left";

    public TempTablesContract() {

    }
}
