package bikurim.silverfix.com.bikurim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import bikurim.silverfix.com.bikurim.adapters.ArchiveAdapter;
import bikurim.silverfix.com.bikurim.database.FamiliesTablesContract;
import bikurim.silverfix.com.bikurim.fragments.DateFragment;
import bikurim.silverfix.com.bikurim.models.Family;
import bikurim.silverfix.com.bikurim.utils.general.Comparators;
import bikurim.silverfix.com.bikurim.utils.Utils;
import bikurim.silverfix.com.bikurim.utils.managers.DatabaseManager;
import bikurim.silverfix.com.bikurim.utils.managers.MediaPlayerManager;

public class ArchiveActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private DatabaseManager dbManager;
    private RecyclerView recyclerView;
    private ArchiveAdapter adapter;

    private ArrayList<Family> families = new ArrayList<Family>();

    private ArchiveBroadcastReceiver receiver;

    private Comparator<Family> comparator;

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

        receiver = new ArchiveBroadcastReceiver();
        dbManager = new DatabaseManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.archive_list);
        recyclerView.setClickable(false);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        /* Setting up some basic features */
        recyclerView.setItemAnimator(animator);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

        // Setting up the adapter
        families = Utils.toArrayList(dbManager.getAllFamilies());
        adapter = new ArchiveAdapter(this, families);

        recyclerView.setAdapter(adapter);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    private void showDatePicker() {
        DateFragment datePicker = DateFragment.newInstance();
        datePicker.show(getSupportFragmentManager(), Constants.Tags.DATE_TAG);
    }

    private void sortList(Comparator<Family> comparator) {
        this.comparator = comparator;
        Collections.sort(families, this.comparator);
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "הרשימה מוינה בהצלחה", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onResume() {
        families.clear();
        families.addAll(Utils.toArrayList(dbManager.getAllFamilies()));
        adapter.notifyDataSetChanged();

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter(Constants.Intent.SORT_ACTION));
        super.onResume();
    }

    @Override
    protected void onStop() {
        families.clear();
        adapter.clearData();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
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
            case R.id.action_sort_date:
                sortList(Comparators.DATE_COMPARATOR);
                return true;
            case R.id.action_sort_names:
                sortList(Comparators.NAME_COMPARATOR);
                return true;
            case R.id.action_sort_visitors:
                sortList(Comparators.VISITORS_COMPARATOR);
                return true;
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
                Cursor cursor = dbManager.query(FamiliesTablesContract.TABLE_NAME, whereClause, args, FamiliesTablesContract.DATE_COLUMN + " DESC");

                if(cursor.getCount() > 0) {
                    families.clear();
                    families.addAll(Utils.toArrayList(cursor));
                    adapter.notifyDataSetChanged();
                } else {
                    MediaPlayerManager.playSound(MediaPlayerManager.Sound.ERROR);
                    Toast.makeText(ArchiveActivity.this, "לא נמצאו תוצאות בתאריך שצוין", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
