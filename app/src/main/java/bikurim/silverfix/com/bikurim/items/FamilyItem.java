package bikurim.silverfix.com.bikurim.items;

import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.adapters.RecyclerViewAdapter;

/**
 * Created by David on 06/06/2016.
 */
public class FamilyItem {

    public String lastName = null;
    public long time;
    public RecyclerViewAdapter.PersonViewHolder holder;

    public FamilyItem() {

    }

    public FamilyItem(String lastName, long time) {
        this.lastName = lastName;
        this.time = time;
    }

    public void setUpTimer() {
        new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                holder.timeLeft.setText(getFormatTime(millisUntilFinished));
            }

            public void onFinish() {
                holder.timeLeft.setText("Done!");
            }
        }.start();
    }

    public void setViewHolder(RecyclerViewAdapter.PersonViewHolder holder) {
        this.holder = holder;
    }

    public RecyclerViewAdapter.PersonViewHolder getViewHolder(View v) {
        if (holder == null)
            holder = new RecyclerViewAdapter.PersonViewHolder(v);
        return holder;
    }

    public String getFormatTime(long millisUntilFinished) {
        String hours, minutes, seconds, result;
        long timeLeft = millisUntilFinished / 1000;
        seconds = "" + timeLeft % 60;
        minutes = "" + timeLeft / 60;
        timeLeft /= 60;
        hours = "" + timeLeft / 60;
        if (Integer.parseInt(hours) < 10)
            hours = "0" + hours;
        if (Integer.parseInt(seconds) < 10)
            seconds = "0" + seconds;
        if (Integer.parseInt(minutes) < 10)
            minutes = "0" + minutes;
        result = hours + ":" + minutes + ":" + seconds;
        return result;
    }


}
