package bikurim.silverfix.com.bikurim.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David on 12/06/2016.
 * This class is used to simplify the work with the database
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Bikurim.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";

    // The table's creation raw query
    private static final String SQL_CREATE_FAMILIES_ENTRIES =
            "CREATE TABLE " + FamiliesTablesContract.TABLE_NAME + " (" +
                    FamiliesTablesContract._ID + INT_TYPE + " PRIMARY KEY," +
                    FamiliesTablesContract.NAME_COLUMN + TEXT_TYPE + "," +
                    FamiliesTablesContract.VISITORS_COLUMNS + INT_TYPE + "," +
                    FamiliesTablesContract.DATE_COLUMN + INT_TYPE + "," +
                    FamiliesTablesContract.TIME_COLUMN + TEXT_TYPE +
                    ")";

    private static final String SQL_CREATE_TEMP_ENTRIES =
            "CREATE TABLE " + TempTablesContract.TABLE_NAME + " (" +
                    TempTablesContract._ID + " INTEGER PRIMARY KEY, " +
                    TempTablesContract.NAME_COLUMN + TEXT_TYPE + "," +
                    TempTablesContract.VISITORS_COLUMNS + INT_TYPE + "," +
                    TempTablesContract.CURRENT_TIME_COLUMN + INT_TYPE + "," +
                    TempTablesContract.ADD_DATE_COLUMN + TEXT_TYPE + "," +
                    TempTablesContract.TIME_LEFT_COLUMN + INT_TYPE +
                    ")";

    private static final String SQL_DELETE_FAMILIES_ENTRIES =
            "DROP TABLE IF EXISTS " + FamiliesTablesContract.TABLE_NAME;

    private static final String SQL_DELETE_TEMP_ENTRIES =
            "DROP TABLE IF EXISTS " + TempTablesContract.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAMILIES_ENTRIES);
        db.execSQL(SQL_CREATE_TEMP_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FAMILIES_ENTRIES);
        db.execSQL(SQL_DELETE_TEMP_ENTRIES);
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
    * for the archive of the app
    * */

    public long insert(ContentValues values, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(tableName, null, values);
    }

    public void delete(String tableName, String selection, String[] args) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, selection, args);
    }

    public void update(String tableName, ContentValues values, String selection, String[] args) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(tableName, values, selection, args);
    }

    public Cursor query(String tableName, String selection, String args[]) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tableName, null, selection, args, null, null, null);
    }
    public Cursor getEntries(String tableName) {
        String query = "SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void clearTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName);
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + FamiliesTablesContract.TABLE_NAME + " WHERE 1=1");
        db.execSQL("DELETE FROM " + TempTablesContract.TABLE_NAME + " WHERE 1=1");
    }

}
