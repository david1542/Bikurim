package bikurim.silverfix.com.bikurim.interfaces;

import android.content.res.Configuration;
import android.view.MenuItem;

/**
 * Created by David on 10/06/2016.
 */
public interface DrawerFragmentListener {
    void onBackClick();

    void syncState();

    boolean onOptionsItemSelected(MenuItem item);

    void onConfigurationChanged(Configuration newConfig);
}
