package bikurim.silverfix.com.bikurim.utils.managers;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.adapters.holders.FamilyViewHolder;
import bikurim.silverfix.com.bikurim.utils.interfaces.EventListener;
import bikurim.silverfix.com.bikurim.utils.Utils;

/**
 * Created by David on 07/07/2016.
 * @author David
 * Inspired by MiguelLavigne
 *
 * A custom CountDownTimer class, which takes care of all the running timers
 */
public class CountDownManager {

    private final long interval;
    private long base;

    // Holds references for all the visible text views
    private ArrayList<FamilyViewHolder> holders;

    private EventListener listener;

    public CountDownManager(long interval, EventListener listener) {
        this.listener = listener;
        this.interval = interval;
        holders = new ArrayList<>();
    }

    public void start() {
        base = System.currentTimeMillis();
        handler.sendMessage(handler.obtainMessage(MSG));
    }

    public void stop() {
        handler.removeMessages(MSG);
    }

    public void reset() {
        synchronized (this) {
            base = System.currentTimeMillis();
        }
    }

    public void addHolder(FamilyViewHolder holder) {
        synchronized (holders) {
            holders.add(holder);
        }
    }

    public void removeHolder(FamilyViewHolder holder) {
        synchronized (holders) {
            holders.remove(holder);
        }
    }

    private void onTick(long elapsedTime) {
       try {
           if(!holders.isEmpty()) {
               long timeLeft;
               for (FamilyViewHolder holder : holders) {
                   timeLeft =  holder.family.whenInMillis() - base - elapsedTime;
                   if(timeLeft > 0) {
                       if(timeLeft <= Constants.Values.ALERT_TIME && !holder.isStrokeChanged) {
                           listener.onLessThanMinute(holder);
                       }
                       holder.family.timeLeft = timeLeft;
                       holder.timeLeft.setText(Utils.updateFormatTime(timeLeft));
                   } else {
                       listener.onFinish(holder);
                       removeHolder(holder);
                   }
               }
           }
       } catch (ConcurrentModificationException e) {
            Log.d("Concurrent Error", "App will shut down now");
            e.printStackTrace();
       }
    }

    public void clear() {
        synchronized (holders) {
            if(!holders.isEmpty()) {
                for(FamilyViewHolder holder : holders)
                    holder.reset();
            }
            holders.clear();
        }
    }

    private static final int MSG = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized (CountDownManager.this) {
                long elapsedTime = System.currentTimeMillis() - base;
                onTick(elapsedTime);
                sendMessageDelayed(obtainMessage(MSG), interval);
            }
        }
    };
}
