package bikurim.silverfix.com.bikurim.utils.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.database.DatabaseHelper;
import bikurim.silverfix.com.bikurim.database.FamiliesTablesContract;
import bikurim.silverfix.com.bikurim.database.TempTablesContract;
import bikurim.silverfix.com.bikurim.models.Family;

/**
 * Created by דודו on 30/06/2016.
 * A class that is meant to simplify the actions that are performed on the database
 */
public class DatabaseManager {

    DatabaseHelper dbHelper;
    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);

    }

    public Cursor getAllFamilies() {
        return dbHelper.getEntries(FamiliesTablesContract.TABLE_NAME);
    }

    public Cursor getTempData() {
        return dbHelper.getEntries(TempTablesContract.TABLE_NAME);
    }

    public Cursor getLastFamilies() {
        String whereClause = FamiliesTablesContract.DATE_COLUMN + "=?";
        String[] arg = {"(SELECT MAX(" + FamiliesTablesContract.DATE_COLUMN + ") FROM " +
                        FamiliesTablesContract.TABLE_NAME + ")"};
        Cursor cursor = query(FamiliesTablesContract.TABLE_NAME, whereClause, null, null);
        cursor.moveToFirst();

        long newestDate = cursor.getLong(cursor.getColumnIndexOrThrow(FamiliesTablesContract.DATE_COLUMN));
        long limit = newestDate - Constants.Values.LIMIT_TO_DAY;
        whereClause = FamiliesTablesContract.DATE_COLUMN + ">=?";
        String[] args = {"" + limit};

        Cursor results = dbHelper.query(FamiliesTablesContract.TABLE_NAME, whereClause, args, FamiliesTablesContract.DATE_COLUMN + " DESC");

        return results;
    }
    public long addFamily(Family family) {
        ContentValues values = new ContentValues();
        values.put(FamiliesTablesContract.NAME_COLUMN, family.name);
        values.put(FamiliesTablesContract.VISITORS_COLUMNS, family.visitorsNum);
        values.put(FamiliesTablesContract.LENGTH_COLUMN, family.visitLength);
        values.put(FamiliesTablesContract.DATE_COLUMN, family.date);
        return dbHelper.insert(values, FamiliesTablesContract.TABLE_NAME);
    }

    public Cursor query(String tableName, String selection, String args[], String orderBy) {
        return dbHelper.query(tableName, selection, args, orderBy);
    }

    /* Saves all the current running families into the Temp table*/
    public void saveRunningFamilies(ArrayList<Family> families) {
        ContentValues values = new ContentValues();
        for(Family family : families) {
            values.put(TempTablesContract.NAME_COLUMN, family.name);
            values.put(TempTablesContract.VISITORS_COLUMNS, family.visitorsNum);
            values.put(TempTablesContract.CURRENT_TIME_COLUMN, System.currentTimeMillis());
            values.put(TempTablesContract.ADD_DATE_COLUMN, family.date);
            values.put(TempTablesContract.TIME_LEFT_COLUMN, family.timeLeft);
            values.put(TempTablesContract.LENGTH_COLUMN, family.visitLength);
            dbHelper.insert(values, TempTablesContract.TABLE_NAME);
        }
    }

    /* Clears Temp table*/

    public void clearTempData() {
        dbHelper.clearTable(TempTablesContract.TABLE_NAME);
    }

    /* Clears Families table*/

    public void clearFamiliesData() {
        dbHelper.clearTable(FamiliesTablesContract.TABLE_NAME);
    }

    public void upgradeDatabase(int oldVersion, int newVersion) {
        dbHelper.onUpgrade(dbHelper.getReadableDatabase(), oldVersion, newVersion);
    }

    public void clearDatabase() {
        dbHelper.clearDatabase();
    }
}
