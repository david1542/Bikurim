package bikurim.silverfix.com.bikurim.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David on 12/06/2016.
 * This class is used to simplify the work with the database
 */
public class FamilyDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Bikurim.db";
    private static final String TEXT_TYPE = " TEXT";

    // The table's creation raw query
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FamiliesTablesContract.TABLE_NAME + " (" +
                    FamiliesTablesContract._ID + " INTEGER PRIMARY KEY," +
                    FamiliesTablesContract.NAME_COLUMN + TEXT_TYPE + "," +
                    FamiliesTablesContract.DATE_COLUMN + TEXT_TYPE + "," +
                    FamiliesTablesContract.TIME_COLUMN + TEXT_TYPE + "," +
                    ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FamiliesTablesContract.TABLE_NAME;


    public FamilyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /*
    * Gets the current number of records in the database
    * */

    public int getNumRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int rows = (int) DatabaseUtils.queryNumEntries(db, FamiliesTablesContract.TABLE_NAME);
        return rows;
    }

    /*
    * The following are some core functions like insert, delete and update
    * */

    public void insert(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(FamiliesTablesContract.TABLE_NAME, null, values);
    }

    public void delete(String selection, String[] args) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FamiliesTablesContract.TABLE_NAME, selection, args);
    }

    public void update(ContentValues values, String selection, String[] args) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(FamiliesTablesContract.TABLE_NAME, values, selection, args);
    }

}
