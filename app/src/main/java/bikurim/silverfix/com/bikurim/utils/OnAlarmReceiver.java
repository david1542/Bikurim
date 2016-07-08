package bikurim.silverfix.com.bikurim.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.database.TempTablesContract;
import bikurim.silverfix.com.bikurim.utils.ReminderService;
import bikurim.silverfix.com.bikurim.utils.WakeReminderIntentService;

/**
 * Created by דודו on 18/06/2016.
 */
public class OnAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long rowid = intent.getExtras().getLong(TempTablesContract._ID);
        String name = intent.getStringExtra(Constants.Intent.FAMILY_NAME);

        WakeReminderIntentService.acquireStaticLock(context);

        Intent i = new Intent(context, ReminderService.class);
        i.putExtra(TempTablesContract._ID, rowid);
        i.putExtra(Constants.Intent.FAMILY_NAME, name);
        context.startService(i);
    }
}
