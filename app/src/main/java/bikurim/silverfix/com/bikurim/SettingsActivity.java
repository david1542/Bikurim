package bikurim.silverfix.com.bikurim;

import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import bikurim.silverfix.com.bikurim.fragments.SettingsFragment;
import bikurim.silverfix.com.bikurim.utils.components.AppCompatPreferenceActivity;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private AppCompatDelegate delegate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupActionBar();

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
            onBackPressed();
        return true;
    }

    private void setupActionBar() {
        getLayoutInflater().inflate(R.layout.tool_bar, (ViewGroup)findViewById(android.R.id.content));
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
