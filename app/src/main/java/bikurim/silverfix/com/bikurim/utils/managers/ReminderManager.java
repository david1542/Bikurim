package bikurim.silverfix.com.bikurim.utils.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.utils.OnAlarmReceiver;

/**
 * Created by David on 18/06/2016.
 */
public class ReminderManager {

    private Context context;
    private AlarmManager alarmManager;

    private ArrayList<Intent> intents;
    public ReminderManager(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        intents = new ArrayList<Intent>();
    }

    public void setReminder(Long remindId, String name, long when) {
        Intent i = new Intent(context, OnAlarmReceiver.class);
        i.putExtra(Constants.Intent.REMINDER_ID, (long)remindId);
        i.putExtra(Constants.Intent.FAMILY_NAME, name);
        i.setAction(Constants.Intent.WAKE_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, when, pi);

        // Adding the current intent to the
        intents.add(i);
    }

    /* Cancels a specific reminder by creating a pending intent */
    public void cancelReminder(int remindId) {
        PendingIntent pi = PendingIntent.getBroadcast(context, remindId, intents.get(remindId-1), PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
    }

    /* Cancels all reminders that are scheduled */
    public void cancelAll() {
        PendingIntent pi;
        for(Intent i : intents) {
            pi = PendingIntent.getBroadcast(context, intents.indexOf(i), i, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pi);
        }
    }
}
