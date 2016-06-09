package bikurim.silverfix.com.bikurim;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import bikurim.silverfix.com.bikurim.adapters.DividerItemDecoration;
import bikurim.silverfix.com.bikurim.adapters.MenuDrawerAdapter;
import bikurim.silverfix.com.bikurim.adapters.RecyclerViewAdapter;
import bikurim.silverfix.com.bikurim.items.AddFragment;
import bikurim.silverfix.com.bikurim.items.FamilyItem;

public class MainActivity extends AppCompatActivity {

    private final String ADD_DIALOG_TAG = "AddDialog";
    private ArrayList<FamilyItem> families = new ArrayList<FamilyItem>();
    private ArrayList<bikurim.silverfix.com.bikurim.items.MenuItem> items = new ArrayList<bikurim.silverfix.com.bikurim.items.MenuItem>();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView recyclerView, menuView;
    private RecyclerViewAdapter adapter;
    private MenuDrawerAdapter navAdapter;
    private SearchView searchView;
    private AddFragment addDialog;
    private CheckBox checkBox;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_families);
        // Setting up the tool bar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Setting up the main lists and their adapters
        fillList();
        createMenuItems();
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        menuView = (RecyclerView) findViewById(R.id.right_drawer);
        adapter = new RecyclerViewAdapter(families);
        navAdapter = new MenuDrawerAdapter(items);
        // Default Animator
        DefaultItemAnimator animator = new DefaultItemAnimator();
        /* Setting up some basic features */
        recyclerView.setItemAnimator(animator);
        menuView.setItemAnimator(animator);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuView.setLayoutManager(new LinearLayoutManager(this));
        menuView.setAdapter(navAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this);
        menuView.addItemDecoration(itemDecoration);
        recyclerView.setNestedScrollingEnabled(false);
        Log.d("Menu Items Count:", ""+navAdapter.getItemCount());
        // Setting up the navigation drawer side-menu with DrawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Drawer toggle is now instantiated in order to control the state of the navigation drawer
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer is in closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.app_name);
                drawerLayout.setVisibility(View.GONE);
            }

            /** Called when a drawer is in open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.drawer_name);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        fm = getSupportFragmentManager();
        addDialog = new AddFragment();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog.show(fm, ADD_DIALOG_TAG);
            }
        });
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    private void fillList() {
        families.add(new FamilyItem("Ohayon", "12:36"));
        families.add(new FamilyItem("Zohar", "14:23"));
        families.add(new FamilyItem("Lasry", "15:55"));
        families.add(new FamilyItem("Zambuzak", "17:45"));
        families.add(new FamilyItem("Cohen", "11:33"));
        families.add(new FamilyItem("Tapera", "09:25"));
        families.add(new FamilyItem("Ochuya", "09:25"));
        families.add(new FamilyItem("Konichiwua", "09:25"));
        families.add(new FamilyItem("Kontaktiwa", "09:25"));
    }

    private void createMenuItems() {
        items.add(new bikurim.silverfix.com.bikurim.items.MenuItem(R.drawable.about, "עזרה"));
        items.add(new bikurim.silverfix.com.bikurim.items.MenuItem(R.drawable.settings, "הגדרות"));
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_families, menu);
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            drawerLayout.setVisibility(View.VISIBLE);
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
