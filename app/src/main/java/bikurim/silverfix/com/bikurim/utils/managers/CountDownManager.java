package bikurim.silverfix.com.bikurim.utils.managers;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.adapters.holders.FamilyViewHolder;
import bikurim.silverfix.com.bikurim.utils.interfaces.TimerEventListener;
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
    private ArrayList<FamilyViewHolder> holders, holdersToRemove;

    private TimerEventListener listener;

    public CountDownManager(long interval, TimerEventListener listener) {
        this.listener = listener;
        this.interval = interval;
        holders = new ArrayList<>();
        holdersToRemove = new ArrayList<>();
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
            if(!holders.contains(holder))
                holders.add(holder);
        }
    }

    public void cancelTimer(FamilyViewHolder holder) {
        synchronized (holders) {
            holders.remove(holder);
        }
    }

    public void onTick(long elapsedTime) {
        long timeLeft, lengthOfVisit;
        for (FamilyViewHolder holder : holders) {
            lengthOfVisit = holder.family.whenInMillis - base;
            timeLeft =  lengthOfVisit - elapsedTime;
            if(timeLeft > 0) {
                if(timeLeft <= Constants.Values.ALERT_TIME && !holder.isBackgroundChanged) {
                    listener.onLessThanMinute(holder);
                }
                holder.family.timeLeft = timeLeft;
                holder.timeLeft.setText(Utils.updateFormatTime(timeLeft));
            } else {
                listener.onFinish(holder);
                holdersToRemove.add(holder);
            }
        }

        if(!holdersToRemove.isEmpty())
            // disposeHolders must be called after onTick() has ended for removing irrelevant holders
            disposeHolders();
    }

    private void disposeHolders() {
        for(FamilyViewHolder holder : holdersToRemove) {
            holders.remove(holder);
        }

        holdersToRemove.clear();
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
