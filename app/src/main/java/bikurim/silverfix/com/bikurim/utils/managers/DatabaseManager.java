package bikurim.silverfix.com.bikurim.utils.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

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

    public Cursor getFamilies() {
        return dbHelper.getEntries(FamiliesTablesContract.TABLE_NAME);
    }

    public Cursor getTempData() {
        return dbHelper.getEntries(TempTablesContract.TABLE_NAME);
    }

    public long addFamily(Family family) {
        ContentValues values = new ContentValues();
        values.put(FamiliesTablesContract.NAME_COLUMN, family.lastName);
        values.put(FamiliesTablesContract.VISITORS_COLUMNS, family.visitorsNum);
        values.put(FamiliesTablesContract.TIME_COLUMN, family.whenInMillis);
        values.put(FamiliesTablesContract.DATE_COLUMN, family.date);
        return dbHelper.insert(values, FamiliesTablesContract.TABLE_NAME);
    }

    public Cursor query(String tableName, String selection, String args[]) {
        return dbHelper.query(tableName, selection, args);
    }

    /* Saves all the current running families into the Temp table*/
    public void saveRunningFamilies(ArrayList<Family> families) {
        ContentValues values = new ContentValues();
        for(Family family : families) {
            values.put(TempTablesContract.NAME_COLUMN, family.lastName);
            values.put(TempTablesContract.VISITORS_COLUMNS, family.visitorsNum);
            values.put(TempTablesContract.CURRENT_TIME_COLUMN, System.currentTimeMillis());
            values.put(TempTablesContract.ADD_DATE_COLUMN, family.date);
            values.put(TempTablesContract.TIME_LEFT_COLUMN, family.timeLeft);
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
