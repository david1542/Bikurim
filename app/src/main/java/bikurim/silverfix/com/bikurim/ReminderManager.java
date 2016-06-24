package bikurim.silverfix.com.bikurim;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import bikurim.silverfix.com.bikurim.database.FamiliesTablesContract;

/**
 * Created by דודו on 18/06/2016.
 */
public class ReminderManager {

    private Context context;
    private AlarmManager alarmManager;
    public ReminderManager(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setReminder(Long taskId, Calendar when) {

        Intent i = new Intent(context, OnAlarmReceiver.class);
        i.putExtra(FamiliesTablesContract._ID, (long)taskId);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
    }
}
