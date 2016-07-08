package bikurim.silverfix.com.bikurim.utils.interfaces;

import bikurim.silverfix.com.bikurim.adapters.FamiliesAdapter;
import bikurim.silverfix.com.bikurim.adapters.holders.FamilyViewHolder;

/**
 * Created by דודו on 07/07/2016.
 */
public interface TimerEventListener {
    void onLessThanMinute(FamilyViewHolder holder);
    void onFinish(FamilyViewHolder holder);
}
