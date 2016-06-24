package bikurim.silverfix.com.bikurim;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;

import java.util.ArrayList;

import bikurim.silverfix.com.bikurim.adapters.RecyclerViewAdapter;
import bikurim.silverfix.com.bikurim.database.FamiliesTablesContract;
import bikurim.silverfix.com.bikurim.database.FamilyDatabaseHelper;
import bikurim.silverfix.com.bikurim.fragments.AddFragment;
import bikurim.silverfix.com.bikurim.items.FamilyItem;

public class FamilyListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Fragments Tagד
    private final String ADD_TAG = "AddDialog";
    private final String DRAWER_TAG = "AddDialog";
    // The Data Set
    private ArrayList<FamilyItem> families = new ArrayList<FamilyItem>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private SearchView searchView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private AddFragment addDialog;
    private CheckBox checkBox;
    private Toolbar toolbar;
    private FragmentManager fm;

    private LocalBroadcastReceiver receiver;

    private FamilyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_families);
        // Setting up the tool bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fillList();
        // Setting up the main lists and their adapters
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        adapter = new RecyclerViewAdapter(families);
        // Setting up the database helper
        dbHelper = new FamilyDatabaseHelper(this);
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

        // A reference to the FragmentManager is created
        fm = getSupportFragmentManager();

        // Initializing necessary fragments
        addDialog = new AddFragment();

        // A basic functionallity is given to the floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fm.findFragmentByTag(ADD_TAG) == null) {
                    // Adding the new fragment to the activity
                    addFragment(addDialog, ADD_TAG);
                    return;
                }
                addDialog.show(fm, ADD_TAG);
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

    private void fillList() {
        long time = System.currentTimeMillis() + 1800000;
        families.add(new FamilyItem("זוהר", time + 6000));
        families.add(new FamilyItem("אוחיון", time + 2000));
        families.add(new FamilyItem("לסרי", time + 7000));
        families.add(new FamilyItem("זמבוזק", time + 1000));
        families.add(new FamilyItem("כהן", time - 4000));
        families.add(new FamilyItem("טפרה", time - 10000));
        families.add(new FamilyItem("קצורה", time + 9000));
        families.add(new FamilyItem("מנחם", time + 1000));
        families.add(new FamilyItem("חשמונאי", time + 12000));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void addFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction =
                fm.beginTransaction();
        fragmentTransaction.add(fragment, tag);
        fragmentTransaction.commit();
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

        }
        return true;
    }

    private class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("SAVE_STATE")) {
                String familyName = intent.getStringExtra(Constants.Intent.FAMILY_NAME);
                boolean isChecked = intent.getBooleanExtra(Constants.Intent.IS_CHECKED, false);
                long time = (isChecked) ? Constants.Intent.EXTRA_TIME : Constants.Intent.DEFAULT_TIME;
                String dateTime = intent.getStringExtra(Constants.Intent.DATE_TIME);
                families.add(new FamilyItem(familyName, time));
                ContentValues values = new ContentValues();
                values.put(FamiliesTablesContract.NAME_COLUMN, familyName);
                values.put(FamiliesTablesContract.DATE_COLUMN, dateTime);
                values.put(FamiliesTablesContract.TIME_COLUMN, time);
                dbHelper.insert(values);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
