package bikurim.silverfix.com.bikurim.utils;

/**
 * Created by דודו on 01/07/2016.
 */
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public abstract class WakeReminderIntentService extends IntentService {
    abstract void doReminderWork(Intent intent);

    public static final String LOCK_NAME_STATIC="bikurim.silverfix.com.bikurim.Static";
    private static PowerManager.WakeLock lockStatic=null;

    public static void acquireStaticLock(Context context) {
        getLock(context).acquire();
    }

    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (lockStatic==null) {
            PowerManager mgr=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
            lockStatic=mgr.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }
        return(lockStatic);
    }

    public WakeReminderIntentService(String name) {
        super(name);
    }

    @Override
    final protected void onHandleIntent(Intent intent) {
        try {
            doReminderWork(intent);
        }
        finally {
            getLock(this).release();
        }
    }
}
