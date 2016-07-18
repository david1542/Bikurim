package bikurim.silverfix.com.bikurim.utils.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.utils.components.OnAlarmReceiver;
import bikurim.silverfix.com.bikurim.utils.Utils;

/**
 * Created by David on 18/06/2016.
 */
public class ReminderManager {

    private Context context;
    private AlarmManager alarmManager;

    private ArrayList<Integer> idList;

    public ReminderManager(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        idList = new ArrayList<Integer>();
    }

    public void setReminder(Long remindId, String name, long when) {
        Intent i = new Intent(context, OnAlarmReceiver.class);
        i.putExtra(Constants.Intent.REMINDER_ID, (long)remindId);
        i.putExtra(Constants.Intent.FAMILY_NAME, name);
        i.setAction(Constants.Intent.WAKE_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, when, pi);

        int id = Utils.safeLongToInt(remindId);
        idList.add(id);
    }

    /* Cancels a specific reminder by creating a pending intent */
    public void cancelReminder(int remindId) {
        Intent i = new Intent(context, OnAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, remindId, i, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pi);
        pi.cancel();
    }

    /* Cancels all reminders that are scheduled */
    public void cancelAll() {
        Intent i = new Intent(context, OnAlarmReceiver.class);
        PendingIntent pi;
        for(int id : idList) {
            pi = PendingIntent.getBroadcast(context, id, i, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pi);
        }

        // Clear the intent list
        idList.clear();
    }
}
