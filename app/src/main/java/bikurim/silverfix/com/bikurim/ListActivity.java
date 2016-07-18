package bikurim.silverfix.com.bikurim;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import bikurim.silverfix.com.bikurim.adapters.FamiliesAdapter;
import bikurim.silverfix.com.bikurim.adapters.holders.GenericViewHolder;
import bikurim.silverfix.com.bikurim.database.FamiliesTablesContract;
import bikurim.silverfix.com.bikurim.utils.general.FamilyRecyclerView;
import bikurim.silverfix.com.bikurim.utils.managers.MediaPlayerManager;
import bikurim.silverfix.com.bikurim.utils.Utils;
import bikurim.silverfix.com.bikurim.utils.managers.DatabaseManager;
import bikurim.silverfix.com.bikurim.database.TempTablesContract;
import bikurim.silverfix.com.bikurim.fragments.AddFragment;
import bikurim.silverfix.com.bikurim.models.Family;
import bikurim.silverfix.com.bikurim.utils.general.Comparators;
import bikurim.silverfix.com.bikurim.utils.managers.ReminderManager;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // The Data Set
    private ArrayList<Family> families = new ArrayList<>();
    private FamiliesAdapter adapter;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private ListBroadcastReceiver receiver;
    private DatabaseManager dataManager;
    private ReminderManager reminderManager;

    private Thread.UncaughtExceptionHandler androidDefaultUEH;

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            androidDefaultUEH.uncaughtException(thread, ex);

            // In a case of crashing, the application saves the current running families
            saveState();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_families);

        // Setting up the default uncaught exception handler, for catching exceptions and backup the current data
        androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);

        // Setting up the tool bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting up the main lists and their adapters
        FamilyRecyclerView recyclerView = (FamilyRecyclerView) findViewById(R.id.rv);

        // Setting up the data manager and the reminder manager
        dataManager = new DatabaseManager(this);
        reminderManager = new ReminderManager(this);
        // ListBroadcastReceiver is used to notify the activity that a task has been added by the user
        receiver = new ListBroadcastReceiver();

        // Default Animator
        DefaultItemAnimator animator = new DefaultItemAnimator();

        /* Setting up some basic features */
        recyclerView.setItemAnimator(animator);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

        View emptyView = findViewById(R.id.empty_container);
        Utils.setFadeAnimation(emptyView);
        recyclerView.setEmptyView(emptyView);

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                // if the sound option is on, play a swoosh sound
                MediaPlayerManager.playSound(MediaPlayerManager.Sound.SWIPE);

                final int pos = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setMessage(R.string.confirmation_message)
                        .setPositiveButton(R.string.proceed_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Family family = families.get(pos);
                                GenericViewHolder holder = null;
                                try {
                                    holder = (GenericViewHolder) viewHolder;
                                } catch (ClassCastException e) {
                                    e.printStackTrace();
                                }

                                String whereClause = FamiliesTablesContract.DATE_COLUMN + "=?";
                                String args[] = new String[]{"" + family.date};
                                Cursor cursor = dataManager.query(FamiliesTablesContract.TABLE_NAME, whereClause, args, null);
                                int entryId = 0;

                                try {
                                    cursor.moveToFirst();
                                    entryId = cursor.getInt(cursor.getColumnIndexOrThrow(FamiliesTablesContract._ID));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                reminderManager.cancelReminder(entryId);
                                String name = Utils.getLastName(family.name);

                                adapter.removeData(pos, holder);
                                dialog.dismiss();

                                Toast.makeText(ListActivity.this, "משפחת "+name+" נמחקה מהרשימה", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton(R.string.cancel_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyItemChanged(pos);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        adapter = new FamiliesAdapter(this, families, touchHelper);
        recyclerView.setAdapter(adapter);
        recyclerView.setVerticalScrollBarEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toolbar.setTitle(R.string.app_name);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbar.setTitle(R.string.drawer_open);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Support in hebrew - Sets layout direction Right to Left
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // In case of crashing, the app loads the temporary data
        loadState();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver,
                new IntentFilter(Constants.Intent.SAVE_ACTION));

        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onPause();
    }

    private void saveState() {
        dataManager.clearTempData();
        dataManager.saveRunningFamilies(families);
    }

    private void loadState() {
        Cursor results = dataManager.getTempData();
        if (results != null) {
            if (results.moveToFirst()) {
                long stopTime, currentMillis, timeLeft;
                do {
                    stopTime = results.getLong(results.getColumnIndexOrThrow(TempTablesContract.CURRENT_TIME_COLUMN));
                    currentMillis = System.currentTimeMillis();

                    timeLeft = results.getLong(results.getColumnIndexOrThrow(TempTablesContract.TIME_LEFT_COLUMN));

                    // Checks if the elapsed time since saveState() is less than the actual time
                    // that is left for the family
                    if (currentMillis - stopTime < timeLeft) {
                        // Extracting the values from the intent bundle
                        String name = results.getString(results.getColumnIndexOrThrow(TempTablesContract.NAME_COLUMN));
                        int visitorsNum = results.getInt(results.getColumnIndexOrThrow(TempTablesContract.VISITORS_COLUMNS));
                        long dateOfFamily = results.getLong(results.getColumnIndexOrThrow(TempTablesContract.ADD_DATE_COLUMN));
                        long visitLength = results.getLong(results.getColumnIndexOrThrow(TempTablesContract.LENGTH_COLUMN));

                        timeLeft -= currentMillis - stopTime;

                        // Adding the new family to the data set
                        families.add(new Family(name, visitorsNum, timeLeft, dateOfFamily, visitLength));
                    }
                } while (results.moveToNext());
                adapter.notifyDataSetChanged();
            }
            results.close();
        }
    }

    private void showAddDialog() {
        AddFragment addFragment = AddFragment.newInstance();
        addFragment.show(getSupportFragmentManager(), Constants.Tags.ADD_TAG);
    }

    private void sortList(Comparator<Family> comparator) {
        Collections.sort(families, comparator);
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "הרשימה מוינה בהצלחה", Toast.LENGTH_SHORT).show();
    }

    private void clearData() {
        families.clear();
        adapter.clearData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.families_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true; // handled
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //Closing drawer on item click
        drawerLayout.closeDrawers();

        Intent intent;
        //Check to see which item was being clicked and perform appropriate action
        switch (item.getItemId()) {
            case R.id.option_archive:
                intent = new Intent(this, ArchiveActivity.class);
                startActivity(intent);
                break;

            case R.id.option_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.option_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_search:
                return true;
            case R.id.action_sort_time:
                sortList(Comparators.TIME_COMPARATOR);
                return true;
            case R.id.action_sort_names:
                sortList(Comparators.NAME_COMPARATOR);
                return true;
            case R.id.action_sort_visitors:
                sortList(Comparators.VISITORS_COMPARATOR);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        saveState();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        saveState();
        clearData();
        super.onDestroy();
    }

    private class ListBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.Intent.SAVE_ACTION)) {
                // Extracting the data from the intent object
                String name = intent.getStringExtra(Constants.Intent.FAMILY_NAME);

                int visitorsNum = intent.getIntExtra(Constants.Intent.VISITORS_NUMBER, 1);
                boolean isChecked = intent.getBooleanExtra(Constants.Intent.IS_CHECKED, false);
                long time = (isChecked) ? Constants.Values.EXTRA_TIME : Constants.Values.DEFAULT_TIME;
                long dateTime = intent.getLongExtra(Constants.Intent.DATE_TIME, 0);

                // Creating a Family object with the extracted data and adds it to the data set
                Family family = new Family(name, visitorsNum, time, dateTime, time);
                families.add(family);

                long remindId = dataManager.addFamily(family);

                reminderManager.setReminder(remindId, name, System.currentTimeMillis() + time - 60000);

                // Sorting the data set whenever new object is added
                int pos = (int) remindId;
                adapter.notifyItemInserted(pos);

                // if the sound option is on, play a swoosh sound
                MediaPlayerManager.playSound(MediaPlayerManager.Sound.ADD);

                String message = "משפחת " + Utils.getLastName(name) + " נוספה בהצלחה!";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
