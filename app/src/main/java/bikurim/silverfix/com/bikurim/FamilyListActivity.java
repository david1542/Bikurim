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
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import bikurim.silverfix.com.bikurim.adapters.FamiliesAdapter;
import bikurim.silverfix.com.bikurim.adapters.holders.GenericViewHolder;
import bikurim.silverfix.com.bikurim.database.FamiliesTablesContract;
import bikurim.silverfix.com.bikurim.utils.managers.DatabaseManager;
import bikurim.silverfix.com.bikurim.database.TempTablesContract;
import bikurim.silverfix.com.bikurim.fragments.AddFragment;
import bikurim.silverfix.com.bikurim.models.Family;
import bikurim.silverfix.com.bikurim.utils.Comparators;
import bikurim.silverfix.com.bikurim.utils.managers.ReminderManager;

public class FamilyListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // The Data Set
    private ArrayList<Family> families = new ArrayList<Family>();
    private RecyclerView recyclerView;
    private FamiliesAdapter adapter;
    private SearchView searchView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private AddFragment addDialog;
    private CheckBox checkBox;
    private Toolbar toolbar;
    private AlertDialog confirm;

    private LocalBroadcastReceiver receiver;
    private DatabaseManager dataManager;
    private ReminderManager reminderManager;

    private Comparator<Family> comparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_families);
        // Setting up the tool bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Setting up the main lists and their adapters
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        adapter = new FamiliesAdapter(this, families);
        // Setting up the data manager and the reminder manager
        dataManager = new DatabaseManager(this);

        reminderManager = new ReminderManager(this);
        // In case of crashing, the app loads the temporary data
        loadState();
        /* LocalBroadcastReceiver is used to notify the activity that a task
        has been added by the user
        * */
        receiver = new LocalBroadcastReceiver();
        // Default Animator
        DefaultItemAnimator animator = new DefaultItemAnimator();
        /* Setting up some basic features */
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(FamilyListActivity.this);
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

                                String whereCaluse = FamiliesTablesContract.DATE_COLUMN + "=?";
                                String args[] = new String[] {"" + family.date};
                                Cursor cursor = dataManager.query(FamiliesTablesContract.TABLE_NAME, whereCaluse, args);
                                int entryId = 0;

                                try {
                                    cursor.moveToFirst();
                                    entryId = cursor.getInt(cursor.getColumnIndexOrThrow(FamiliesTablesContract._ID));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                reminderManager.cancelReminder(entryId);
                                String name = family.lastName;

                                adapter.removeData(pos, holder);
                                Toast.makeText(FamilyListActivity.this, "משפחת "+name+" נמחקה מהרשימה", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
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
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Support in hebrew - Sets layout direction Right to Left
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
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
        synchronized (families) {
            dataManager.saveRunningFamilies(families);
        }
    }

    private void loadState() {
        Cursor results = dataManager.getTempData();
        if(results != null) {
            if(results.moveToFirst()) {
                String name;
                int visitorsNum;
                long dateOfFamily, currentMillis, timeLeft;
                do {
                    timeLeft = results.getLong(results.getColumnIndexOrThrow(TempTablesContract.TIME_LEFT_COLUMN));
                    if(timeLeft > 0) {
                        // Extracting the values from the intent bundle
                        name = results.getString(results.getColumnIndexOrThrow(TempTablesContract.NAME_COLUMN));
                        visitorsNum = results.getInt(results.getColumnIndexOrThrow(TempTablesContract.VISITORS_COLUMNS));
                        dateOfFamily = results.getLong(results.getColumnIndexOrThrow(TempTablesContract.ADD_DATE_COLUMN));

                        // Adding the new family to the data set and notify the adapter
                        synchronized (families) {
                            families.add(new Family(name, visitorsNum, System.currentTimeMillis() + timeLeft, timeLeft, dateOfFamily));
                        }
                    }
                } while(results.moveToNext());
                adapter.notifyDataSetChanged();
            }
        }
        results.close();
    }

    private void showAddDialog() {
        AddFragment addFragment = AddFragment.newInstance();
        addFragment.show(getSupportFragmentManager(), Constants.Tags.ADD_TAG);
    }

    private void sortList(Comparator<Family> comparator) {
        this.comparator = comparator;
        Collections.sort(families, this.comparator);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.families_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
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
        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);

        //Closing drawer on item click
        drawerLayout.closeDrawers();

        //Check to see which item was being clicked and perform appropriate action
        switch (item.getItemId()) {
            case R.id.option_archive:
                Intent intent = new Intent(this, ArchiveActivity.class);
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
            case R.id.action_reset:
                reminderManager.cancelAll();
                dataManager.clearDatabase();
                families.clear();
                adapter.clearData();
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "המסד אותחל מחדש", Toast.LENGTH_SHORT).show();
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
        adapter.clearData();
        super.onDestroy();
    }

    private class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.Intent.SAVE_ACTION)) {
                // Extracting the data from the intent object
                String familyName = intent.getStringExtra(Constants.Intent.FAMILY_NAME);
                int visitorsNum = intent.getIntExtra(Constants.Intent.VISITORS_NUMBER, 1);
                boolean isChecked = intent.getBooleanExtra(Constants.Intent.IS_CHECKED, false);
                long time = (isChecked) ? Constants.Intent.EXTRA_TIME : Constants.Intent.DEFAULT_TIME;
                long dateTime = intent.getLongExtra(Constants.Intent.DATE_TIME, 0);

                // Creating a Family object with the extracted data and adds it to the data set
                Family family = new Family(familyName, visitorsNum, System.currentTimeMillis() + time, time, dateTime);
                families.add(family);

                long remindId = dataManager.addFamily(family);

                reminderManager.setReminder(remindId, familyName, System.currentTimeMillis() + time - 60000);

                // Sorting the data set whenever new object is added
                int pos = (int) remindId;
                adapter.notifyItemInserted(pos);
                String message = "משפחת " + familyName + " נוספה בהצלחה!" ;
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
