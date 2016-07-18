package bikurim.silverfix.com.bikurim.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import bikurim.silverfix.com.bikurim.R;

/**
 * Created by David on 15/07/2016.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
