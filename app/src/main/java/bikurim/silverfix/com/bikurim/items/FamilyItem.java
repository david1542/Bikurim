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

    public FamilyItem() {

    }

    public FamilyItem(String lastName, long time) {
        this.lastName = lastName;
        this.time = time;
    }
}
