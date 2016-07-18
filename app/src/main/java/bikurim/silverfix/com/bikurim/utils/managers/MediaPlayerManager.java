package bikurim.silverfix.com.bikurim.utils.managers;

import android.content.Context;
import android.media.MediaPlayer;

import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.utils.general.SettingsChecker;

/**
 * Created by David on 08/07/2016.
 * A utility class which takes care of all the sound playing in the app
 *
 * @author David Lasry
 */
public class MediaPlayerManager {

    private static MediaPlayer add, alert, end, error, swipe;

    private static SettingsChecker settingsChecker;

    public static void initiate(Context context) {
        settingsChecker = new SettingsChecker(context);

        add = MediaPlayer.create(context, R.raw.add);
        alert = MediaPlayer.create(context, R.raw.alert);
        end = MediaPlayer.create(context, R.raw.end);
        swipe = MediaPlayer.create(context, R.raw.swipe);
        error = MediaPlayer.create(context, R.raw.error);
    }

    public synchronized static void playSound(Sound type) {
        if(settingsChecker.isSoundOn()) {
            switch (type) {
                case ADD:
                    add.start();
                    break;
                case ALERT:
                    alert.start();
                    break;
                case END:
                    end.start();
                    break;
                case SWIPE:
                    swipe.start();
                    break;
                case ERROR:
                    error.start();
                    break;
            }
        }
    }

    public static void clear() {
        settingsChecker = null;
        add.release();
        alert.release();
        end.release();
        swipe.release();
    }

    /* Sound enum. Represents the different types of sounds in the application */
    public enum Sound {
        ADD,
        ALERT,
        ERROR,
        END,
        SWIPE
    }
}
