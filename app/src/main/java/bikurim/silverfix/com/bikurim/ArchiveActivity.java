package bikurim.silverfix.com.bikurim;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;

import bikurim.silverfix.com.bikurim.adapters.ArchiveAdapter;
import bikurim.silverfix.com.bikurim.database.FamiliesTablesContract;
import bikurim.silverfix.com.bikurim.database.DatabaseHelper;
import bikurim.silverfix.com.bikurim.fragments.DateFragment;

public class ArchiveActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private DatabaseHelper dbHelper;
    private ListView listView;
    private ArchiveAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        // Setting up the tool bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(this);

        listView = (ListView) findViewById(R.id.archive_list);


        // Setting up the adapter
        Cursor cursor = dbHelper.getEntries(FamiliesTablesContract.TABLE_NAME);
        adapter = new ArchiveAdapter(this, cursor);

        listView.setAdapter(adapter);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    private void showDatePicker() {
        DateFragment datePicker = DateFragment.newInstance();
        datePicker.show(getSupportFragmentManager(), Constants.Tags.DATE_TAG);
    }

    @Override
    protected void onResume() {
        Cursor cursor = dbHelper.getEntries(FamiliesTablesContract.TABLE_NAME);
        adapter.swapCursor(cursor);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.archive_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_date:
                showDatePicker();
                break;
        }
        return true;
    }

    private class ArchiveBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.Intent.SORT_ACTION)) {
                long time = intent.getLongExtra(Constants.Intent.ARCHIVE_DATE, 0);
                long limit = time + Constants.Values.LIMIT_TO_DAY;

                String whereClause = FamiliesTablesContract.DATE_COLUMN + " >= ? AND " + FamiliesTablesContract.DATE_COLUMN + " <= ?";
                String args[] = {"" + time, "" + limit};
                Cursor cursor = dbHelper.query(FamiliesTablesContract.TABLE_NAME, whereClause, args);
                adapter.swapCursor(cursor);
                Log.d("Cursor rows: ", "" + cursor.getCount());
            }
        }
    }
}
