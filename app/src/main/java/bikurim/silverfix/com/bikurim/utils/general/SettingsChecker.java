package bikurim.silverfix.com.bikurim.utils.general;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import bikurim.silverfix.com.bikurim.Constants;

/**
 * Created by David on 15/07/2016.
 *
 * An helper class for retrieving the settings values from the SharedPreferences file
 */
public class SettingsChecker {

    private Context context;
    private SharedPreferences sp;
    public SettingsChecker(Context context) {
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isSoundOn() {
        return sp.getBoolean(Constants.SharedPreferences.SOUND_KEY, true);
    }

    public boolean isVibrateOn() {
        return sp.getBoolean(Constants.SharedPreferences.VIBRATE_KEY, true);
    }
}
