package bikurim.silverfix.com.bikurim.database;

import android.provider.BaseColumns;

/**
 * Created by David on 12/06/2016.
 */
public abstract class FamiliesTablesContract implements BaseColumns {

    public static final String TABLE_NAME = "families";
    public static final String NAME_COLUMN = "name";
    public static final String VISITORS_COLUMNS = "visitors";
    public static final String DATE_COLUMN = "date";
    public static final String TIME_COLUMN = "time";

    public static final String FAMILIES_COLUMNS[] = {NAME_COLUMN, VISITORS_COLUMNS,
                                                        DATE_COLUMN, TIME_COLUMN};
}
