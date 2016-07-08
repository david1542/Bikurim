package bikurim.silverfix.com.bikurim.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.FamilyListActivity;
import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.database.TempTablesContract;

/**
 * Created by David on 01/07/2016.
 */
public class ReminderService extends WakeReminderIntentService{
    public ReminderService() {
        super(Constants.Names.REMINDER_SERVICE);
    }
    @Override
    void doReminderWork(Intent intent) {
        // Creates an intent that delivers the user to the FamilyListActivity
        long rowId = intent.getExtras().getLong(TempTablesContract._ID);
        String name = intent.getStringExtra(Constants.Intent.FAMILY_NAME);
        Intent notificationIntent = new Intent(this, FamilyListActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        // Sets the notification behavior
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.reminder)
                .setContentTitle("הזמן עומד להגמר!")
                .setContentText("הזמן עומד להגמר למשפחת " + name)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pi);
        NotificationManager mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Issues the notification and delivers it to the Notification Manager
        int id = (int) rowId;
        mgr.notify(id, builder.build());
    }
}
