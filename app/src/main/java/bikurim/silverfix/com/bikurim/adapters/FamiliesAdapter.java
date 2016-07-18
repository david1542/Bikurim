package bikurim.silverfix.com.bikurim.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import java.util.ArrayList;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.adapters.holders.FamilyViewHolder;
import bikurim.silverfix.com.bikurim.adapters.holders.GenericViewHolder;
import bikurim.silverfix.com.bikurim.adapters.holders.TimeUpViewHolder;
import bikurim.silverfix.com.bikurim.models.Family;
import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.utils.managers.MediaPlayerManager;
import bikurim.silverfix.com.bikurim.utils.Utils;
import bikurim.silverfix.com.bikurim.utils.interfaces.HolderListener;
import bikurim.silverfix.com.bikurim.utils.managers.CountDownManager;
import bikurim.silverfix.com.bikurim.utils.interfaces.EventListener;

public class FamiliesAdapter extends GenericAdapter implements Filterable, EventListener, HolderListener {

    // last position holds the last position of the element that was added, for animation purposes
    private static int lastPosition = -1;

    private int[] viewTypes;

    private ItemTouchHelper touchHelper;

    private CountDownManager countDownManager;

    public FamiliesAdapter(Context context, ArrayList<Family> families, ItemTouchHelper touchHelper) {
        super(context, families);
        this.touchHelper = touchHelper;

        viewTypes = Constants.Tags.VIEW_TYPES;

        countDownManager = new CountDownManager(Constants.Values.TIME_INTERVAL, this);
        countDownManager.start();
    }


    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflating the view from a given layout resource file
        Log.d("A function has called", "onCreateViewHolder() was invoked");
        View v;
        switch (viewType) {
            case Constants.Tags.FAMILY_VIEW:
                v = LayoutInflater.from(context).inflate(R.layout.family_list_item, parent, false);
                FamilyViewHolder fvh = new FamilyViewHolder(context, v);
                return fvh;
            case Constants.Tags.TIME_UP_VIEW:
                v = LayoutInflater.from(context).inflate(R.layout.timeup_list_item, parent, false);
                TimeUpViewHolder tvh = new TimeUpViewHolder(context, v, this);
                return tvh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        // Binds the Family object to the holder view
        Family family = families.get(position);
        holder.bindData(family);


        if(holder instanceof FamilyViewHolder){
            if(!((FamilyViewHolder) holder).isHolderAdded) {
                countDownManager.addHolder((FamilyViewHolder) holder);

                ((FamilyViewHolder) holder).isHolderAdded = true;
            }
        }

        // Sets animation on the given view, in case it wasn't displayed before
        Utils.setSlideAnimation(context, holder.getView(), position, lastPosition, false);
    }

    @Override
    public int getItemViewType(int position) {
        if(families.get(position).timeLeft <= 0)
            return viewTypes[1];
        return viewTypes[0];
    }

    /* Changes the UI of a holder to a time's up view with a flickering ImageButton and a TextView*/

    @Override
    public void onFinish(FamilyViewHolder holder) {
        // Resets the holder for future uses
        holder.reset();

        // Play a sound for notifying purposes
        MediaPlayerManager.playSound(MediaPlayerManager.Sound.END);

        // Vibrates the device for 1.5 seconds
        Utils.vibrate(context, Constants.Values.VIBRATION_LENGTH);

        notifyItemChanged(holder.getAdapterPosition());
    }

    @Override
    public void onLessThanMinute(FamilyViewHolder holder) {
        // if the sound option is on, play a swoosh sound
        MediaPlayerManager.playSound(MediaPlayerManager.Sound.ALERT);
        Drawable stroke = ContextCompat.getDrawable(context, R.drawable.family_item_red_stroke);
        holder.frame.setBackground(stroke);

        Utils.setFadeAnimation(holder.frame);
        holder.isStrokeChanged = true;
    }

    @Override
    public void onRemoveTimeUpViewHolder(int pos, GenericViewHolder holder) {
        touchHelper.startSwipe(holder);
        removeData(pos, holder);
    }

    public void removeData(int pos, GenericViewHolder holder) {
        // Sets the last position to the given deleted position for animation purposes
        lastPosition = pos;

        families.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, getItemCount());

        if(holder instanceof FamilyViewHolder)
            // Cancels the the timer and removes it from the entry set
            countDownManager.removeHolder((FamilyViewHolder) holder);

        holder.reset();
    }



    /* Cancels the timers and clears the entry set */
    public void cancelTimers() {
        countDownManager.reset();
        countDownManager.clear();
        countDownManager.stop();
    }

    /* Clears the adapter's data and resets the last position to -1 */
    @Override
    public void clearData() {
        super.clearData();
        cancelTimers();
        lastPosition = -1;
    }
}
